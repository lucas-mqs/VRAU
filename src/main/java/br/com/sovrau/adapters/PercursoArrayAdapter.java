package br.com.sovrau.adapters;

import android.app.Activity;
import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.sovrau.R;
import br.com.sovrau.dto.PercursoDTO;


/**
 * Created by Lucas.Marques on 07/11/2016.
 */
public class PercursoArrayAdapter extends ArrayAdapter<PercursoDTO> {
    private Activity activity;
    private List<PercursoDTO> lPercursos;
    private LayoutInflater inflater = null;

    public PercursoArrayAdapter(Activity activity, int resource, List<PercursoDTO> lPercursos) {
        super(activity, resource, lPercursos);
        try{
            this.activity = activity;
            this.lPercursos = lPercursos;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }catch (Exception e) {
            Log.e("PercursoArrayAdapter", "Erro ao iniciailizar adapter: " + e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return lPercursos.size();
    }

    @Nullable
    public PercursoDTO getItem(PercursoDTO position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class ViewHolder {
        public TextView customInicioPercurso;
        public TextView customFinalPercurso;
        public TextView customOdometroInicial;
        public TextView customTipoPercurso;
        public TextView customDataPercurso;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if(convertView == null) {
                vi = inflater.inflate(R.layout.custom_list_percurso, null);
                holder = new ViewHolder();

                holder.customInicioPercurso = (TextView) vi.findViewById(R.id.customInicioPercurso);
                holder.customFinalPercurso = (TextView) vi.findViewById(R.id.customFinalPercurso);
                holder.customOdometroInicial = (TextView) vi.findViewById(R.id.customOdometroInicial);
                holder.customTipoPercurso = (TextView) vi.findViewById(R.id.customTipoPercurso);
                holder.customDataPercurso = (TextView) vi.findViewById(R.id.customDataPercurso);
            } else {
                holder = (ViewHolder) vi.getTag();
            }
            holder.customInicioPercurso.setText(lPercursos.get(position).getEnderecoInicial());
            holder.customFinalPercurso.setText(lPercursos.get(position).getEnderecoFinal());
            holder.customOdometroInicial.setText(Long.toString(lPercursos.get(position).getOdometroInicial()));
            holder.customTipoPercurso.setText(lPercursos.get(position).getTipoPercurso());
        } catch (Exception e) {
            Log.e("PercursoArrayAdapter", e.getMessage());
        }
        return vi;
    }
}

