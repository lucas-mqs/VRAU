package br.com.sovrau.user;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import br.com.sovrau.fragments.IniciaPercursoFragment;
import br.com.sovrau.fragments.PercursoManualFragment;
import br.com.sovrau.fragments.RevisaoManualFragment;
import br.com.sovrau.percurso.IniciaPercursoActivity;
import br.com.sovrau.providers.DatabaseHelper;
import br.com.sovrau.veiculo.VeiculoActivity;

/**
 * Created by Lucas on 28/04/2016.
 */
public class UserHome extends ListActivity implements AdapterView.OnItemClickListener, View.OnClickListener{
    private final static String TAG = UserHome.class.getSimpleName();

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
    //Hamburger Menu
    ListView mDrawerList;
    RelativeLayout mDrawerPane;

    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_activity);

        TextView userNameMenu = (TextView) findViewById(R.id.userName);

        ListView listView = getListView();

        this.fab = (FloatingActionButton) findViewById(R.id.btnFabVeiculos);
        helper = new DatabaseHelper(this);

        populateDrawerList();
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView lblBoasVindas = (TextView) findViewById(R.id.lblBoasVindas);
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.EXTRA_USUARIO_LOGADO)){
            usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
            mChildRef = mRootRef.child(Constants.NODE_DATABASE).child(usuario.getIdUSuario());
            String localName = usuario.getNome().contains(" ") ? usuario.getNome().split(" ")[0] : usuario.getNome();

            lblBoasVindas.setText(lblBoasVindas.getText().toString().concat(localName));
            userNameMenu.setText(localName);
        }
        String[] de = {"nmMoto", "nmMarca", "nmModelo", "anoFab"};
        int[] para = {R.id.customNmMoto, R.id.custonNmMarca, R.id.customNmModelo, R.id.customAnoFab};
        setListAdapter(new SimpleAdapter(this, listarVeiculos(usuario.getIdUSuario()), R.layout.custom_list_motos, de, para));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCadMoto = new Intent(getApplicationContext(), VeiculoActivity.class);
                intentCadMoto.putExtra(Constants.EXTRA_USUARIO_LOGADO, usuario);
                startActivity(intentCadMoto);
            }
        });
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
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        //TODO:
        MotoDTO motoSelecionada = new MotoDTO();
        motoSelecionada.setIdMoto(idMoto);
        motoSelecionada.setNmMoto(nmMoto);
        motoSelecionada.setNmMarca(nmMarca);
        motoSelecionada.setNmModelo(nmModelo);
        motoSelecionada.setAnoFabricacao(anoFab);

        startActivity(new Intent(this, VeiculoActivity.class));
    }
    private List<Map<String, Object>> listarVeiculos(String idUsuario){
        mChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count " ,"" + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Map<String, Object> mapMotos = new HashMap<>();
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
        /*
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select mt._id, mt.nmMoto, ma.nmMarca, mt.nmModelo, mt.anoFabricacao\n" +
                "from Moto mt\n" +
                "join Marca ma on (ma._id = mt.idMarca)\n" +
                "where mt.idUsuario = ?", new String[]{""+idUsuario});
        cursor.moveToFirst();
        for(int i=0; i < cursor.getCount(); i++){
            Map<String, Object> mapMotos = new HashMap<>();
            mapMotos.put("idMoto", cursor.getLong(0));
            mapMotos.put("nmMoto", cursor.getString(1));
            mapMotos.put("nmMarca", cursor.getString(2));
            mapMotos.put("nmModelo", cursor.getString(3));
            mapMotos.put("anoFab", cursor.getInt(4));
            this.motos.add(mapMotos);
            cursor.moveToNext();
        }
        if(cursor != null)
            cursor.close();
        */
        return this.motos;
    }

    @Override
    public void onClick(View v) {

    }
    private void populateDrawerList(){
        mNavItems.add(new NavItem("Listar Percursos", R.drawable.ic_percurso));
        mNavItems.add(new NavItem("Revisão", R.drawable.ic_revision));
        mNavItems.add(new NavItem("Alertas", R.drawable.ic_alerta));
        mNavItems.add(new NavItem("Iniciar Percurso", R.drawable.ic_percurso_manual));
        mNavItems.add(new NavItem("Veículos", R.drawable.ic_veiculos));
        mNavItems.add(new NavItem("Sair", R.drawable.ic_sair));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

    }
    private void selectItemFromDrawer(int position) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0: //Listar Percursos

            case 1: //Revisão
                fragmentManager.beginTransaction().replace(R.id.main_content, new RevisaoManualFragment()).addToBackStack(null).commit();
            case 2: //Alertas

            case 3: //Iniciar Percurso
                fragmentManager.beginTransaction().replace(R.id.main_content, new IniciaPercursoFragment()).addToBackStack(null).commit();
                //startActivity(new Intent(this, IniciaPercursoActivity.class));
            case 4: //Veiculos
                startActivity(new Intent(this, UserHome.class));
            case 5: //Sair

        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    //inner class menu lateral
    class NavItem{
        private String mTitle;
        private int mIcon;

        public NavItem(String title,  int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }
    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}

