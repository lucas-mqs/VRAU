package br.com.sovrau.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import br.com.sovrau.adapters.MotoArrayAdapter;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.utilities.CodeUtils;
import br.com.sovrau.veiculo.VeiculoActivity;

public class ListaVeiculosFragment extends Fragment  {
    private final static String TAG = ListaVeiculosFragment.class.getSimpleName();

    private UsuarioDTO usuario;
    private TextView lblBoasVindas;
    private ListView listMoto;
    private FloatingActionButton fab;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mChildRef;
    private List<MotoDTO> motos = new ArrayList<>();
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
        mChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Map<String, Object> mapMotos = (Map<String, Object>) postSnapshot.getValue();
                    Log.i(TAG, "Data: " + postSnapshot.getValue());
                    motos.addAll(CodeUtils.getInstance().parseMapToListMoto(mapMotos));
                }
                MotoArrayAdapter motoArrayAdapter = new MotoArrayAdapter(getActivity(), R.layout.custom_list_motos, motos);
                listMoto.setAdapter(motoArrayAdapter);
                listMoto.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listMoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        editarMoto(parent, view, position, id);
                    }
                });
                listMoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        excluirMoto(parent, view, position, id);
                        return true;
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Erro: " + databaseError.getCode() + " " + databaseError.getMessage());
            }
        });

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
    private void editarMoto(AdapterView<?> parent, View view, int position, long id) {
        MotoDTO motoEscolhida = motos.get(position);
        String idMoto = motoEscolhida.getIdMoto();
        String nmMoto = motoEscolhida.getNmMoto();

        String mensagem = "Moto selecionada: "+ idMoto + " - " + nmMoto;
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
        Intent intentMotoEditar = new Intent(getContext(), VeiculoActivity.class);
        intentMotoEditar.putExtra(Constants.EXTRA_MOTO_EDITAR, motoEscolhida);
        intentMotoEditar.putExtra(Constants.EXTRA_USUARIO_LOGADO, usuario);
        startActivity(intentMotoEditar);
    }
    private void excluirMoto(final AdapterView<?> parent, View view, int position, long id) {
        final MotoDTO motoEscolhida = motos.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Tem certeza que deseja excluir a moto?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            mChildRef.child(Constants.NODE_MOTO).child(motoEscolhida.getIdMoto()).removeValue();
                            Log.i(TAG, "Moto " + motoEscolhida.getNmMoto() + " excluida");
                            Toast.makeText(parent.getContext(), "Moto excluída", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(TAG, "Erro ao excluir moto: " + e.getMessage());
                            Toast.makeText(parent.getContext(), "Erro ao excluir moto", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
    }
}
