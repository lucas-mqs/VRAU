package br.com.sovrau.percurso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import br.com.sovrau.R;

/**
 * Created by Lucas on 04/09/2016.
 */
public class MonitorAvisoActivity extends Activity {
    private static final String TAG = MonitorAvisoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor_aviso_activity);
        Intent intentService = new Intent(this, GPSLocationService.class);
        intentService.putExtras(getIntent());
        getApplicationContext().startService(intentService);
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
}
