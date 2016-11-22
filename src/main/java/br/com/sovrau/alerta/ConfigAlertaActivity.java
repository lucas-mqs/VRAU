package br.com.sovrau.alerta;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.AlertaDTO;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.fragments.ListaAlertaFragment;
import br.com.sovrau.user.UserHome;
import br.com.sovrau.utilities.CodeUtils;
import br.com.sovrau.utilities.ValidationUtils;

/**
 * Created by Lucas on 20/09/2016.
 */
//Criar tela de licenciamento para enviar alertas ao usuário

public class ConfigAlertaActivity extends Activity {

    private Spinner spItens;
    private TextView txtPercentual;
    private SeekBar skPercentual;
    private Spinner spMotosAlerta;
    private Button btnSalvarAlerta;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mChildRef;
    private DatabaseReference mMotoRef;
    private UsuarioDTO usuarioDTO = new UsuarioDTO();
    private String itemAlerta;
    private int percentualAlerta = 0;
    private String percentualPalceHolder;
    private List<MotoDTO> listMoto = new ArrayList();
    private ArrayList<AlertaDTO> listAlertas = new ArrayList<>();
    private MotoDTO motoEscolhida;
    private static final String TAG = ConfigAlertaActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_alerta_activity);
        initComponents();
        final Intent intent = getIntent();
        usuarioDTO = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        mMotoRef = mRootRef.child(Constants.NODE_DATABASE).child(usuarioDTO.getIdUSuario());
        if(intent.hasExtra(Constants.EXTRA_ALERTA_ADICIONADO)){
            listAlertas = (ArrayList<AlertaDTO>) intent.getSerializableExtra(Constants.EXTRA_ALERTA_ADICIONADO);
        }
        populateSpinner();

        spItens.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemAlerta = (String) adapterView.getItemAtPosition(i);
                Log.i(TAG, "item escolhido: " + itemAlerta);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMotosAlerta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String escolha = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < listMoto.size(); i++){
                    if(listMoto.get(i).getNmMoto().equals(escolha)){
                        motoEscolhida = listMoto.get(i);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        skPercentual.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                skPercentual.setProgress(i);
                txtPercentual.setText(percentualPalceHolder + i + "%");
                percentualAlerta = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSalvarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    try {
                        String genericAlertaID = CodeUtils.getInstance().getGenericID(itemAlerta);
                        Map<String, Object> mappedAlerta = new HashMap();
                        mappedAlerta.put(Constants.ID, genericAlertaID);
                        mappedAlerta.put(Constants.TIPO_ALERTA, itemAlerta);
                        mappedAlerta.put(Constants.PERCENTUAL_ATUAL, 0);
                        mappedAlerta.put(Constants.AVISO_TROCA, percentualAlerta);
                        mappedAlerta.put(Constants.ATIVO, true);
                        mappedAlerta.put(Constants.KM_RODADOS, motoEscolhida.getOdometro());
                        mappedAlerta.put(Constants.KM_FALTANTES, 10000);
                        mappedAlerta.put(Constants.NODE_MOTO, motoEscolhida.getIdMoto());

                        mMotoRef.child(Constants.NODE_ALERTA).child(genericAlertaID).setValue(mappedAlerta);

                        mChildRef = mMotoRef.child(Constants.NODE_MOTO).child(motoEscolhida.getIdMoto()).child(Constants.NODE_ALERTA).child(genericAlertaID);
                        mChildRef.child(Constants.TIPO_ALERTA).setValue(itemAlerta);
                        mChildRef.child(Constants.PERCENTUAL_ATUAL).setValue(0);
                        mChildRef.child(Constants.AVISO_TROCA).setValue(percentualAlerta);
                        mChildRef.child(Constants.ID).setValue(genericAlertaID);
                        mChildRef.child(Constants.ATIVO).setValue(true);
                        mChildRef.child(Constants.KM_RODADOS).setValue(motoEscolhida.getOdometro());
                        //TODO: criar metodo para retornar maximo de KM por item de acordo com a moto
                        mChildRef.child(Constants.KM_FALTANTES).setValue(100000);
                        mChildRef.child(Constants.NODE_MOTO).setValue(motoEscolhida.getIdMoto());

                        Log.i(TAG, "Alerta cadastrado com sucesso");
                        try {
                            Intent intentAlerta = new Intent(getApplicationContext(), UserHome.class);
                            intentAlerta.putExtra(Constants.EXTRA_USUARIO_LOGADO, usuarioDTO);
                            intentAlerta.putExtra(Constants.EXTRA_ALERTA_ADICIONADO, ListaAlertaFragment.class.getName());
                            startActivity(intentAlerta);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    } catch (Exception e){
                        Log.e(TAG, "Erro ao cadastrar alerta: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Erro ao adicionar alerta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void initComponents(){
        this.spMotosAlerta = (Spinner) findViewById(R.id.spMotosAlerta);
        this.spItens = (Spinner) findViewById(R.id.spItens);
        this.txtPercentual = (TextView) findViewById(R.id.txtPercentual);
        this.skPercentual = (SeekBar) findViewById(R.id.skDesgaste);
        this.skPercentual.incrementProgressBy(1);
        this.skPercentual.setProgress(0);
        this.skPercentual.setMax(100);
        this.percentualPalceHolder = this.txtPercentual.getText().toString();
        this.btnSalvarAlerta = (Button) findViewById(R.id.btnSalvarAlerta);
    }
    private void populateSpinner() {
        List<String> listItens = new ArrayList<>();
        listItens.add("Óleo");
        listItens.add("Pastilhas de Freio");
        listItens.add("Liquido de Arrefecimento");
        listItens.add("Caixa de Direção");
        listItens.add("Pneus");
        listItens.add("Freios");
        Collections.sort(listItens);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listItens);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spItens.setPrompt("Itens");
        this.spItens.setAdapter(dataAdapter);

        mMotoRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> mapMotos = (Map<String, Object>) postSnapshot.getValue();
                    Log.i(TAG, "Data: " + postSnapshot.getValue());
                    listMoto.addAll(CodeUtils.getInstance().parseMapToListMoto(mapMotos));
                }
                List<String> motoID = new ArrayList<>();
                for (MotoDTO motoDTO : listMoto) {
                    motoID.add(motoDTO.getNmMoto());
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(ConfigAlertaActivity.this, android.R.layout.simple_spinner_item, motoID);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMotosAlerta.setAdapter(dataAdapter);
                if(listMoto.isEmpty()) {
                    Toast.makeText(ConfigAlertaActivity.this, "É preciso adicionar ao menos uma moto para iniciar um percurso", Toast.LENGTH_SHORT).show();
                    btnSalvarAlerta.setClickable(false);
                } else if (listMoto.size() == 1) {
                    spMotosAlerta.setSelection(dataAdapter.getPosition(listMoto.get(0).getNmMoto()));
                    spMotosAlerta.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
    private boolean validate(){
        if(this.percentualAlerta == 0) {
            Toast.makeText(this, "Por favor informar percentual para enviar alerta", Toast.LENGTH_SHORT).show();
            return false;
        } else if(ValidationUtils.getInstance().isNullOrEmpty(this.itemAlerta)) {
            Toast.makeText(this, "Por favor selecione um item que deseja ser alertado sobre o desgaste", Toast.LENGTH_LONG).show();
            return false;
        }
        for (AlertaDTO alerta : listAlertas) {
            if(alerta.getIdMoto().equals(motoEscolhida.getIdMoto())
                    && alerta.getTipoAlerta().equalsIgnoreCase(this.itemAlerta)){
                //Usuário já possui um alerta de um determinado item para sua moto.
                //Não é preciso criar outro.
                AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                dialog.setTitle("Atenção").setMessage("Você já possui um alerta cadastrado para este item nesta moto.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                return false;
            }
        }
        return true;
    }
}
