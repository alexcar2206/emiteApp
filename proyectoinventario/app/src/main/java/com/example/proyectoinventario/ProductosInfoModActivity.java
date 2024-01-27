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

import com.example.proyectoinventario.databinding.ActivityProductosInfoModBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductosInfoModActivity extends AppCompatActivity {

    ActivityProductosInfoModBinding binding;

    HashMap<String,Integer> familiasID;

    ArrayList<String> familiasName;
    ArrayList<Familia> familias;
    String familia;
    ArrayAdapter<String> adapter;
    int familiaSelect;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductosInfoModBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent it = new Intent(this, ProductosActivity.class);
        Intent intent = getIntent();
        Productos pr = (Productos) intent.getSerializableExtra("CLASSPRODUCTO");


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cClusters = db.rawQuery(" SELECT * FROM "+Contract.ClusterEntry.TABLE_NAME,null);
        Cursor c2 = db.rawQuery(" SELECT * FROM "+Contract.FamiliaEntry.TABLE_NAME,null);
        Cursor cFamilia = db.rawQuery(" SELECT "+Contract.FamiliaEntry.NOMBRE+" FROM "+Contract.FamiliaEntry.TABLE_NAME+" WHERE "+Contract.FamiliaEntry.ID+" = "+pr.getFamiliaID(),null);




        while (cFamilia.moveToNext()){

            familia = cFamilia.getString(cFamilia.getColumnIndex(Contract.FamiliaEntry.NOMBRE));
        }


        familiasName = new ArrayList<>();
        familiasID = new HashMap<>();
        familias = new ArrayList<>();

        while (c2.moveToNext()){

            @SuppressLint("Range") int pos = c2.getInt(c2.getColumnIndex(Contract.FamiliaEntry.ID));
            @SuppressLint("Range") String nombre = c2.getString(c2.getColumnIndex(Contract.FamiliaEntry.NOMBRE));

            familiasID.put(nombre,pos);
            familiasName.add(nombre);

            Familia fl = new Familia();
            fl.setId(pos);
            fl.setNombre(nombre);
            familias.add(fl);
        }



        adapter = new ArrayAdapter<>(this, R.layout.spinner_item , familiasName);

        binding.spinner5.setAdapter(adapter);
        binding.spinner5.setSelection(familiasName.indexOf(familia));




        int[] clustersProduct = pr.getIdCluster();
        ArrayList<Cluster> clusters = new ArrayList<>();

        while (cClusters.moveToNext()){

            @SuppressLint("Range") int ID = cClusters.getInt(cClusters.getColumnIndex(Contract.ClusterEntry.ID));
            @SuppressLint("Range") String nombre = cClusters.getString(cClusters.getColumnIndex(Contract.ClusterEntry.NOMBRE));
            @SuppressLint("Range") int cadID = cClusters.getInt(cClusters.getColumnIndex(Contract.ClusterEntry.CAD_ID));

            Cluster cl = new Cluster();
            cl.setId(ID);
            cl.setNombre(nombre);
            cl.setCad_id(cadID);

            clusters.add(cl);
        }




        ChipGroup contenedor = binding.chipgroup;


        for (int i = 0; i < clusters.size(); i++) {

            Chip chip = new Chip(this);
            chip.setId(clusters.get(i).getId());
            chip.setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
            chip.setText(clusters.get(i).getNombre());
            chip.setCheckable(true);

            for (int j = 0; j < clustersProduct.length; j++) {
                if (clustersProduct[j] == clusters.get(i).getId()){
                    chip.setChecked(true);
                }
            }
            contenedor.addView(chip);
        }



        binding.editTextTextPersonName5.setText(pr.getNombre());



        binding.spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                familiaSelect = familias.get(position).getId();
                Toast.makeText(getApplicationContext(), "ID:"+familiaSelect, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.button16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Integer> checkedClusters = binding.chipgroup.getCheckedChipIds();
                int[] nuevosClustersID = new int[checkedClusters.size()];

                for (int j = 0; j < nuevosClustersID.length; j++) {

                    nuevosClustersID[j] = checkedClusters.get(j);
                }

                pr.setNombre(binding.editTextTextPersonName5.getText().toString());
                pr.setFamiliaID(familiaSelect);
                pr.setIdCluster(nuevosClustersID);



                db.execSQL(" UPDATE "+Contract.ProductosEntry.TABLE_NAME+" SET "+Contract.ProductosEntry.NOMBRE_COMPLETO +" = \'"+pr.getNombre()+"\',"+Contract.ProductosEntry.FAMILIA_ID+" = "+pr.getFamiliaID()+" WHERE "+Contract.ProductosEntry.ID+" = "+pr.getId());
                db.execSQL("DELETE FROM "+Contract.ProductoClusterEntry.TABLE_NAME+ " WHERE "+Contract.ProductoClusterEntry.ID_PRODUCTO+" = "+pr.getId());

                for (int i = 0; i < nuevosClustersID.length; i++) {
                    db.execSQL("INSERT INTO "+Contract.ProductoClusterEntry.TABLE_NAME+ "("+ Contract.ProductoClusterEntry.ID_PRODUCTO+","+Contract.ProductoClusterEntry.ID_CLUSTER+") VALUES ("+pr.getId()+","+nuevosClustersID[i]+")");

                }

                Toast.makeText(getApplicationContext(), "Datos guardados", Toast.LENGTH_SHORT).show();

                startActivity(it);
            }
        });

    }
}