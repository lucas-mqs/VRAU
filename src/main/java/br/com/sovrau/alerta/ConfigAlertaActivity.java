package br.com.sovrau.alerta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.UsuarioDTO;
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
    private Button btnSalvarAlerta;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mChildRef;
    private UsuarioDTO usuarioDTO = new UsuarioDTO();
    private String itemAlerta;
    private int percentualAlerta = 0;
    private String percentualPalceHolder;
    private static final String TAG = ConfigAlertaActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_alerta_activity);
        initComponents();
        Intent intent = getIntent();
        usuarioDTO = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        mRootRef.child(Constants.NODE_USER).child(usuarioDTO.getIdUSuario()).child(Constants.NODE_MOTO);
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
        skPercentual.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                skPercentual.setProgress(i);
                txtPercentual.setText(percentualPalceHolder + ""+i);
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
                        mChildRef = mRootRef.child(Constants.NODE_ALERTA);
                        Map<String, Object> mappedAlerta = new HashMap();
                        mappedAlerta.put("id", CodeUtils.getInstance().getGenericID(""));
                        //"tipoAlerta", "percentualAtual", "indicador", "avisoTroca"
                        mappedAlerta.put("tipoAlerta", itemAlerta);
                        mappedAlerta.put("percentualAtual", 0);
                        mappedAlerta.put("avisoTroca", percentualAlerta);
                        mChildRef.setValue(mappedAlerta);
                        Log.i(TAG, "Alerta cadastrado com sucesso");
                    } catch (Exception e){
                        Log.e(TAG, "Erro ao cadastrar alerta: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Erro ao adicionar alerta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void initComponents(){
        this.spItens = (Spinner) findViewById(R.id.spItens);
        this.txtPercentual = (TextView) findViewById(R.id.txtPercentual);
        this.skPercentual = (SeekBar) findViewById(R.id.skDesgaste);
        this.skPercentual.incrementProgressBy(1);
        this.skPercentual.setProgress(0);
        this.skPercentual.setMax(100);
        this.percentualPalceHolder = this.txtPercentual.getText().toString();
        this.btnSalvarAlerta = (Button) findViewById(R.id.btnSalvarAlerta);
    }
    private void populateSpinner(){
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
    }
    private boolean validate(){
        if(this.percentualAlerta == 0) {
            Toast.makeText(this, "Por favor informar percentual para enviar alerta", Toast.LENGTH_SHORT).show();
            return false;
        } else if(ValidationUtils.getInstance().isNullOrEmpty(this.itemAlerta)) {
            Toast.makeText(this, "Por favor selecione um item que deseja ser alertado sobre o desgaste", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
