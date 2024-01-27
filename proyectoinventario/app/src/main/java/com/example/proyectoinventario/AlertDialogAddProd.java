package com.example.proyectoinventario;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class AlertDialogAddProd extends DialogFragment {



    ArrayList<String> names;

    SQLiteDatabase db = InventarioActivity.db;


    ArrayAdapter<String> adapter;
    ArrayList<Integer> idProd;

    int idSelected;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        View customLayout = getLayoutInflater().inflate(R.layout.add_prod_dialog,null);
        View title = getLayoutInflater().inflate(R.layout.add_prod_title, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Cursor c = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME+" WHERE "+Contract.ProductosEntry.ID+" NOT IN ( SELECT "+Contract.ProductoClusterEntry.ID_PRODUCTO+" FROM "+Contract.ProductoClusterEntry.TABLE_NAME+" WHERE "+Contract.ProductoClusterEntry.ID_CLUSTER+" = "+((InventarioActivity)getActivity()).idCluster+")",null);

        names = new ArrayList<>();
        idProd = new ArrayList<>();

        names.add("-Selecciona un producto-");
        idProd.add(0);


        while (c.moveToNext()){
            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.ProductosEntry.ID));
            @SuppressLint("Range") String name = c.getString(c.getColumnIndex(Contract.ProductosEntry.NOMBRE));
            names.add(name);
            idProd.add(id);
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,names);
        Spinner spinner = customLayout.findViewById(R.id.spinnerProds);
        spinner.setAdapter(adapter);

        TextView textView = customLayout.findViewById(R.id.textView76);
        textView.setText("Escoge un producto:");


        builder.setView(customLayout);
        //builder.setTitle("Nuevo producto");
        builder.setCustomTitle(title);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idSelected = idProd.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        builder.setPositiveButton(R.string.boton_positivo, new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        ((InventarioActivity)getActivity()).opcionOk(idSelected);
                    }
                });

        builder.setNegativeButton(R.string.boton_negativo, new
                DialogInterface.OnClickListener() {
                    @Override // Click en el botón negativo
                    public void onClick(DialogInterface dialog, int id) {
                        ((InventarioActivity)getActivity()).opcionCancel("");
                    }
                });


        return builder.create();
    }
}
