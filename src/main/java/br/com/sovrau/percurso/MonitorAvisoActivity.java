package br.com.sovrau.percurso;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import br.com.sovrau.R;

/**
 * Created by Lucas on 04/09/2016.
 */
public class MonitorAvisoActivity extends Activity {
    private LocationManager mLocationManager;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 100; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 30000;

    private static final String TAG = MonitorAvisoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor_aviso_activity);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MINIMUM_TIME_BETWEEN_UPDATES,
                        MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                        customLocationListener
                );
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String provider = mLocationManager.getBestProvider(criteria, true);
                Location location = mLocationManager.getLastKnownLocation(provider);

                String message = String.format(
                        "Localização Atual \n Longitude: %1$s \n Latitude: %2$s",
                        location.getLongitude(), location.getLatitude()
                );
                Log.i(TAG, message);
            }
        }catch(Exception e){}
    }
    LocationListener customLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onPause() {
        super.onPause();
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(customLocationListener);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, customLocationListener);
        }

    }
}
