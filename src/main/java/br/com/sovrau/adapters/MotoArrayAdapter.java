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
import br.com.sovrau.dto.MotoDTO;

public class MotoArrayAdapter extends ArrayAdapter<MotoDTO> {
    private Activity activity;
    private List<MotoDTO> lMotos;
    private static LayoutInflater inflater = null;

    public MotoArrayAdapter(Activity activity, int textViewResourceId, List<MotoDTO> lMotos) {
        super(activity, textViewResourceId, lMotos);
        try{
            this.activity = activity;
            this.lMotos = lMotos;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            Log.e("MotoArrayAdapter", "Erro ao inicializar adapter: " + e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return lMotos.size();
    }

    @Nullable
    public MotoDTO getItem(MotoDTO position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class ViewHolder {
        public TextView customNmMoto;
        public TextView customNmMarca;
        public TextView customNmModelo;
        public TextView customAnoFab;
        public TextView customOdometro;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try{
            if(convertView ==  null) {
                vi = inflater.inflate(R.layout.custom_list_motos, null);
                holder = new ViewHolder();

                holder.customNmMoto = (TextView) vi.findViewById(R.id.customNmMoto);
                holder.customNmMarca = (TextView) vi.findViewById(R.id.custonNmMarca);
                holder.customNmModelo = (TextView) vi.findViewById(R.id.customNmModelo);
                holder.customAnoFab = (TextView) vi.findViewById(R.id.customAnoFab);
                holder.customOdometro = (TextView) vi.findViewById(R.id.customOdometroMoto);

                vi.setTag(holder);
            } else{
                holder = (ViewHolder) vi.getTag();
            }
            holder.customNmMoto.setText(lMotos.get(position).getNmMoto());
            holder.customNmMarca.setText(lMotos.get(position).getNmMarca());
            holder.customNmModelo.setText(lMotos.get(position).getNmModelo());
            holder.customAnoFab.setText(Integer.toString(lMotos.get(position).getAnoFabricacao()));
            holder.customOdometro.setText(String.valueOf(lMotos.get(position).getOdometro()) + " Km");

        } catch (Exception e) {
            Log.e("MotoArrayAdapter", e.getMessage());
        }
        return vi;
    }
}
