package br.com.sovrau.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.sovrau.R;
import br.com.sovrau.adapters.AlertaArrayAdapter;
import br.com.sovrau.alerta.ConfigAlertaActivity;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.AlertaDTO;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.utilities.CodeUtils;

/**
 * Created by Lucas on 18/09/2016.
 */
public class ListaAlertaFragment extends Fragment implements AdapterView.OnItemClickListener {
    private final static String TAG = ListaAlertaFragment.class.getSimpleName();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mChildRef;
    private UsuarioDTO usuario;
    private FloatingActionButton btnFabAlertas;
    private ListView listAlertas;
    private List<AlertaDTO> alertas  = new ArrayList<>();

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
        mChildRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario()).child(Constants.NODE_ALERTA);

        mChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> mapAlertas = (Map<String, Object>) postSnapshot.getValue();
                    Log.i(TAG, "Data: " + postSnapshot.getValue());
                    alertas.addAll(CodeUtils.getInstance().parseMapToListAlerta(mapAlertas));
                }
                AlertaArrayAdapter alertaArrayAdapter = new AlertaArrayAdapter(getActivity(), R.layout.custom_list_alert, alertas);
                listAlertas.setAdapter(alertaArrayAdapter);
                listAlertas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Erro: " + databaseError.getCode() + " " + databaseError.getMessage());
            }
        });

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
        AlertaDTO alertaDTO = alertas.get(position);
        String percentualAtual = String.valueOf(alertaDTO.getPorcentagemTotal());
        String indicador = String.valueOf(alertaDTO.getPorcentagemAlerta());
        String avisoTroca = String.valueOf(alertaDTO.getQtdeKmFalta());
    }
}
