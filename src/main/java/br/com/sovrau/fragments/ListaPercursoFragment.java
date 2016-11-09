package br.com.sovrau.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import br.com.sovrau.adapters.PercursoArrayAdapter;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.PercursoDTO;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.utilities.CodeUtils;

/**
 * Created by Lucas on 18/09/2016.
 */
public class ListaPercursoFragment extends Fragment {
    private ListView listPercursos;
    private List<PercursoDTO> percursos = new ArrayList<>();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mChildRef;
    private UsuarioDTO usuario;
    private static final String TAG = ListaPercursoFragment.class.getSimpleName();

    public static ListaPercursoFragment newInstance() {
        Bundle args = new Bundle();
        ListaPercursoFragment fragment = new ListaPercursoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ListaPercursoFragment() {}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_percurso_activity, container, false);
        this.listPercursos = (ListView) view.findViewById(android.R.id.list);

        Intent intent = getActivity().getIntent();
        usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);

        mChildRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario()).child(Constants.NODE_PERCURSO); //.child(motoDTO.getIdMoto()).child(Constants.NODE_PERCURSO);

        mChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Map<String, Object> mapPercursos = (Map<String, Object>) postSnapshot.getValue();
                    percursos.addAll(CodeUtils.getInstance().parseMapToListPercurso(mapPercursos));
                }
                PercursoArrayAdapter percursoArrayAdapter = new PercursoArrayAdapter(getActivity(), R.layout.custom_list_percurso, null);
                listPercursos.setAdapter(percursoArrayAdapter);
                listPercursos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Erro: " + databaseError.getCode() + " " + databaseError.getMessage());
            }
        });
        return view;
    }
}
