package br.com.sovrau.fragments;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.internal.AutocompletePredictionEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.percurso.MonitorAvisoActivity;
import br.com.sovrau.utilities.CodeUtils;
import br.com.sovrau.utilities.ValidationUtils;

/**
 * Created by Lucas on 11/09/2016.
 */
public class IniciaPercursoFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private RadioGroup rdTipoPercurso;
    private RadioButton tipoPercurso;
    private AutoCompleteTextView txtInicioPercurso;
    private AutoCompleteTextView txtFinalPercurso;
    private EditText txtOdometroInicial;
    private EditText txtObs;
    private CheckBox isDetectarFim;
    private Spinner spMotosPercurso;
    private AppCompatButton btnIniciaPercurso;

    private LocationManager locManager;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPercursoRef;
    private UsuarioDTO usuario;
    private List<MotoDTO> listMotos = new ArrayList<>();
    private MotoDTO motoEscolhida = new MotoDTO();

    private static final String TAG = IniciaPercursoFragment.class.getSimpleName();
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBQcK9sX8AmJ2LvKAN_yiaW1ATe4KX6BbM";
    private GooglePlacesAutocompleteAdapter mAdapter;

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
        Intent intent = getActivity().getIntent();
        if(intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO) != null){
            usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        } else if (getArguments() != null){
            if(getArguments().getSerializable(Constants.EXTRA_USUARIO_LOGADO) != null)
                usuario = (UsuarioDTO) getArguments().getSerializable(Constants.EXTRA_USUARIO_LOGADO);
        }
        spMotosPercurso.setOnItemSelectedListener(this);
        populateSpinner();

        btnIniciaPercurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                handleGPS(locManager);
                insertData(v);
                startActivity(new Intent(getContext(), MonitorAvisoActivity.class));
            }
        });
        return view;
    }
    private void initComponents(View view) {
        this.rdTipoPercurso = (RadioGroup) view.findViewById(R.id.rdTipoPercurso);
        //Aqui iniciamos o auto complete de endereços
        //Fazendo o bind das views e passando o adapter e o listener
        mAdapter = new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item );

        this.txtInicioPercurso = (AutoCompleteTextView) view.findViewById(R.id.txtInicio);
        txtInicioPercurso.setAdapter(mAdapter);
        txtInicioPercurso.setOnItemClickListener(mOnLocationClicked);
        this.txtFinalPercurso = (AutoCompleteTextView) view.findViewById(R.id.txtFinal);
        txtFinalPercurso.setAdapter(mAdapter);
        txtFinalPercurso.setOnItemClickListener(mOnLocationClicked);

        this.txtOdometroInicial = (EditText) view.findViewById(R.id.txtOdometroInicial);
        this.txtObs = (EditText) view.findViewById(R.id.txtMotivo);
        this.isDetectarFim = (CheckBox) view.findViewById(R.id.chDetectFimPercurso);
        this.btnIniciaPercurso = (AppCompatButton) view.findViewById(R.id.btnIniciar);
        this.spMotosPercurso = (Spinner) view.findViewById(R.id.spListaMotosPercurso);
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
        String txtMotivo = this.txtObs.getText().toString();
        boolean isDetectarFimPercurso = this.isDetectarFim.isChecked();
        int localCelular = this.rdTipoPercurso.getCheckedRadioButtonId();
        this.tipoPercurso = (RadioButton) view.getRootView().findViewById(localCelular);

        if(!validate(txtInicioPercurso, txtFinalPercurso, txtOdometroInicial)){
            Toast.makeText(getActivity().getApplicationContext(), "Por favor, preencha os campos corretamente", Toast.LENGTH_LONG).show();
            return;
        } else{
            String percursoID = CodeUtils.getInstance().getGenericID("Percurso");
            Map<String, Object> mappedPercurso = new HashMap<>();
            mappedPercurso.put("id", percursoID);
            mappedPercurso.put("inicio", txtInicioPercurso);
            mappedPercurso.put("final", txtFinalPercurso);
            mappedPercurso.put("odometroInicial", txtOdometroInicial);
            mappedPercurso.put("motivo", txtMotivo);
            mappedPercurso.put("isMedirAuto", true);
            mappedPercurso.put("isDetectarFimPercurso", isDetectarFimPercurso);
            mappedPercurso.put("tipoPercurso", tipoPercurso.getText());
            mappedPercurso.put("moto", motoEscolhida.getIdMoto());

            mPercursoRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario()).child(Constants.NODE_MOTO).child(Constants.NODE_PERCURSO);

            try{
                mPercursoRef.child(percursoID).setValue(mappedPercurso);
            }catch (Exception e){
                Log.e("ERR_INSERT_PERCURSO", "Erro ao inserir percurso: " + e.getMessage());
                Toast.makeText(getContext(), "Erro ao inserir percurso:\nPor favor, tente novamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String escolha = (String) parent.getItemAtPosition(position);
        for (int i = 0; i < listMotos.size(); i++){
            if(this.listMotos.get(i).getNmMoto().equals(escolha)){
                this.motoEscolhida = this.listMotos.get(i);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void populateSpinner(){
        listMotos();
        List<String> motoID = new ArrayList<>();
        for (MotoDTO motoDTO: listMotos) {
            motoID.add(motoDTO.getNmMoto());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, motoID);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spMotosPercurso.setAdapter(dataAdapter);

    }
    private void listMotos(){
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
    private AdapterView.OnItemClickListener mOnLocationClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String str = (String) parent.getItemAtPosition(position);
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        }
    };

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:BR");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            Log.e(TAG, "URL do Request: " + sb.toString());
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable{
        private ArrayList<String> resultList;
        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }
        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

    }
}
