package br.com.sovrau.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.sovrau.R;
import br.com.sovrau.alerta.ConfigAlertaActivity;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.UsuarioDTO;

/**
 * Created by Lucas on 18/09/2016.
 */
public class ListaAlertaFragment extends Fragment implements AdapterView.OnItemClickListener {
    private final static String TAG = ListaAlertaFragment.class.getSimpleName();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mChildRef;
    private UsuarioDTO usuario;
    private MotoDTO motoDTO;
    private FloatingActionButton btnFabAlertas;
    private ListView listAlertas;
    private List<Map<String, Object>> alertas  = new ArrayList<>();

    public ListaAlertaFragment() {
    }

    public static ListaAlertaFragment newInstance() {
        Bundle args = new Bundle();

        ListaAlertaFragment fragment = new ListaAlertaFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_alerta_activity, container, false);
        this.btnFabAlertas = (FloatingActionButton) view.findViewById(R.id.btnFabAlertas);
        this.listAlertas = (ListView) view.findViewById(android.R.id.list);

        Intent intent = getActivity().getIntent();
        usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        //motoDTO = (MotoDTO) intent.getSerializableExtra(Constants.EXTRA_MOTO_ADICIONADA);
        mChildRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario()).child(Constants.NODE_MOTO).child(Constants.NODE_ALERTA);//.child(motoDTO.getIdMoto()).child(Constants.NODE_ALERTA);

        String[] de = {"tipoAlerta", "percentualAtual", "indicador", "avisoTroca"};
        int para[] = {R.id.txtAlertas, R.id.skPercentualAlertas, R.id.txtIndicadorPercentual, R.id.txtAvisoTroca};
        SimpleAdapter listAdapter = new SimpleAdapter(getActivity(), listarAlertas(), R.layout.custom_list_alert, de, para);
        listAdapter.notifyDataSetChanged();
        listAlertas.setAdapter(listAdapter);
        listAlertas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        btnFabAlertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ConfigAlertaActivity.class);
                intent.putExtra(Constants.EXTRA_USUARIO_LOGADO, usuario);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = alertas.get(position);
        String tipoAlerta = (String) map.get("tipoAlerta");
        String percentualAtual = (String) map.get("percentualAtual");
        String indicador = (String) map.get("indicador");
        String avisoTroca = (String) map.get("avisoTroca");
    }
    private List<Map<String, Object>> listarAlertas(){
        mChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> mapAlertas = (Map<String, Object>) postSnapshot.getValue();
                    Log.i(TAG, "Data: " + postSnapshot.getValue());
                    alertas.add(mapAlertas);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Erro: " + databaseError.getCode() + " " + databaseError.getMessage());
            }
        });
        return this.alertas;
    }
}
