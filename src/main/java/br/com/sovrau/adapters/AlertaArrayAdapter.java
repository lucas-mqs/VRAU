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
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.sovrau.R;
import br.com.sovrau.dto.AlertaDTO;

/**
 * Created by Lucas on 09/11/2016.
 */

public class AlertaArrayAdapter extends ArrayAdapter {
    private Activity activity;
    private List<AlertaDTO> lAlertas;
    private LayoutInflater inflater = null;

    public AlertaArrayAdapter(Activity activity, int resource, List lAlertas) {
        super(activity, resource, lAlertas);
        try{
            this.activity = activity;
            this.lAlertas = lAlertas;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            Log.e("AlertaArrayAdapter", "Erro ao inicializar adapter: " + e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return lAlertas.size();
    }

    @Nullable
    public AlertaDTO getItem(AlertaDTO position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class ViewHolder {
        public TextView txtIndicadorPercentual;
        public TextView txtAvisoTroca;
        public TextView txtQuilometragemRodada;
        public TextView txtTipoAlerta;
        public SeekBar skPercentualAlertas;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if(convertView == null) {
                vi = inflater.inflate(R.layout.custom_list_alert, null);
                holder = new ViewHolder();

                holder.txtIndicadorPercentual = (TextView) vi.findViewById(R.id.txtIndicadorPercentual);
                holder.txtAvisoTroca = (TextView) vi.findViewById(R.id.txtAvisoTroca);
                holder.txtQuilometragemRodada = (TextView) vi.findViewById(R.id.txtQuilometragemRodada);
                holder.txtTipoAlerta = (TextView) vi.findViewById(R.id.txtTipoAlerta);
                holder.skPercentualAlertas = (SeekBar) vi.findViewById(R.id.skPercentualAlertas);

                holder.skPercentualAlertas.setEnabled(false);
            } else {
                holder = (ViewHolder) vi.getTag();
            }
            holder.txtIndicadorPercentual.setText(new Double(lAlertas.get(position).getPorcentagemTotal()).intValue());
            holder.txtAvisoTroca.setText(String.valueOf(lAlertas.get(position).getQtdeKmFalta()));
            holder.txtQuilometragemRodada.setText(String.valueOf(lAlertas.get(position).getQtdeKmRodado()));
            holder.txtTipoAlerta.setText(lAlertas.get(position).getTipoAlerta());
            holder.skPercentualAlertas.setProgress(new Double(lAlertas.get(position).getPorcentagemTotal()).intValue());

        } catch (Exception e) {
            Log.e("AlertaArrayAdapter", e.getMessage());
        }
        return vi;
    }
}
