package br.com.sovrau.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lucas on 18/09/2016.
 */
public class ListaAlertaFragment extends Fragment {

    public ListaAlertaFragment() {
    }

    public static ListaAlertaFragment newInstance() {

        Bundle args = new Bundle();

        ListaAlertaFragment fragment = new ListaAlertaFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
