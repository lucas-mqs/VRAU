package br.com.sovrau.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.sovrau.R;

/**
 * Created by Lucas on 11/09/2016.
 */
public class IniciaPercursoFragment extends Fragment {

    public static IniciaPercursoFragment newInstance() {
        Bundle args = new Bundle();

        IniciaPercursoFragment fragment = new IniciaPercursoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public  IniciaPercursoFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inicia_percurso_activity, container, false);
    }
}
