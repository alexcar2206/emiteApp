package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.proyectoinventario.databinding.ActivityProductoInfoBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductoInfoActivity extends AppCompatActivity {

    ActivityProductoInfoBinding binding;

    String[] nombreCluster;
    int[] idCluster;

    ArrayList<String> listClustersName = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductoInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent it = new Intent(this,ProductosInfoModActivity.class);

        Intent intent = getIntent();
        Productos pr = (Productos) intent.getSerializableExtra("CLASSPRODUCTO");


        idCluster = pr.getIdCluster();
        nombreCluster = new String[idCluster.length];

        if (idCluster.length > 0){
            for (int i = 0; i < idCluster.length; i++) {
                Cursor c = db.rawQuery(" SELECT "+Contract.ClusterEntry.NOMBRE+" FROM "+Contract.ClusterEntry.TABLE_NAME+" WHERE "+Contract.ClusterEntry.ID+"="+idCluster[i],null);

                while (c.moveToNext()){
                    nombreCluster[i] = c.getString(c.getColumnIndex(Contract.ClusterEntry.NOMBRE));
                }
            }
        }




        if (nombreCluster.length > 0){
            listClustersName.addAll(Arrays.asList(nombreCluster));
            adapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, listClustersName);
            binding.lvclusterprod.setAdapter(adapter);
        }else {
            binding.textView42.setVisibility(View.GONE);
        }







        Cursor c = db.rawQuery(" SELECT "+Contract.FamiliaEntry.NOMBRE+" FROM "+Contract.FamiliaEntry.TABLE_NAME+ " WHERE "+Contract.FamiliaEntry.ID+" = "+pr.getFamiliaID(), null);
        String familia = "";

        while (c.moveToNext()){
            familia = c.getString(c.getColumnIndex(Contract.FamiliaEntry.NOMBRE));
        }

        binding.textViewNombreProd.setText(pr.getNombre());
        binding.textViewFamiliaProd.setText(familia);


        binding.button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                it.putExtra("CLASSPRODUCTO", pr);
                startActivity(it);
            }
        });
    }
}