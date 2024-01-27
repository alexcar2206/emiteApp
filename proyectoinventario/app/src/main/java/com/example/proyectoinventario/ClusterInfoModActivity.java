package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityClusterInfoModBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ClusterInfoModActivity extends AppCompatActivity {


    ActivityClusterInfoModBinding binding;

    ArrayList<String> cadenasName;
    ArrayList<Cadena> cadenas;

    int cadSelected;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClusterInfoModBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent it = new Intent(this, ClusterActivity.class);

        Intent intent = getIntent();
        Cluster cl = (Cluster) intent.getSerializableExtra("CLASECLUSTER");



        cadenasName = new ArrayList<>();
        cadenasName.add("-Selecciona una opción");
        cadenas = new ArrayList<>();


        Cursor c = db.rawQuery(" SELECT * FROM "+ Contract.CadenaEntry.TABLE_NAME,null);

        while (c.moveToNext()){

            int id = c.getInt(c.getColumnIndex(Contract.ClusterEntry.ID));
            String nombreCad = c.getString(c.getColumnIndex(Contract.CadenaEntry.NOMBRE));


            cadenasName.add(nombreCad);

            Cadena cad = new Cadena();
            cad.setId(id);
            cad.setNombre(nombreCad);
            cadenas.add(cad);
        }


        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner_item , cadenasName);
        binding.spinner2.setAdapter(adapter);

        String cadena = "";
        c = db.rawQuery(" SELECT "+Contract.CadenaEntry.NOMBRE+" FROM "+Contract.CadenaEntry.TABLE_NAME+" WHERE "+Contract.CadenaEntry.ID+" IS "+cl.getCad_id(), null);

        while (c.moveToNext()){
            cadena = c.getString(c.getColumnIndex(Contract.CadenaEntry.NOMBRE));
        }


        int pos = cadenasName.indexOf(cadena);
        binding.spinner2.setSelection(pos);
        binding.editTextTextPersonName.setText(cl.getNombre());


        binding.button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cadSelected != -1){

                    db.execSQL("UPDATE "+Contract.ClusterEntry.TABLE_NAME+" SET "+Contract.ClusterEntry.NOMBRE+" = \'"+binding.editTextTextPersonName.getText().toString()+"\',  "+Contract.ClusterEntry.CAD_ID+" = "+cadSelected+" WHERE "+Contract.ClusterEntry.ID+" ="+cl.getId());
                    Toast.makeText(ClusterInfoModActivity.this, "Datos guardados", Toast.LENGTH_SHORT).show();
                    startActivity(it);
                }


            }
        });

        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0){
                    cadSelected = cadenas.get(position - 1).getId();
                }else{
                    cadSelected = -1;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent itList = new Intent(this, ClusterActivity.class);
        startActivity(itList);
    }
}