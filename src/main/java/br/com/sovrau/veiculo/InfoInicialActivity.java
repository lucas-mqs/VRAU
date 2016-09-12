package br.com.sovrau.veiculo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.user.UserHome;
import br.com.sovrau.utilities.CodeUtils;
import br.com.sovrau.utilities.ValidationUtils;

/**
 * Created by Lucas on 07/05/2016.
 */
public class InfoInicialActivity extends Activity {
    private EditText txtOdometroInicial;
    private EditText dataUltimaRevisao;
    private CheckBox chCombustivel;
    private CheckBox chOleo;
    private CheckBox chDesgastePastilhas;
    private CheckBox chDesgastePneus;
    private CheckBox chConsumoLiqArref;
    private CheckBox chDesgasteFreios;
    private CheckBox chDesgasteCxDirecao;
    private RadioGroup radioGroupLocalCelular;
    private RadioButton radioBtnLocalCelular;
    private AppCompatButton btnSaveInfos;
    private String idMotoAdd;
    private long valorOdometro;
    private String strDataRevisao;
    private static final int DATE_DIALOG_ID = 0;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mChildRef;

    private UsuarioDTO usuario = new UsuarioDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infos_iniciais_activity);
        initComponents();

        final Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_MOTO_ADICIONADA)) {
            idMotoAdd = intent.getStringExtra(Constants.EXTRA_MOTO_ADICIONADA);
        }
        if (intent.hasExtra(Constants.EXTRA_USUARIO_LOGADO)) {
            usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        }
        mChildRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario()).child(Constants.NODE_MOTO).child(idMotoAdd);

        dataUltimaRevisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dataUltimaRevisao)
                    showDialog(DATE_DIALOG_ID);
            }
        });
        btnSaveInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    insertInfoInicial();
                    Intent intentInfos = new Intent(getApplicationContext(), UserHome.class);
                    intentInfos.putExtra(Constants.EXTRA_USUARIO_LOGADO, intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO));
                    startActivity(intentInfos);

                } catch (ParseException pe) {
                    Log.e("INSERT_INFO_INICIAL", pe.getMessage());
                    Toast.makeText(getApplicationContext(), "Preencha a data no formato DD/MM/YYYY", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initComponents() {
        this.txtOdometroInicial = (EditText) findViewById(R.id.txtOdometro);
        this.dataUltimaRevisao = (EditText) findViewById(R.id.dataUltimaRevisao);
        this.chCombustivel = (CheckBox) findViewById(R.id.chCombustivel);
        this.chOleo = (CheckBox) findViewById(R.id.chOleo);
        this.chDesgastePastilhas = (CheckBox) findViewById(R.id.chDesgastePastilhas);
        this.chDesgastePneus = (CheckBox) findViewById(R.id.chDesgastePneus);
        this.chConsumoLiqArref = (CheckBox) findViewById(R.id.chConsumoLiqArref);
        this.chDesgasteFreios = (CheckBox) findViewById(R.id.chDesgasteFreios);
        this.chDesgasteCxDirecao = (CheckBox) findViewById(R.id.chDesgasteCxDirecao);
        this.radioGroupLocalCelular = (RadioGroup) findViewById(R.id.radioLocalCelular);
        this.btnSaveInfos = (AppCompatButton) findViewById(R.id.btnSaveInfos);
    }

    public void insertInfoInicial() throws ParseException {
        String odometro = this.txtOdometroInicial.getText().toString();
        String dataRevisao = this.strDataRevisao;

        boolean isMonitorarCombustivel = this.chCombustivel.isChecked();
        boolean isMonitorarOleo = this.chOleo.isChecked();
        boolean isMonitorarPastilhas = this.chDesgastePastilhas.isChecked();
        boolean isMonitorarPneus = this.chDesgastePneus.isChecked();
        boolean isMonitorarLiquido = this.chConsumoLiqArref.isChecked();
        boolean isMonitorarFreios = this.chDesgasteFreios.isChecked();
        boolean isMonitorarCxDirecao = this.chDesgasteCxDirecao.isChecked();
        int localCelular = this.radioGroupLocalCelular.getCheckedRadioButtonId();
        this.radioBtnLocalCelular = (RadioButton) findViewById(localCelular);

        if (!validate(odometro, dataRevisao, isMonitorarCombustivel, isMonitorarOleo, isMonitorarPastilhas, isMonitorarPneus, isMonitorarLiquido, isMonitorarFreios, isMonitorarCxDirecao)) {
            Toast.makeText(getBaseContext(), "Por favor, preencha os campos corretamente", Toast.LENGTH_LONG).show();
            return;
        } else {
            Map<String, Object> mappedInfos = new HashMap<>();
            mappedInfos.put("odometro", odometro);
            mappedInfos.put("dataRevisao", dataRevisao);
            mappedInfos.put("isMonitorarCombustivel", isMonitorarCombustivel);
            mappedInfos.put("isMonitorarOleo", isMonitorarOleo);
            mappedInfos.put("isMonitorarPastilhas", isMonitorarPastilhas);
            mappedInfos.put("isMonitorarPneus", isMonitorarPneus);
            mappedInfos.put("isMonitorarLiquido", isMonitorarLiquido);
            mappedInfos.put("isMonitorarFreios", isMonitorarFreios);
            mappedInfos.put("isMonitorarCxDirecao", isMonitorarCxDirecao);
            mappedInfos.put("localCelular", localCelular);
            mChildRef.updateChildren(mappedInfos);

        }
    }

    public boolean validate(String odometro, String dataRevisao, boolean isMonitorarCombustivel, boolean isMonitorarOleo, boolean isMonitorarPastilhas, boolean isMonitorarPneus,
                            boolean isMonitorarLiquido, boolean isMonitorarFreios, boolean isMonitorarCxDirecao) {
        boolean isValid = true;
        if (!isMonitorarCombustivel && !isMonitorarOleo && !isMonitorarPastilhas && !isMonitorarPneus &&
                !isMonitorarLiquido && !isMonitorarFreios && !isMonitorarCxDirecao) {
            Toast.makeText(this, "Selecione ao menos um item para monitorar", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (ValidationUtils.getInstance().isNullOrEmpty(odometro)) {
            Toast.makeText(this, "Valor do Odometro Inválido", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {
            valorOdometro = Long.valueOf(odometro);
        }
        if (ValidationUtils.getInstance().isNullOrEmpty(dataRevisao)) {
            //Caso o usuário não tenha feito nenhuma revisão na moto
            //Consideramnos a data do dia atual
            strDataRevisao = CodeUtils.getInstance().formatDateToString(new Date());
        }
        return isValid;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, ano, mes,
                        dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            strDataRevisao = String.valueOf(dayOfMonth <= 9 ? "0"+ dayOfMonth : dayOfMonth) + "/" +
                             String.valueOf((monthOfYear + 1) <= 9 ? "0"+ monthOfYear : monthOfYear ) + "/" +
                             String.valueOf(year);
            dataUltimaRevisao.setText(strDataRevisao);
        }
    };
}
