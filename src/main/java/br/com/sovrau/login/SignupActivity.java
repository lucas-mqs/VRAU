package br.com.sovrau.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.sovrau.R;

/**
 * Created by Lucas on 22/04/2016.
 */
public class SignupActivity extends FragmentActivity {
    /* Request code used to invoke sign in user interactions. */
    private Button btnFacebook;
    private Button btnGooglePlus;
    private Button btnCreateAccount;
    private LoginButton facebookLoginButton;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.signup_activity);

        initComponents();
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    LoginManager.getInstance().logOut();
                } else {
                }
            }
        });

        CallbackManager callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setReadPermissions("public_profile", "user_friends", "email");
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

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
        dialog = ProgressDialog.show(this, "Validando Login", "Aguarde...", false, true);

        Toast.makeText(this, "Função ainda não implementada", Toast.LENGTH_SHORT).show();
    }

    public void doGooglePlusLogin(View v) {
        Toast.makeText(this, "Função ainda não implementada", Toast.LENGTH_SHORT).show();
    }

    public void goToAccountCreation(View v) {
        startActivity(new Intent(this, CreateAcountActiviy.class));
    }
    private void initComponents() {
        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnGooglePlus = (Button) findViewById(R.id.btnGplus);
        btnCreateAccount = (Button) findViewById(R.id.btnCriarConta);

        facebookLoginButton = (LoginButton) findViewById(R.id.btnFacebook);
        facebookLoginButton.setReadPermissions("email");
    }
 }

