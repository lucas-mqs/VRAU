package br.com.sovrau.percurso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.sovrau.R;

/**
 * Created by Lucas on 04/09/2016.
 */
public class MonitorAvisoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = MonitorAvisoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor_aviso_activity);
        if(savedInstanceState == null){
            SupportMapFragment fragment = new SupportMapFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.map, fragment);
            fragmentTransaction.commit();
        }
        else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        Intent intentService = new Intent(this, GPSLocationService.class);
        intentService.putExtras(getIntent());
        getApplicationContext().startService(intentService);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Percurso"));
    }
}
