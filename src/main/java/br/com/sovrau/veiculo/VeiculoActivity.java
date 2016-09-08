package br.com.sovrau.veiculo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.MarcaDTO;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.utilities.ValidationUtils;

/**
 * Created by Lucas on 04/05/2016.
 */
public class VeiculoActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private EditText txtNomeVeiculo;
    private Spinner spMarcaVeiculo;
    private EditText txtModelo;
    private EditText txtPlaca;
    private EditText txtAno;
    private TextView lblCilindradas;
    private SeekBar barCilindradas;
    private EditText txtTanque;
    private EditText txtObs;
    private AppCompatButton btnCadVeiculo;
    private final int MINIMO_CILINDRADAS = 50;
    private final int MAXIMO_CILINDRADAS = 1600;
    private static final int step = 25;
    private int cilindradasCorrigido;
    private MarcaDTO marcaEscolhida = new MarcaDTO();

    private UsuarioDTO usuario;
    private Map<Long, String> mapMarcaDTO = new HashMap<>();
    private String cilindradasPlaceHolder;
    private MotoDTO motoEditar;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mVeiculoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.veiculo_activity);
        initComponents();

        spMarcaVeiculo.setOnItemSelectedListener(this);
        populateSpinner();

        final Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_USUARIO_LOGADO)) {
            usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        } else if (intent.hasExtra(Constants.EXTRA_MOTO_EDITAR)){
            motoEditar = new MotoDTO();
            motoEditar = (MotoDTO) intent.getSerializableExtra(Constants.EXTRA_MOTO_EDITAR);

            txtNomeVeiculo.setText(motoEditar.getNmMoto());
            txtModelo.setText(motoEditar.getNmModelo());
            txtTanque.setText(motoEditar.getTanque());
            txtAno.setText(motoEditar.getAnoFabricacao());
            txtPlaca.setText(motoEditar.getPlaca());
            txtObs.setText(motoEditar.getObs());

            barCilindradas.setProgress(motoEditar.getCilindradasMoto());
            barCilindradas.incrementProgressBy(step);
            barCilindradas.setMax(MAXIMO_CILINDRADAS);
        }
        mVeiculoRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario()).child(Constants.NODE_MOTO);

        barCilindradas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int)Math.round(progress/step))*step;
                //progress = progress + step;
                seekBar.setProgress(progress);
                cilindradasCorrigido = progress + MINIMO_CILINDRADAS;
                lblCilindradas.setText(cilindradasPlaceHolder + cilindradasCorrigido);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnCadVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nmMoto = txtNomeVeiculo.getText().toString();
                String nmModelo = txtModelo.getText().toString();
                String tanque = txtTanque.getText().toString();
                String ano = txtAno.getText().toString();
                String placa = txtPlaca.getText().toString();
                String obs = txtObs.getText().toString();
                if (!validate(nmMoto, nmModelo, tanque, ano)) {
                    Toast.makeText(getBaseContext(), "Por favor, preencha os campos corretamente", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Map<String, Object> mappedMoto = new HashMap();
                    mappedMoto.put("marca", marcaEscolhida.getNmMarca());
                    mappedMoto.put("nome", nmMoto);
                    mappedMoto.put("cilindradas", cilindradasCorrigido);
                    mappedMoto.put("tanque", tanque);
                    mappedMoto.put("ano", ano);
                    mappedMoto.put("placa", placa);
                    mappedMoto.put("obs", obs.length() > 100 ? obs.substring(0, 99) : obs);
                    try{
                        mVeiculoRef.setValue(mappedMoto);
                        Intent intentMotoAdd = new Intent(getApplicationContext(), InfoInicialActivity.class);
                        intentMotoAdd.putExtra(Constants.EXTRA_MOTO_ADICIONADA, 1);
                        intentMotoAdd.putExtra(Constants.EXTRA_USUARIO_LOGADO, usuario);
                        startActivity(intentMotoAdd);
                    } catch(Exception e) {
                        Log.e("ERR_UPDATE_MOTO", "Erro ao atualizar a moto: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Erro ao atualizar a moto\nPor favor, tente novamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mVeiculoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*if(dataSnapshot.getValue() != null) {
                    Map map = (Map) dataSnapshot.getValue();
                    txtNomeVeiculo.setText(String.valueOf(map.get("nome")));
                    //txtModelo.setText(String.valueOf(map.get("marca")));
                    txtTanque.setText(String.valueOf(map.get("tanque")));
                    txtAno.setText(String.valueOf(map.get("ano")));
                    txtPlaca.setText(String.valueOf(map.get("placa")));
                    txtObs.setText( String.valueOf(map.get("obs")));
                    barCilindradas.setProgress(new Integer(String.valueOf(map.get("cilindradas"))));
                }*/
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERR_DATASNAPSHOT_MOTO", "Erro ao recuperar dados da moto: " + databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Erro ao recuperar dados da moto", Toast.LENGTH_SHORT);
            }
        });
    }
    private void initComponents() {
        this.txtNomeVeiculo = (EditText) findViewById(R.id.txtNomeVeiculo);
        this.spMarcaVeiculo = (Spinner) findViewById(R.id.spMarcaVeiculo);
        this.txtModelo = (EditText) findViewById(R.id.txtModelo);
        this.txtPlaca = (EditText) findViewById(R.id.txtPlaca);
        this.txtAno = (EditText) findViewById(R.id.txtAno);
        this.lblCilindradas = (TextView) findViewById(R.id.lblCilindradas);
        this.barCilindradas = (SeekBar) findViewById(R.id.barCilindradas);
        this.txtTanque = (EditText) findViewById(R.id.txtTanque);
        this.txtObs = (EditText) findViewById(R.id.txtObs);
        this.btnCadVeiculo = (AppCompatButton) findViewById(R.id.btnCadVeiculo);
        this.cilindradasPlaceHolder = this.lblCilindradas.getText().toString();
    }

    private void populateSpinner() {
        MarcaDAO dao = new MarcaDAO(this);
        this.mapMarcaDTO = dao.getListMarcas();
        List<String> listMarcas = new ArrayList<>(mapMarcaDTO.values());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMarcas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spMarcaVeiculo.setPrompt("Marca da Moto");
        this.spMarcaVeiculo.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String escolha = (String) parent.getItemAtPosition(position);
        MarcaDTO marca = new MarcaDTO();
        marca.setNmMarca(escolha);
        for (Long o : this.mapMarcaDTO.keySet()) {
            if (this.mapMarcaDTO.get(o).equals(escolha)) {
                marca.setIdMarca(o);
            }
            this.marcaEscolhida = marca;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public boolean validate(String nmMoto, String nmModelo, String tanque, String ano) {
        boolean isValid = true;
        if (ValidationUtils.getInstance().isNullOrEmpty(nmMoto)) {
            txtNomeVeiculo.setError("Nome da moto obrigatório");
            isValid = false;
        }
        if (ValidationUtils.getInstance().isNullOrEmpty(nmModelo)) {
            txtModelo.setError("Por favor, preencha o modelo para a moto");
            isValid = false;
        }
        if (ValidationUtils.getInstance().isNullOrEmpty(tanque)) {
            txtTanque.setError("Por favor, preencha o tamanho do tanque da moto");
            isValid = false;
        }
        if (ValidationUtils.getInstance().isNullOrEmpty(ano)) {
            txtAno.setError("Por favor, preencha o ano de fabricação da moto");
            isValid = false;
        }
        if (marcaEscolhida.getIdMarca() == 0 || marcaEscolhida.getNmMarca().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, selecione a marca da moto", Toast.LENGTH_SHORT).show();
            spMarcaVeiculo.requestFocus();
            isValid = false;
        }
        return isValid;
    }

}
