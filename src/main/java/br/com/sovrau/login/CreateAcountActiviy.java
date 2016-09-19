package br.com.sovrau.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.user.UserHome;
import br.com.sovrau.utilities.ValidationUtils;

/**
 * Created by Lucas on 22/04/2016.
 */
public class CreateAcountActiviy extends AppCompatActivity {
    private EditText email;
    private EditText confirmarEmail;
    private EditText nome;
    private EditText senha;
    private EditText confirmarSenha;
    private Button btnCreateAccount;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = CreateAcountActiviy.class.getSimpleName();
    private String nomeUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_acount_activity);
        mAuth = FirebaseAuth.getInstance();

        this.nome = (EditText) findViewById(R.id.txtNome);
        this.email = (EditText) findViewById(R.id.txtEmail);
        this.confirmarEmail = (EditText) findViewById(R.id.txtConfEmail);
        this.senha = (EditText) findViewById(R.id.txtDigiteSenha);
        this.confirmarSenha = (EditText) findViewById(R.id.txtConfSenha);
        this.btnCreateAccount = (Button) findViewById(R.id.btnSubmitCriarConta);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Usuario Logado
                    Log.i(TAG, "Usuário Logado: " + user.getUid());
                    //Após criarmos o usuário apenas o login e senha estão disponiveis
                    //Então, iremos atualizar para incluir o nome
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(nomeUsuario).build();
                    user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Nome do usuário incluso");
                        }
                        }
                    });
                    final UsuarioDTO usuario = new UsuarioDTO();
                    usuario.setIdUSuario(mAuth.getCurrentUser().getUid());
                    usuario.setEmail(mAuth.getCurrentUser().getEmail());
                    usuario.setNome(mAuth.getCurrentUser().getDisplayName());
                    logar(usuario);
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "Usuário Deslogado");
                }
            }
        };
    }
    public void criarConta(View v) {
        String email = this.email.getText().toString();
        String confEmail = this.confirmarEmail.getText().toString();
        String nome = this.nome.getText().toString();
        String senha = this.senha.getText().toString();
        String confSenha = this.confirmarSenha.getText().toString();

        nomeUsuario = nome;
        if(validate(nome, email, confEmail, senha, confSenha)){
            //Toast.makeText(getBaseContext(), "Preencha os campos corretamente", Toast.LENGTH_LONG).show();
            final ProgressDialog progressDialog = new ProgressDialog(CreateAcountActiviy.this, R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Aguarde...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.e(TAG, "createUserWithEmail:error: " + task.getException().getMessage());
                                Toast.makeText(CreateAcountActiviy.this, "Falha na Autenticação.", Toast.LENGTH_SHORT).show();
                            } else {
                                final UsuarioDTO usuario = new UsuarioDTO();
                                usuario.setIdUSuario(mAuth.getCurrentUser().getUid());
                                usuario.setEmail(mAuth.getCurrentUser().getEmail());
                                usuario.setNome(mAuth.getCurrentUser().getDisplayName());
                                logar(usuario);
                            }
                        }
                    });
            progressDialog.dismiss();
        }
    }
    public void logar(final UsuarioDTO user) {
        if (user == null) {
            Toast.makeText(this, "Erro ao criar conta", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Por favor, preencha seu cadastro novamente", Toast.LENGTH_SHORT).show();
            this.nome.getText().clear();
            this.email.getText().clear();
            this.confirmarEmail.getText().clear();
            this.senha.getText().clear();
            this.confirmarSenha.getText().clear();
            this.nome.requestFocus();
        } else{
            mAuth.signInWithEmailAndPassword(user.getEmail(), user.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Log.e(TAG, "Erro ao logar", task.getException());
                        Toast.makeText(getApplicationContext(), "Erro ao realizar o sign-in", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(getBaseContext(), UserHome.class);
                    intent.putExtra(Constants.EXTRA_USUARIO_LOGADO, user);
                    startActivity(intent);
                }
            });

        }
    }
    public boolean validate(String nome, String email, String confEmail, String senha, String confSenha){
        boolean isValid = true;
        if(ValidationUtils.getInstance().isNullOrEmpty(nome)){
            this.nome.setError("Nome é obrigatório");
            isValid = false;
        }
        if (!ValidationUtils.getInstance().isEqualsAndNotNull(email, confEmail)) {
            this.email.setError("Preencha o email corretamente");
            isValid = false;
        }
        if (!ValidationUtils.getInstance().isValidEmail(email) || !ValidationUtils.getInstance().isValidEmail(confEmail)) {
            this.email.setError("Email Inválido");
            isValid = false;
        }
        if (!ValidationUtils.getInstance().isEqualsAndNotNull(senha, confSenha) || !ValidationUtils.getInstance().isValidLength(senha, 8)) {
            this.senha.setError("Preencha os campos de senha corretamente");
            isValid = false;
        }
        return isValid;
    }
    private void saveLocalUser(usuarioDTO){
        CodeUtils().getInstance().saveSP(this, "idUsuario", usuarioDTO.getIdUsuario());
        CodeUtils().getInstance().saveSP(this, "email", usuarioDTO.getEmail());
        CodeUtils().getInstance().saveSP(this, "nome", usuarioDTO.getNome());
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
