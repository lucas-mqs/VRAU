package br.com.sovrau.fragments;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.utilities.CodeUtils;
import br.com.sovrau.utilities.ValidationUtils;

/**
 * Created by Lucas on 11/09/2016.
 */
public class PercursoManualFragment extends Fragment {
    private RadioGroup rdTipoPercurso;
    private RadioButton tipoPercurso;
    private EditText txtInicioPercurso;
    private EditText txtFinalPercurso;
    private EditText txtOdometroInicial;
    private EditText txtOdometroFinal;
    private EditText txtObs;
    private Spinner spMotosPercurso;
    private AppCompatButton btnIniciaPercurso;
    private List<MotoDTO> listMotos = new ArrayList<>();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPercursoRef;
    private MotoDTO motoDTO;
    private MotoDTO motoEscolhida = new MotoDTO();
    private UsuarioDTO usuario;
    private static final String TAG = PercursoManualFragment.class.getSimpleName();

    public static PercursoManualFragment newInstance() {
        return new PercursoManualFragment();
    }

    public PercursoManualFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.percurso_manual_fragment, container, false);
        initComponents(view);
        Intent intent = getActivity().getIntent();
        usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        motoDTO = (MotoDTO) intent.getSerializableExtra(Constants.EXTRA_MOTO_ADICIONADA);
        spMotosPercurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String escolha = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < listMotos.size(); i++){
                     if(listMotos.get(i).getNmMoto().equals(escolha)){
                       motoEscolhida = listMotos.get(i);
                   }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        populateSpinner();
        btnIniciaPercurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData(view);
                Class fragmentClass = ListaVeiculosFragment.class;
                try {
                    Fragment fragment = (Fragment) fragmentClass.newInstance();
                    getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
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
        this.btnIniciaPercurso = (AppCompatButton) view.findViewById(R.id.btnIniciar);
        this.spMotosPercurso = (Spinner) view.findViewById(R.id.spListaMotosPercurso);
    }
    //TODO: Deixar Inicio e Fim do Percurso Opcionais
    private boolean validate(String txtInicioPercurso, String txtFinalPercurso, String txtOdometroInicial) {
        boolean isValid = true;
        if (ValidationUtils.getInstance().isNullOrEmpty(txtInicioPercurso)) {
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, preencha um início", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (ValidationUtils.getInstance().isNullOrEmpty(txtFinalPercurso)) {
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, preencha um destino", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (ValidationUtils.getInstance().isNullOrEmpty(txtOdometroInicial)) {
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, informe a quilometragem atual da moto", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private void insertData(View view) {
        String txtInicioPercurso = this.txtInicioPercurso.getText().toString();
        String txtFinalPercurso = this.txtFinalPercurso.getText().toString();
        String txtOdometroInicial = this.txtOdometroInicial.getText().toString();
        String txtOdometroFinal = this.txtOdometroFinal.getText().toString();
        String txtMotivo = this.txtObs.getText().toString();
        int localCelular = this.rdTipoPercurso.getCheckedRadioButtonId();
        this.tipoPercurso = (RadioButton) view.findViewById(localCelular);

        if (!validate(txtInicioPercurso, txtFinalPercurso, txtOdometroInicial)) {
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, preencha os campos corretamente", Toast.LENGTH_LONG).show();
            return;
        } else {
            String percursoID = CodeUtils.getInstance().getGenericID("Percurso");
            Map<String, Object> mappedPercurso = new HashMap<>();
            mappedPercurso.put("id", percursoID);
            mappedPercurso.put("inicio", txtInicioPercurso);
            mappedPercurso.put("final", txtFinalPercurso);
            mappedPercurso.put("odometroInicial", txtOdometroInicial);
            mappedPercurso.put("odometroFinal", txtOdometroFinal);
            mappedPercurso.put("motivo", txtMotivo);
            mappedPercurso.put("isMedirAuto", false);
            mappedPercurso.put("tipoPercurso", tipoPercurso.getText());
            mappedPercurso.put("moto", motoDTO.getIdMoto());

            try {
                mPercursoRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario()).child(Constants.NODE_MOTO).child(motoDTO.getIdMoto()).child(Constants.NODE_PERCURSO);
                mPercursoRef.child(percursoID).setValue(mappedPercurso);
            } catch (Exception e) {
                Log.e("ERR_INSERT_PERCURSO", "Erro ao inserir percurso: " + e.getMessage());
                Toast.makeText(getContext(), "Erro ao inserir percurso:\nPor favor, tente novamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateSpinner() {
        listMotos();
        List<String> motoID = new ArrayList<>();
        for (MotoDTO motoDTO : listMotos) {
            motoID.add(motoDTO.getNmMoto());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, motoID);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spMotosPercurso.setAdapter(dataAdapter);

    }

    private void listMotos() {
        mRootRef.child(usuario.getIdUSuario()).child(Constants.NODE_MOTO).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<List<MotoDTO>> genericList = new GenericTypeIndicator<List<MotoDTO>>() {
                            @Override
                            protected Object clone() throws CloneNotSupportedException {
                                return super.clone();
                            }
                        };
                        listMotos = dataSnapshot.getValue(genericList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Erro ao recuperar lista de motos: " + databaseError.getMessage());
                    }
                }
        );

    }
}