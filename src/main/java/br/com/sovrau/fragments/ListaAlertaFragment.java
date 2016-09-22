package br.com.sovrau.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
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
public class ListaAlertaFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private List<Map<String, Object>> alertas = new ArrayList<>();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mChildRef;
    private UsuarioDTO usuario;
    //private MotoDTO motoDTO;
    private FloatingActionButton btnFabAlertas;

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


        Intent intent = getActivity().getIntent();
        usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        //motoDTO = (MotoDTO) intent.getSerializableExtra(Constants.EXTRA_MOTO_ADICIONADA);
        mChildRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario()).child(Constants.NODE_MOTO); //.child(motoDTO.getIdMoto()).child(Constants.NODE_ALERTA);

        String[] de = {};
        int para[] = {};
        setListAdapter(new SimpleAdapter(getContext(), listarAlertas(), R.layout.custom_list_motos, de, para));

        btnFabAlertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ConfigAlertaActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ListView listView = getListView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    private List<Map<String, Object>> listarAlertas(){
        mChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> mapAlertas = (Map<String, Object>) postSnapshot.getValue();
                    alertas.add(mapAlertas);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return this.alertas;
    }
}
