package com.example.proyectoinventario;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DialogoFecha extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        // Cogemos la fecha actual para inicializar el calendario.
        // Se puede usar un constructor para inicializarlo con otra fecha
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        // Se crea el diálogo y lo devuelve
        return new DatePickerDialog(getActivity(), this, anio, mes, dia);
    }
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String mensaje=dayOfMonth+ "/"+(monthOfYear+1)+"/"+year;
        ((AccederInventarioActivity)getActivity()).cambiarFecha(mensaje);
        AccederInventarioActivity.touch = 0;
    }
}
