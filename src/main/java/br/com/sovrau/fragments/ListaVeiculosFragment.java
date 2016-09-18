package br.com.sovrau.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.providers.DatabaseHelper;
import br.com.sovrau.veiculo.VeiculoActivity;

/**
 * Created by Lucas on 13/09/2016.
 */
public class ListaVeiculosFragment extends ListFragment implements AdapterView.OnItemClickListener {
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

    public static ListaVeiculosFragment newInstance(){
        return new ListaVeiculosFragment();
    }
    public ListaVeiculosFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_home_activity, container, false);

        this.fab = (FloatingActionButton) view.findViewById(R.id.btnFabVeiculos);
        this.lblBoasVindas = (TextView) view.findViewById(R.id.lblBoasVindas);

        Intent intent = getActivity().getIntent();
        if(intent.hasExtra(Constants.EXTRA_USUARIO_LOGADO)){
            usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
            mChildRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario());
            String localName = usuario.getNome().contains(" ") ? usuario.getNome().split(" ")[0] : usuario.getNome();

            lblBoasVindas.setText(lblBoasVindas.getText().toString().concat(localName));
        }
        String[] de = {"nmMoto", "nmMarca", "nmModelo", "anoFab"};
        int[] para = {R.id.customNmMoto, R.id.custonNmMarca, R.id.customNmModelo, R.id.customAnoFab};
        setListAdapter(new SimpleAdapter(getContext(), listarVeiculos(usuario.getIdUSuario()), R.layout.custom_list_motos, de, para));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCadMoto = new Intent(getActivity().getApplicationContext(), VeiculoActivity.class);
                intentCadMoto.putExtra(Constants.EXTRA_USUARIO_LOGADO, usuario);
                startActivity(intentCadMoto);
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = motos.get(position);
        Long idMoto = (Long) map.get("idMoto");
        String nmMoto = (String) map.get("nmMoto");
        String nmMarca = (String) map.get("nmMarca");
        String nmModelo = (String) map.get("nmModelo");
        int anoFab = (int) map.get("anoFab");

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
    private List<Map<String, Object>> listarVeiculos(String idUsuario){
        mChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count " ,"" + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Map<String, Object> mapMotos;
                    mapMotos = (Map<String, Object>) postSnapshot.getValue();
                    /*
                    MotoDTO moto = postSnapshot.getValue(MotoDTO.class);
                    mapMotos.put("nmMoto", moto.getNmMoto());
                    mapMotos.put("nmMarca", moto.getNmMarca());
                    mapMotos.put("nmModelo", moto.getNmModelo());
                    mapMotos.put("anoFab", String.valueOf(moto.getAnoFabricacao()));
                    */
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
