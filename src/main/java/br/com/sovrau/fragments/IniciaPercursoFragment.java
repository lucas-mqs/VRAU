package br.com.sovrau.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import br.com.sovrau.R;
import br.com.sovrau.utilities.CodeUtils;
import br.com.sovrau.utilities.ValidationUtils;

/**
 * Created by Lucas on 11/09/2016.
 */
public class IniciaPercursoFragment extends Fragment {
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

    public static IniciaPercursoFragment newInstance() {
        return new IniciaPercursoFragment();
    }
    public  IniciaPercursoFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.inicia_percurso_activity, container, false);
        initComponents(view);
        //Após iniciar a interface verificamos se o serviço de localização está ativo
        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        btnIniciaPercurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                handleGPS(locManager);
                insertData(view);
            }
        });
        return view;
    }
    private void initComponents(View view) {
        this.rdTipoPercurso = (RadioGroup) view.findViewById(R.id.rdTipoPercurso);
        this.txtInicioPercurso = (EditText) view.findViewById(R.id.txtInicio);
        this.txtFinalPercurso = (EditText) view.findViewById(R.id.txtFinal);
        this.txtOdometroInicial = (EditText) view.findViewById(R.id.txtOdometroInicial);
        this.txtOdometroFinal = (EditText) view.findViewById(R.id.txtOdometroFinal);
        this.txtObs = (EditText) view.findViewById(R.id.txtMotivo);
        this.isMedirAuto = (CheckBox) view.findViewById(R.id.chMedicaoAut);
        this.isDetectarFim = (CheckBox) view.findViewById(R.id.chDetectFimPercurso);
        this.btnIniciaPercurso = (AppCompatButton) view.findViewById(R.id.btnIniciar);
    }
    private void handleGPS(LocationManager locManager){
        if(!CodeUtils.getInstance().isGPSProviderEnabled(locManager)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
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
                    Toast.makeText(getActivity().getApplicationContext(), "Para utilizar a medição automatica de percurso, por favor habilitar a localização nas configurações", Toast.LENGTH_LONG).show();
                }
            });
            builder.create().show();
        }
    }
    private boolean validate(String txtInicioPercurso, String txtFinalPercurso, String txtOdometroInicial){
        boolean isValid = true;
        if(ValidationUtils.getInstance().isNullOrEmpty(txtInicioPercurso)){
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, preencha um início", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(ValidationUtils.getInstance().isNullOrEmpty(txtFinalPercurso)){
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, preencha um destino", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(ValidationUtils.getInstance().isNullOrEmpty(txtOdometroInicial)){
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, informe a quilometragem atual da moto", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }
    private void insertData(View view){
        String txtInicioPercurso = this.txtInicioPercurso.getText().toString();
        String txtFinalPercurso = this.txtFinalPercurso.getText().toString();
        String txtOdometroInicial = this.txtOdometroInicial.getText().toString();
        String txtOdometroFinal = this.txtOdometroFinal.getText().toString();
        String txtMotivo = this.txtObs.getText().toString();
        boolean isMedirAuto = this.isMedirAuto.isChecked();
        boolean isDetectarFimPercurso = this.isDetectarFim.isChecked();
        int localCelular = this.rdTipoPercurso.getCheckedRadioButtonId();
        this.tipoPercurso = (RadioButton) view.findViewById(localCelular);

        if(!validate(txtInicioPercurso, txtFinalPercurso, txtOdometroInicial)){
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, preencha os campos corretamente", Toast.LENGTH_LONG).show();
            return;
        } else{

        }
    }
}
