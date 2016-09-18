package br.com.sovrau.user;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import br.com.sovrau.fragments.IniciaPercursoFragment;
import br.com.sovrau.fragments.ListaVeiculosFragment;
import br.com.sovrau.fragments.RevisaoManualFragment;

/**
 * Created by Lucas on 28/04/2016.
 */
public class UserHome extends AppCompatActivity {
    private final static String TAG = UserHome.class.getSimpleName();

    //Hamburger Menu
    ListView mDrawerList;
    RelativeLayout mDrawerPane;

    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.root_layout, ListaVeiculosFragment.newInstance(), "listaVeiculos").commit();
        }
        populateDrawerList();
        //getActionBar().setDisplayHomeAsUpEnabled(true);
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
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (position) {
            case 0: //Listar Percursos
                fragmentClass = RevisaoManualFragment.class;
                break;
            case 1: //Revisão
                fragmentClass = RevisaoManualFragment.class;
                break;
            case 2: //Alertas
                fragmentClass = RevisaoManualFragment.class;
                break;
            case 3: //Iniciar Percurso
                fragmentClass = IniciaPercursoFragment.class;
                break;
            case 4: //Veiculos
                fragmentClass = ListaVeiculosFragment.class;
                //startActivity(new Intent(this, UserHome.class).putExtra(Constants.EXTRA_USUARIO_LOGADO, this.usuario));
                break;
            case 5: //Sair
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
        android.app.FragmentManager fragmentManager = getFragmentManager();
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

