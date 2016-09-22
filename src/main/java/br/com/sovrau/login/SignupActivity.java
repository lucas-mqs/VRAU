package br.com.sovrau.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.api.GoogleApiClient;

import br.com.sovrau.R;

/**
 * Created by Lucas on 22/04/2016.
 */
public class SignupActivity extends FragmentActivity {
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    private Button btnFacebook;
    private Button btnGooglePlus;
    private Button btnCreateAccount;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnGooglePlus = (Button) findViewById(R.id.btnGplus);
        btnCreateAccount = (Button) findViewById(R.id.btnCriarConta);
        FacebookSdk.sdkInitialize(getApplicationContext());

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnFacebook:
                        doFacebookLogin(v);
                }
            }
        });
        btnGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnGplus:
                        doGooglePlusLogin(v);
                }
            }
        });
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnCriarConta:
                        goToAccountCreation(v);
                }
            }
        });
    }
    public void doFacebookLogin(View v) {
        Toast.makeText(this, "Função ainda não implementada", Toast.LENGTH_SHORT).show();
    }

    public void doGooglePlusLogin(View v) {
        Toast.makeText(this, "Função ainda não implementada", Toast.LENGTH_SHORT).show();
    }

    public void goToAccountCreation(View v) {
        startActivity(new Intent(this, CreateAcountActiviy.class));
    }
}

