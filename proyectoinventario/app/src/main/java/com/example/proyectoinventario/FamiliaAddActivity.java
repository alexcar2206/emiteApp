package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.proyectoinventario.databinding.ActivityFamiliaAddBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class FamiliaAddActivity extends AppCompatActivity {


    ActivityFamiliaAddBinding binding;
    ArrayList<Subfamilia> subfamilias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamiliaAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent it = new Intent(this, FamiliaActivity.class);


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        subfamilias = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM "+Contract.SubfamiliaEntry.TABLE_NAME,null);

        while (c.moveToNext()){

            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.SubfamiliaEntry.ID));
            @SuppressLint("Range") String name = c.getString(c.getColumnIndex(Contract.SubfamiliaEntry.NOMBRE));

            Subfamilia sb = new Subfamilia();
            sb.setId(id);
            sb.setNombre(name);

            subfamilias.add(sb);

        }



        ChipGroup contenedor = binding.ChipGroupFamilia;
        //contenedor.setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.FILL_PARENT, ChipGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < subfamilias.size(); i++) {

            Chip chip = new Chip(this);
            chip.setId(subfamilias.get(i).getId());
            chip.setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
            chip.setText(subfamilias.get(i).getNombre());
            chip.setCheckable(true);




            contenedor.addView(chip);
        }


        binding.btnAddFamiliaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(it);


            }
        });
    }
}