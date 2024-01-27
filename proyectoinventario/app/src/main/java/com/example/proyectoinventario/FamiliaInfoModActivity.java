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

import com.example.proyectoinventario.databinding.ActivityFamiliaInfoModBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FamiliaInfoModActivity extends AppCompatActivity {

    ActivityFamiliaInfoModBinding binding;
    Familia fl;


    ArrayList<Subfamilia> subfamilias;

    ArrayList<Integer> listaSubID = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamiliaInfoModBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();



        Intent intent = getIntent();
        fl = (Familia) intent.getSerializableExtra("CLASSFAMILIA");

        Intent it = new Intent(this, FamiliaActivity.class);





        binding.editTextFamilia.setText(fl.getNombre());

       listaSubID = new ArrayList<>();

       Cursor clistSub = db.rawQuery(" SELECT "+ Contract.SubfamiliaEntry.ID+" FROM "+ Contract.SubfamiliaEntry.TABLE_NAME+" WHERE "+Contract.SubfamiliaEntry.ID_FAMILIA+" = "+fl.getId(),null);

       while (clistSub.moveToNext()){
           @SuppressLint("Range") int idSub = clistSub.getInt(clistSub.getColumnIndex(Contract.SubfamiliaEntry.ID));
           listaSubID.add(idSub);
       }




        subfamilias = new ArrayList<>();

        Cursor cSub = db.rawQuery(" SELECT * FROM "+Contract.SubfamiliaEntry.TABLE_NAME, null);

        while (cSub.moveToNext()){
            @SuppressLint("Range") int id = cSub.getInt(cSub.getColumnIndex(Contract.SubfamiliaEntry.ID));
            @SuppressLint("Range") String nombre = cSub.getString(cSub.getColumnIndex(Contract.SubfamiliaEntry.NOMBRE));
            @SuppressLint("Range") int idFam = cSub.getInt(cSub.getColumnIndex(Contract.SubfamiliaEntry.ID_FAMILIA));

            Subfamilia sb = new Subfamilia();
            sb.setId(id);
            sb.setNombre(nombre);
            sb.setIdFamilia(idFam);

            subfamilias.add(sb);
        }




        ChipGroup chipGroupSub = binding.chipGroupFam;

        //chipGroupSub.setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.FILL_PARENT, ChipGroup.LayoutParams.WRAP_CONTENT));


        for (int i = 0; i < subfamilias.size(); i++) {

            Chip chip = new Chip(this);
            chip.setId(subfamilias.get(i).getId());
            chip.setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
            chip.setText(subfamilias.get(i).getNombre());
            chip.setCheckable(true);


            for (int j = 0; j < listaSubID.size(); j++) {
                if (listaSubID.get(j) == subfamilias.get(i).getId()){
                    chip.setChecked(true);
                }
            }


            chipGroupSub.addView(chip);
        }






        binding.button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Integer> subfamiliasIDNuevos = binding.chipGroupFam.getCheckedChipIds();


                for (int i = 0; i < listaSubID.size(); i++) {
                    db.execSQL(" UPDATE "+Contract.SubfamiliaEntry.TABLE_NAME+" SET "+Contract.SubfamiliaEntry.ID_FAMILIA+" = "+0+" WHERE "+Contract.SubfamiliaEntry.ID+" = "+listaSubID.get(i));
                }




                for (int i = 0; i < subfamiliasIDNuevos.size(); i++) {
                    db.execSQL("UPDATE "+Contract.SubfamiliaEntry.TABLE_NAME+" SET "+ Contract.SubfamiliaEntry.ID_FAMILIA+" = "+fl.getId()+" WHERE "+Contract.SubfamiliaEntry.ID+" = "+subfamiliasIDNuevos.get(i));
                }




                String nombreFamilia = binding.editTextFamilia.getText().toString();

                db.execSQL("UPDATE "+Contract.FamiliaEntry.TABLE_NAME+" SET "+Contract.FamiliaEntry.NOMBRE+" = \'"+nombreFamilia+ "\' WHERE "+Contract.FamiliaEntry.ID+" = "+fl.getId());

                Toast.makeText(getApplicationContext(), "Datos guardados", Toast.LENGTH_SHORT).show();
                startActivity(it);
            }
        });

    }
}