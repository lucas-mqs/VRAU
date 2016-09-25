package br.com.sovrau.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import br.com.sovrau.R;
import br.com.sovrau.utilities.ValidationUtils;

/**
 * Created by Lucas on 11/09/2016.
 */
public class RevisaoManualFragment extends Fragment {
    private EditText dtRevisao;
    private EditText txtOdometro;
    private EditText txtTipoServico;
    private EditText txtValor;
    private EditText txtLocal;
    private EditText txtObs;
    private Spinner spMotoRevisada;
    private AppCompatButton btnSalvar;
    private String strDataRevisao;
    private static final int DATE_DIALOG_ID = 0;
    private static final String TAG = RevisaoManualFragment.class.getSimpleName();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public static RevisaoManualFragment newInstance() {
        Bundle args = new Bundle();

        RevisaoManualFragment fragment = new RevisaoManualFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RevisaoManualFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.revisao_manual_activity, container, false);
        initComponents(view);
        dtRevisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == dtRevisao)
                    onCreateDialog(DATE_DIALOG_ID);
            }
        });
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    insertData(v);
                } catch (Exception e){
                    Log.e(TAG, e.getMessage());
                }
            }
        });

        return view;
    }
    private void initComponents(View view){
        this.spMotoRevisada = (Spinner) view.findViewById(R.id.spListaMotosPercurso);
        this.dtRevisao = (EditText) view.findViewById(R.id.dtRevisao);
        this.txtOdometro = (EditText) view.findViewById(R.id.txtOdometro);
        this.txtTipoServico = (EditText) view.findViewById(R.id.txtTipoServico);
        this.txtLocal = (EditText) view.findViewById(R.id.txtLocal);
        this.txtValor = (EditText) view.findViewById(R.id.txtValor);
        this.txtObs = (EditText) view.findViewById(R.id.txtObs);
        this.btnSalvar = (AppCompatButton) view.findViewById(R.id.btnSalvar);

    }
    private void insertData(View view){
        String dtRevisao = this.dtRevisao.getText().toString();
        String quilometragem = this.txtOdometro.getText().toString();
        String tipoServico = this.txtTipoServico.getText().toString();
        String local = this.txtLocal.getText().toString();
        String valor = this.txtValor.getText().toString();
        //FIXME
        String moto = this.spMotoRevisada.getSelectedItem().toString();

        if(!validate(moto, dtRevisao, quilometragem, tipoServico, local, valor)){
            Toast.makeText(getContext(), "Por favor, preencha os campos corretamente", Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(getActivity(), mDateSetListener, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            strDataRevisao = String.valueOf(dayOfMonth <= 9 ? "0"+ dayOfMonth : dayOfMonth) + "/" +
                    String.valueOf((monthOfYear + 1) <= 9 ? "0"+ monthOfYear : monthOfYear ) + "/" +
                    String.valueOf(year);
            dtRevisao.setText(strDataRevisao);
        }
    };
    private boolean validate(String moto ,String dataRevisao, String quilometragem, String tipoServico, String local, String valor){
        boolean isValid = true;
        if(ValidationUtils.getInstance().isNullOrEmpty(moto)) {
            isValid = false;
        }
        if(ValidationUtils.getInstance().isNullOrEmpty(dataRevisao)){
            this.dtRevisao.setError("Por favor, selecione uma data");
            isValid = false;
        }
        if(ValidationUtils.getInstance().isNullOrEmpty(quilometragem)){
            this.txtOdometro.setError("Por favor, informe a quilometragem no momento da revisão");
            isValid = false;
        }
        if(ValidationUtils.getInstance().isNullOrEmpty(tipoServico)) {
            this.txtTipoServico.setError("Por favor, informe o serviço realizado");
            isValid = false;
        }
        if(ValidationUtils.getInstance().isNullOrEmpty(local)) {
            this.txtLocal.setError("Por favor, informe o local onde a revisão foi realizada");
            isValid = false;
        }
        if(ValidationUtils.getInstance().isNullOrEmpty(valor)) {
            this.txtValor.setError("Por favor, informe o valor da revisão");
            isValid = false;
        }
        return isValid;
    }
}
