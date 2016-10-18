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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import br.com.sovrau.providers.DatabaseHelper;
import br.com.sovrau.veiculo.VeiculoActivity;

/**
 * Created by Lucas on 13/09/2016.
 */
public class ListaVeiculosFragment extends Fragment implements AdapterView.OnItemClickListener {
    private final static String TAG = ListaVeiculosFragment.class.getSimpleName();

    private UsuarioDTO usuario;
    private TextView lblBoasVindas;
    private ListView listMoto;
    private FloatingActionButton fab;
    private DatabaseHelper helper;
    private long motoSelecionada;
    private String motoUrl;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mChildRef;
    private List<Map<String, Object>> motos = new ArrayList<>();
    private View rootView = null;

    public static ListaVeiculosFragment newInstance(){
        return new ListaVeiculosFragment();
    }
    public ListaVeiculosFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       rootView = inflater.inflate(R.layout.lista_veiculos_fragment, container, false);
       this.fab = (FloatingActionButton) rootView.findViewById(R.id.btnFabVeiculos);
       this.lblBoasVindas = (TextView) rootView.findViewById(R.id.lblBoasVindas);
       this.listMoto = (ListView) rootView.findViewById(android.R.id.list);

        Intent intent = getActivity().getIntent();
        if(intent.hasExtra(Constants.EXTRA_USUARIO_LOGADO)){
            usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
            mChildRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario());
            String localName = usuario.getNome().contains(" ") ? usuario.getNome().split(" ")[0] : usuario.getNome();
            lblBoasVindas.setText(lblBoasVindas.getText().toString().concat(localName));
        }

        //listMoto = getListView();
       String[] de = {"nome", "marca", "modelo", "ano"};
       int[] para = {R.id.customNmMoto, R.id.custonNmMarca, R.id.customNmModelo, R.id.customAnoFab};
       SimpleAdapter listAdapter = new SimpleAdapter(getActivity(), listarVeiculos(), R.layout.custom_list_motos, de, para);
       listAdapter.notifyDataSetChanged();
       listMoto.setAdapter(listAdapter);
       listMoto.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
       //setListAdapter(new SimpleAdapter(getContext(), listarVeiculos(), R.layout.custom_list_motos, de, para));

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intentCadMoto = new Intent(getActivity().getApplicationContext(), VeiculoActivity.class);
               intentCadMoto.putExtra(Constants.EXTRA_USUARIO_LOGADO, usuario);
               startActivity(intentCadMoto);
           }
       });
       return rootView;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = motos.get(position);
        String idMoto = (String) map.get("id");
        String nmMoto = (String) map.get("nome");
        String nmMarca = (String) map.get("marca");
        String nmModelo = (String) map.get("modelo");
        int anoFab = (int) map.get("ano");

        String mensagem = "Moto selecionada: "+ idMoto + " - " + nmMoto;
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
        //TODO:
        MotoDTO motoSelecionada = new MotoDTO();
        motoSelecionada.setIdMoto(idMoto);
        motoSelecionada.setNmMoto(nmMoto);
        motoSelecionada.setNmMarca(nmMarca);
        motoSelecionada.setNmModelo(nmModelo);
        motoSelecionada.setAnoFabricacao(anoFab);

        startActivity(new Intent(getContext(), VeiculoActivity.class));
    }
    private List<Map<String, Object>> listarVeiculos(){
        mChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "DataSnap Size " + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Map<String, Object> mapMotos = (Map<String, Object>) postSnapshot.getValue();
                    Log.i(TAG, "Data: " + postSnapshot.getValue());
                    motos.add(mapMotos);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Erro: " + databaseError.getCode() + " " + databaseError.getMessage());
            }
        });
        return this.motos;
    }
}
