package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityClusterAddBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ClusterAddActivity extends AppCompatActivity {

    ActivityClusterAddBinding binding;

    ArrayList<String> cadenasName;
    ArrayList<Cadena> cadenas;
    ArrayAdapter<String> adapter;

    int cadenaID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClusterAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();



        Intent it = new Intent(this, ClusterActivity.class);


        cadenasName = new ArrayList<>();
        cadenasName.add("-Selecciona una cadena-");
        cadenas = new ArrayList<>();


        Cursor c = db.rawQuery("SELECT * FROM "+Contract.CadenaEntry.TABLE_NAME,null);

        while (c.moveToNext()){
            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.ClusterEntry.ID));
            @SuppressLint("Range") String name = c.getString(c.getColumnIndex(Contract.CadenaEntry.NOMBRE));
            cadenasName.add(name);

            Cadena cad = new Cadena();
            cad.setId(id);
            cad.setNombre(name);

            cadenas.add(cad);
        }


        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, cadenasName);
        binding.spinner4.setAdapter(adapter);


        binding.buttonAddCluster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cadenaID != -1){

                    Cluster cl = new Cluster();
                    cl.setNombre( binding.editTextCluster.getText().toString());
                    cl.setCad_id(cadenaID);


                    if (binding.editTextCluster.getText().toString().trim().equals("")){
                        binding.editTextCluster.setHintTextColor(Color.RED);
                    }


                    db.execSQL("INSERT INTO "+Contract.ClusterEntry.TABLE_NAME+"("+Contract.ClusterEntry.NOMBRE +","+Contract.ClusterEntry.CAD_ID+") VALUES (\'"+cl.getNombre()+"\',"+cl.getCad_id()+")");
                    Toast.makeText(getApplicationContext(), "Cluster añadido", Toast.LENGTH_SHORT).show();
                    startActivity(it);

                }


            }
        });


        binding.spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("Range")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position > 0){

                    cadenaID = cadenas.get(position-1).getId();
                }else{
                    cadenaID = -1;
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