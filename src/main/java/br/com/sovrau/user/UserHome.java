package br.com.sovrau.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.fragments.AgendarRevisaoFragment;
import br.com.sovrau.fragments.IniciaPercursoFragment;
import br.com.sovrau.fragments.ListaAlertaFragment;
import br.com.sovrau.fragments.ListaPercursoFragment;
import br.com.sovrau.fragments.ListaVeiculosFragment;
import br.com.sovrau.fragments.PercursoSimuladoFragment;
import br.com.sovrau.fragments.RevisaoManualFragment;

/**
 * Created by Lucas on 28/04/2016.
 */
public class UserHome extends AppCompatActivity {
    private final static String TAG = UserHome.class.getSimpleName();

    //Hamburger Menu
    ListView mDrawerList;
    RelativeLayout mDrawerPane;

    private TextView lblUserName;
    private UsuarioDTO usuario;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_activity);

        lblUserName = (TextView) findViewById(R.id.userName);
        Intent intent = getIntent();
        usuario = (UsuarioDTO) intent.getSerializableExtra(Constants.EXTRA_USUARIO_LOGADO);
        String localName = usuario.getNome().contains(" ") ? usuario.getNome().split(" ")[0] : usuario.getNome();

        lblUserName.setText(localName);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_content, ListaVeiculosFragment.newInstance(), "listaVeiculos").commit();
        }
        populateDrawerList();
    }
    private void populateDrawerList(){
        mNavItems.add(new NavItem("Listar Percursos", R.drawable.ic_percurso));
        mNavItems.add(new NavItem("Revisão Feita", R.drawable.ic_revision));
        mNavItems.add(new NavItem("Agendar Revisão", R.drawable.ic_calendar));
        mNavItems.add(new NavItem("Alertas", R.drawable.ic_alerta));
        mNavItems.add(new NavItem("Iniciar Percurso", R.drawable.ic_percurso));
        mNavItems.add(new NavItem("Percurso Simulado", R.drawable.ic_percurso_manual));
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
        Fragment fragment = null;
        Class fragmentClass;
        Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRA_USUARIO_LOGADO, usuario);
        switch (position) {
            case 0: //Listar Percursos
                fragmentClass = ListaPercursoFragment.class;
                break;
            case 1: //Revisão Feita
                fragmentClass = RevisaoManualFragment.class;
                break;
            case 2: //Agendar Revisão
                fragmentClass = AgendarRevisaoFragment.class;
                break;
            case 3: //Alertas
                fragmentClass = ListaAlertaFragment.class;
                break;
            case 4: //Iniciar Percurso
                fragmentClass = IniciaPercursoFragment.class;
                break;
            case 5: //Percurso Manual
                fragmentClass = PercursoSimuladoFragment.class;
                break;
            case 6: //Veiculos
                fragmentClass = ListaVeiculosFragment.class;
                break;
            case 7: //Sair
                fragmentClass = RevisaoManualFragment.class;
                break;
            default:
                fragmentClass = ListaVeiculosFragment.class;
                break;
        }
            try{
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e){
                Log.e(TAG, e.getMessage());
            }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, fragment).addToBackStack(null).commit();

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

