package com.example.proyectoinventario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

public class AlertDialogInvsPend extends DialogFragment {

    @Override // Función llamada por el sistema al crear el diálogo
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Quieres continuar con ellos?");
        builder.setTitle("Tienes inventarios pendientes");


        builder.setPositiveButton(R.string.boton_positivo, new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity)getActivity()).opcionOk("");

                    }
                });

        builder.setNegativeButton(R.string.boton_negativo, new
                DialogInterface.OnClickListener() {
                    @Override // Click en el botón negativo
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity)getActivity()).opcionCancel("");

                    }
                });

        return builder.create();
    }
}
