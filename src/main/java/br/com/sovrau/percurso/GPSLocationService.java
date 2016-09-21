package br.com.sovrau.percurso;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.PercursoDTO;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.utilities.CodeUtils;

/**
 * Created by Lucas on 20/09/2016.
 */
public class GPSLocationService extends Service {
    private final static String TAG = GPSLocationService.class.getSimpleName();
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private static final List<Double> listVelocidades = new ArrayList<>();
    private static long starTime;
    private static long endTime;
    private Location locationInicial;
    private Location locationFinal;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPercursoRef;

    private UsuarioDTO usuario;
    private MotoDTO moto;
    private PercursoDTO percurso;

    private class LocationListener implements android.location.LocationListener{
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }
        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            double actualSpeed = CodeUtils.getInstance().convertMPStoKMH(location.getSpeed());
            Log.i(TAG, "Velocidade Atual Km/h: " + actualSpeed);
            listVelocidades.add(actualSpeed);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        moto = (MotoDTO) intent.getSerializableExtra(Constants.EXTRA_MOTO_ADICIONADA);
        percurso = (PercursoDTO) intent.getSerializableExtra(Constants.EXTRA_PERCURSO_ADICIONADO);
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
                starTime = SystemClock.elapsedRealtime();
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String provider = mLocationManager.getBestProvider(criteria, true);
                locationInicial = mLocationManager.getLastKnownLocation(provider);

                mPercursoRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario()).child(Constants.NODE_MOTO).child(Constants.NODE_PERCURSO);
                addNotification();
            }
        } catch (Exception ex) {
            Log.i(TAG, "fail to request location update, ignore: " + ex.getMessage(), ex);
        }
    }
    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
        endTime = SystemClock.elapsedRealtime();
        long totalTime = endTime - starTime;
        String totalTimeFormatted = CodeUtils.getInstance().getTimeFormatted(totalTime);
        locationFinal = mLocationManager.getLastKnownLocation(getProvider());
        double distance =  locationInicial.distanceTo(locationFinal);
        double avgVelocityMpS = distance / (totalTime / 1000.0);
        double avgVelocityKmH = CodeUtils.getInstance().convertMPStoKMH(avgVelocityMpS);

        Map<String,Object> map = new HashMap<>();
        map.put("totalTime", totalTime / 1000.0);
        map.put("totalTimeFormatted", totalTimeFormatted);
        map.put("distance", distance);
        map.put("velocidadeMediaMPS", avgVelocityMpS);
        map.put("velocidadeMediaKmH", avgVelocityKmH);
        mPercursoRef.child(percurso.getIdPercurso()).updateChildren(map);
        removeNotification();
    }
    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
    private String getProvider(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        return mLocationManager.getBestProvider(criteria, true);
    }
    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, MonitorAvisoActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1337, builder.build());
    }

    // Remove notification
    private void removeNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(1337);
    }
}

