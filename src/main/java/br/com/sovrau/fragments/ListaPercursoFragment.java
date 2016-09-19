package br.com.sovrau.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.UsuarioDTO;

/**
 * Created by Lucas on 18/09/2016.
 */
public class ListaPercursoFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private List<Map<String, Object>> percursos = new ArrayList<>();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mChildRef;
    private UsuarioDTO usuario;
    private MotoDTO motoDTO;
    private static final String TAG = ListaPercursoFragment.class.getSimpleName();

    public static ListaPercursoFragment newInstance() {

        Bundle args = new Bundle();

        ListaPercursoFragment fragment = new ListaPercursoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ListaPercursoFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_percurso_activity, container, false);

        Intent intent = getActivity().getIntent();
        usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        motoDTO = (MotoDTO) intent.getSerializableExtra(Constants.EXTRA_MOTO_ADICIONADA);
        mChildRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario()).child(Constants.NODE_MOTO).child(motoDTO.getIdMoto()).child(Constants.NODE_PERCURSO);

        String[] de = {};
        int para[] = {};
        setListAdapter(new SimpleAdapter(getContext(), listarPercurso(), R.layout.custom_list_motos, de, para));

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    private List<Map<String, Object>> listarPercurso(){
        mChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Map<String, Object> mapPercursos = (Map<String, Object>) postSnapshot.getValue();
                    percursos.add(mapPercursos);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Erro: " + databaseError.getCode() + " " + databaseError.getMessage());
            }
        });
        return this.percursos;
    }
}
