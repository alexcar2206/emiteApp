package com.example.proyectoinventario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class DialogoAlerta extends DialogFragment {
    @Override // Función llamada por el sistema al crear el diálogo
    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Se encarga de configurar el diálogo.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
// Se especifica el mensaje del diálogo
        builder.setMessage(R.string.mensaje);
// Y el título
        builder.setTitle(R.string.titulo);
// Se añade el botón de respuesta positiva con su evento
        builder.setPositiveButton(R.string.boton_positivo, new
                DialogInterface.OnClickListener() {
                    @Override // Click en el botón positivo.
                    public void onClick(DialogInterface dialog, int id) {
                        ((InventariosActivity)getActivity()).opcionOk(getString(R.string.respuesta_ok));
                    }
                });
// Se añade el botón de respuesta negativa
        builder.setNegativeButton(R.string.boton_negativo, new
                DialogInterface.OnClickListener() {
                    @Override // Click en el botón negativo
                    public void onClick(DialogInterface dialog, int id) {
                        ((InventariosActivity)getActivity()).opcionCancel(
                                getString(R.string.respuesta_negativa));
                    }
                });

// Se devuelve el diálogo al sistema para que lo muestre
        return builder.create();
    }
}