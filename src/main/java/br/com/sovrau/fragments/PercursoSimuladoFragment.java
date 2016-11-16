package br.com.sovrau.fragments;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import br.com.sovrau.R;
import br.com.sovrau.adapters.GooglePlacesAutocompleteAdapter;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.utilities.CodeUtils;
import br.com.sovrau.utilities.ValidationUtils;

/**
 * Created by Lucas on 11/09/2016.
 */
public class PercursoSimuladoFragment extends Fragment {
    private RadioGroup rdTipoPercurso;
    private RadioButton tipoPercurso;
    private AutoCompleteTextView txtInicioPercurso;
    private AutoCompleteTextView txtFinalPercurso;
    private EditText txtPeriodoDias;
    private EditText txtOdometroInicial;
    private Spinner spMotosPercurso;
    private AppCompatButton btnIniciaPercurso;
    private List<MotoDTO> listMotos = new ArrayList<>();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mMotoRef;
    private MotoDTO motoEscolhida = new MotoDTO();
    private UsuarioDTO usuario;
    private static final String TAG = PercursoSimuladoFragment.class.getSimpleName();
    private static final String DIRECTIONS_URL_BASE = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    private static final String DISTANCE_MATRIX_KEY = "AIzaSyAgNkFq-etPp7OZ3GqNqwfU5nb_08EgdAo";
    private GooglePlacesAutocompleteAdapter mAdapter;
    private static final int REQUEST_CODE = 101;
    private String apiResponse;

    public static PercursoSimuladoFragment newInstance() {
        return new PercursoSimuladoFragment();
    }

    public PercursoSimuladoFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.percurso_manual_fragment, container, false);
        initComponents(view);
        Intent intent = getActivity().getIntent();
        usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        populateSpinner();
        spMotosPercurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String escolha = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < listMotos.size(); i++){
                     if(listMotos.get(i).getNmMoto().equals(escolha)){
                       motoEscolhida = listMotos.get(i);
                       txtOdometroInicial.setText(String.valueOf(motoEscolhida.getOdometro()));
                   }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnIniciaPercurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarPercurso(view);
            }
        });
        return view;
    }

    private void initComponents(View view) {
        this.rdTipoPercurso = (RadioGroup) view.findViewById(R.id.rdTipoPercurso);

        mAdapter = new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.txtInicioPercurso = (AutoCompleteTextView) view.findViewById(R.id.txtInicio);
        txtInicioPercurso.setAdapter(mAdapter);
        txtInicioPercurso.setOnItemClickListener(mOnLocationSelected);

        this.txtFinalPercurso = (AutoCompleteTextView) view.findViewById(R.id.txtFinal);
        txtFinalPercurso.setAdapter(mAdapter);
        txtFinalPercurso.setOnItemClickListener(mOnLocationSelected);

        this.txtPeriodoDias = (EditText) view.findViewById(R.id.txtPeriodoDias);

        txtPeriodoDias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = NumberPickerFragment.newInstance();
                newFragment.setTargetFragment(PercursoSimuladoFragment.this, REQUEST_CODE);
                newFragment.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        this.txtOdometroInicial = (EditText) view.findViewById(R.id.txtOdometroInicial);
        this.btnIniciaPercurso = (AppCompatButton) view.findViewById(R.id.btnIniciar);
        this.spMotosPercurso = (Spinner) view.findViewById(R.id.spListaMotosPercurso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            String periodo = data.getSerializableExtra(Constants.EXTRA_PERIODO).toString();
            txtPeriodoDias.setText(periodo);
        }
    }

    private boolean validate(String txtInicioPercurso, String txtFinalPercurso, String txtOdometroInicial, String motoEscolhida) {
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
        if (ValidationUtils.getInstance().isNullOrEmpty(motoEscolhida)){
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, informe para qual moto é o percurso", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private void gerarPercurso(View view) {
        String txtInicioPercurso = this.txtInicioPercurso.getText().toString();
        String txtFinalPercurso = this.txtFinalPercurso.getText().toString();
        String txtOdometroInicial = this.txtOdometroInicial.getText().toString();
        int tipoPercurso = this.rdTipoPercurso.getCheckedRadioButtonId();
        this.tipoPercurso = (RadioButton) view.findViewById(tipoPercurso);

        if (!validate(txtInicioPercurso, txtFinalPercurso, txtOdometroInicial, motoEscolhida.getIdMoto())) {
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, preencha os campos corretamente", Toast.LENGTH_LONG).show();
            return;
        } else {
            try {
                JSONObject jsonObjectDistance = getDirectionsJSON(txtInicioPercurso, txtFinalPercurso);
                double distancia = getDistanceByJSON(jsonObjectDistance) * Double.parseDouble(txtPeriodoDias.getText().toString().trim());
                Log.i(TAG, "Distancia Total: " + distancia);
                Snackbar.make(view, "Diatancia Total: " + distancia + " Km", Snackbar.LENGTH_LONG).show();
                calcularConsumo(distancia);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
    private void calcularConsumo(double distancia) {
        Log.i(TAG, "Entrando no calculo de consumo " + distancia );
        try {
            InputStream inputStream = getResources().getAssets().open("consumo.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String[] headers = reader.readLine().split(";");
            int positionPercurso = getTipoPercurso(headers);
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(";");
                if(Long.valueOf(cols[1].trim()) <= motoEscolhida.getCilindradasMoto()
                        && motoEscolhida.getCilindradasMoto() <= Long.valueOf(cols[2].trim())){
                        double rodagem = Double.parseDouble(cols[positionPercurso].trim());
                        Log.i(TAG, "cilindradas : " + motoEscolhida.getCilindradasMoto());
                        Log.i(TAG, "Kms por litro: " + rodagem);
                        double gastoPorKm = distancia/rodagem;
                    Snackbar.make(getView(), "Você gastou: " + String.format( "%.2f", gastoPorKm ) + " litros", Snackbar.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Erro ao localoizar arquivo CSV", Toast.LENGTH_SHORT).show();
        }
    }
    private int getTipoPercurso(String[] values) {
        String tipoPercurso = this.tipoPercurso.getText().toString();
        if(values[3].trim().equalsIgnoreCase(tipoPercurso)) {
            return 3;
        } else if (values[4].trim().equalsIgnoreCase(tipoPercurso)) {
            return 4;
        } return 5;
    }

    private void populateSpinner() {
        mMotoRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario());
        mMotoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Map<String, Object> mapMotos = (Map<String, Object>) postSnapshot.getValue();
                    listMotos.addAll(CodeUtils.getInstance().parseMapToListMoto(mapMotos));
                    }
                    List<String> motoID = new ArrayList<>();
                    for (MotoDTO motoDTO : listMotos) {
                        motoID.add(motoDTO.getNmMoto());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, motoID);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMotosPercurso.setAdapter(dataAdapter);
                    if(listMotos.isEmpty()) {
                        Toast.makeText(getContext(), "É preciso adicionar ao menos uma moto para iniciar um percurso", Toast.LENGTH_SHORT).show();
                    } else if (listMotos.size() == 1) {
                        spMotosPercurso.setSelection(dataAdapter.getPosition(listMotos.get(0).getNmMoto()));
                        spMotosPercurso.setEnabled(false);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                   Log.e(TAG, "Erro ao recuperar lista de motos: " + databaseError.getMessage());
                 }
            }
        );
    }
    private AdapterView.OnItemClickListener mOnLocationSelected = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String str = (String) parent.getItemAtPosition(position);
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Item selecionado: " + str);
        }
    };

    private JSONObject getDirectionsJSON(String origem, String destino) throws JSONException {
        FetchDistanceAsyncTask task = new FetchDistanceAsyncTask();
        try {
            apiResponse = task.execute(new String[]{origem, destino}).get();
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        } catch (ExecutionException e) {
            Log.e(TAG, e.getMessage());
        }
        return new JSONObject(apiResponse);
    }
    private double getDistanceByJSON(JSONObject jsonObjectDistance) throws JSONException {
        JSONArray array = jsonObjectDistance.getJSONArray("rows");
        JSONObject routes = array.getJSONObject(0);
        JSONArray legs = routes.getJSONArray("elements");
        JSONObject steps = legs.getJSONObject(0);
        JSONObject distance = steps.getJSONObject("distance");

        Log.i("Distancia: ", distance.toString());
        return Double.parseDouble(distance.getString("text").replace(".", "").replace(",",".").replaceAll("[^\\.0123456789]",""));
    }
    class FetchDistanceAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder buildUrl = new StringBuilder();
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                buildUrl.append(DIRECTIONS_URL_BASE)
                        .append("origins=" + URLEncoder.encode(params[0], "utf8"))
                        .append("&destinations=" + URLEncoder.encode(params[1], "utf8"))
                        .append("&language=pt-BR")
                        .append("&key=" + DISTANCE_MATRIX_KEY);
                Log.i(TAG, "URL Distance Request: " + buildUrl.toString());

                URL url = new URL(buildUrl.toString());
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, "URL Malformada: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "Erro ao abrir conexão: " + e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return jsonResults.toString();
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            apiResponse = jsonObject;
        }
    }
}
