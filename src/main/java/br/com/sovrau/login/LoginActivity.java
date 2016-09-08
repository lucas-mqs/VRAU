package br.com.sovrau.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.user.UserHome;
import br.com.sovrau.utilities.ValidationUtils;

public class LoginActivity extends AppCompatActivity {
    private EditText txtLogin;
    private EditText txtSenha;
    private Button btnAcessar;
    private Button btnCriarC;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.txtLogin = (EditText) findViewById(R.id.txtLogin);
        this.txtSenha = (EditText) findViewById(R.id.txtSenha);
        this.btnAcessar = (Button) findViewById(R.id.btnAcessar);
        this.btnCriarC = (Button) findViewById(R.id.btnCriarContaLogin);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    UsuarioDTO usuarioLogado = new UsuarioDTO();
                    usuarioLogado.setIdUSuario(user.getUid());
                    usuarioLogado.setNome(user.getDisplayName());
                    usuarioLogado.setEmail(user.getEmail());
                    Intent intentLogado = new Intent(getApplicationContext(), UserHome.class);
                    intentLogado.putExtra(Constants.EXTRA_USUARIO_LOGADO, usuarioLogado);
                    startActivity(intentLogado);
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        btnAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(v);
            }
        });
    }

    public void doLogin(View v) {
        String login = this.txtLogin.getText().toString().toLowerCase();
        String senha = this.txtSenha.getText().toString();
        if (!validate()) {
            Toast.makeText(getBaseContext(), "Erro no Login", Toast.LENGTH_LONG).show();
            return;
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Aguarde...");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(login, senha)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail", task.getException());
                                Toast.makeText(LoginActivity.this, "Usuário/Senha inválidos",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
            progressDialog.dismiss();
        }

    }
    public void doSignup(View v) {
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean isValid = true;
        String login = txtLogin.getText().toString().toLowerCase();
        String senha = txtSenha.getText().toString();
        if (ValidationUtils.getInstance().isNullOrEmpty(senha) ||
                !ValidationUtils.getInstance().isValidLength(senha, 8)) {
            txtSenha.setError("Preencha corretamente a senha");
            isValid = false;
        } else {
            txtSenha.setError(null);
        }
        if (ValidationUtils.getInstance().isNullOrEmpty(login) || !Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
            txtLogin.setError("Preencha corretamente o email");
            isValid = false;
        } else {
            txtLogin.setError(null);
        }
        return isValid;
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
