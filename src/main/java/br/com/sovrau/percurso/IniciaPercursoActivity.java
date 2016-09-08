package br.com.sovrau.percurso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import br.com.sovrau.R;
import br.com.sovrau.utilities.CodeUtils;
import br.com.sovrau.utilities.ValidationUtils;

/**
 * Created by Lucas on 04/09/2016.
 */
public class IniciaPercursoActivity extends Activity{
    private RadioGroup rdTipoPercurso;
    private RadioButton tipoPercurso;
    private EditText txtInicioPercurso;
    private EditText txtFinalPercurso;
    private EditText txtOdometroInicial;
    private EditText txtOdometroFinal;
    private EditText txtObs;
    private CheckBox isMedirAuto;
    private CheckBox isDetectarFim;
    private AppCompatButton btnIniciaPercurso;

    private LocationManager locManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicia_percurso_activity);

        initComponents();
        //Após iniciar a interface verificamos se o serviço de localização está ativo
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        btnIniciaPercurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                handleGPS(locManager);
                insertData();
            }
        });
    }

    private void initComponents() {
        this.rdTipoPercurso = (RadioGroup) findViewById(R.id.rdTipoPercurso);
        this.txtInicioPercurso = (EditText) findViewById(R.id.txtInicio);
        this.txtFinalPercurso = (EditText) findViewById(R.id.txtFinal);
        this.txtOdometroInicial = (EditText) findViewById(R.id.txtOdometroInicial);
        this.txtOdometroFinal = (EditText) findViewById(R.id.txtOdometroFinal);
        this.txtObs = (EditText) findViewById(R.id.txtMotivo);
        this.isMedirAuto = (CheckBox) findViewById(R.id.chMedicaoAut);
        this.isDetectarFim = (CheckBox) findViewById(R.id.chDetectFimPercurso);
        this.btnIniciaPercurso = (AppCompatButton) findViewById(R.id.btnIniciar);
    }

    private void handleGPS(LocationManager locManager){
        if(!CodeUtils.getInstance().isGPSProviderEnabled(locManager)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Atenção!");
            builder.setMessage("Desculpe, a localização não pôde ser determinada. Por favor, habilite a localização.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Para utilizar a medição automatica de percurso, por favor habilitar a localização nas configurações", Toast.LENGTH_LONG).show();
                }
            });
            builder.create().show();
        }
    }
    private boolean validate(String txtInicioPercurso, String txtFinalPercurso, String txtOdometroInicial){
        boolean isValid = true;
        if(ValidationUtils.getInstance().isNullOrEmpty(txtInicioPercurso)){
            Toast.makeText(this, "Por favor, preencha um início", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(ValidationUtils.getInstance().isNullOrEmpty(txtFinalPercurso)){
            Toast.makeText(this, "Por favor, preencha um destino", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(ValidationUtils.getInstance().isNullOrEmpty(txtOdometroInicial)){
            Toast.makeText(this, "Por favor, informe a quilometragem atual da moto", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }
    private void insertData(){
        String txtInicioPercurso = this.txtInicioPercurso.getText().toString();
        String txtFinalPercurso = this.txtFinalPercurso.getText().toString();
        String txtOdometroInicial = this.txtOdometroInicial.getText().toString();
        String txtOdometroFinal = this.txtOdometroFinal.getText().toString();
        String txtMotivo = this.txtObs.getText().toString();
        boolean isMedirAuto = this.isMedirAuto.isChecked();
        boolean isDetectarFimPercurso = this.isDetectarFim.isChecked();
        int localCelular = this.rdTipoPercurso.getCheckedRadioButtonId();
        this.tipoPercurso = (RadioButton) findViewById(localCelular);

        if(!validate(txtInicioPercurso, txtFinalPercurso, txtOdometroInicial)){
            Toast.makeText(getBaseContext(), "Por favor, preencha os campos corretamente", Toast.LENGTH_LONG).show();
            return;
        } else{

        }
    }
}
