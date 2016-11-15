package br.com.sovrau.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import br.com.sovrau.R;
import br.com.sovrau.constants.Constants;

/**
 * Created by Lucas on 15/11/2016.
 */

public class NumberPickerFragment extends DialogFragment {
    private static final int REQUEST_CODE = 101;

    private MaterialNumberPicker numberPicker;

    public static NumberPickerFragment newInstance() {

        Bundle args = new Bundle();

        NumberPickerFragment fragment = new NumberPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.number_picker_fragment, null);

        numberPicker = (MaterialNumberPicker) view.findViewById(R.id.npDias);

        Dialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.putExtra(Constants.EXTRA_PERIODO, numberPicker.getValue());
                                getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, intent);
                            }
                        })
                .create();
        return alertDialog;
    }
}
