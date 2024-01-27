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

import com.example.proyectoinventario.databinding.ActivityProductosAddBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class ProductosAddActivity extends AppCompatActivity {

    ActivityProductosAddBinding binding;

    ArrayList<Cluster> clusters;
    ArrayList<String> nombresFamilia;
    ArrayList<Familia> familias;
    ArrayAdapter<String> adapter;

    int FamiliaSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductosAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cFam = db.rawQuery("SELECT * FROM "+Contract.FamiliaEntry.TABLE_NAME,null);
        Cursor cCluster = db.rawQuery("SELECT * FROM "+Contract.ClusterEntry.TABLE_NAME, null);



        Intent it = new Intent(this, ProductosActivity.class);


        nombresFamilia = new ArrayList<>();
        nombresFamilia.add("-Selecciona una opción-");
        familias = new ArrayList<>();

        while (cFam.moveToNext()){
            @SuppressLint("Range") int id = cFam.getInt(cFam.getColumnIndex(Contract.FamiliaEntry.ID));
            @SuppressLint("Range") String nombre = cFam.getString(cFam.getColumnIndex(Contract.FamiliaEntry.NOMBRE));

            nombresFamilia.add(nombre);

            Familia fl = new Familia();
            fl.setId(id);
            fl.setNombre(nombre);
            familias.add(fl);
        }

        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, nombresFamilia);
        binding.spinner7.setAdapter(adapter);



        clusters = new ArrayList<>();

        while (cCluster.moveToNext()){
            @SuppressLint("Range") int id = cCluster.getInt(cCluster.getColumnIndex(Contract.ClusterEntry.ID));
            @SuppressLint("Range") String nombre = cCluster.getString(cCluster.getColumnIndex(Contract.ClusterEntry.NOMBRE));
            @SuppressLint("Range") int idCad = cCluster.getInt(cCluster.getColumnIndex(Contract.ClusterEntry.CAD_ID));

            Cluster cl = new Cluster();
            cl.setId(id);
            cl.setNombre(nombre);
            cl.setCad_id(idCad);

            clusters.add(cl);
        }


        ChipGroup clusterChip = binding.chipgroupCluster;
        clusterChip.setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.FILL_PARENT, ChipGroup.LayoutParams.WRAP_CONTENT));


        for (int i = 0; i < clusters.size(); i++) {

            Chip chip = new Chip(this);
            chip.setId(clusters.get(i).getId());
            chip.setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
            chip.setText(clusters.get(i).getNombre());
            chip.setCheckable(true);

            clusterChip.addView(chip);
        }



        binding.spinner7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0){
                    FamiliaSelect = familias.get(position).getId();
                }else{
                    FamiliaSelect = -1;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.buttonAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (FamiliaSelect != -1){

                    Productos pr = new Productos();
                    pr.setNombre(binding.editTextTextNombreProduct.getText().toString());
                    pr.setFamiliaID(FamiliaSelect);

                    db.execSQL("INSERT INTO "+Contract.ProductosEntry.TABLE_NAME+"("+Contract.ProductosEntry.NOMBRE_COMPLETO +","+Contract.ProductosEntry.FAMILIA_ID+") VALUES (\'"+pr.getNombre()+"\',"+pr.getFamiliaID()+")");


                    Cursor cprod = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME,null);

                    cprod.moveToLast();
                    @SuppressLint("Range") int idProd = cprod.getInt(cprod.getColumnIndex(Contract.ProductosEntry.ID));


                    List<Integer> idsClustersSelect = clusterChip.getCheckedChipIds();

                    if (idsClustersSelect.size() > 0){

                        for (int i = 0; i < idsClustersSelect.size(); i++) {

                            db.execSQL("INSERT INTO "+Contract.ProductoClusterEntry.TABLE_NAME+"("+Contract.ProductoClusterEntry.ID_PRODUCTO+","+Contract.ProductoClusterEntry.ID_CLUSTER+") VALUES ("+idProd+","+idsClustersSelect.get(i)+")");
                        }
                    }

                    startActivity(it);
                }
            }
        });
    }
}