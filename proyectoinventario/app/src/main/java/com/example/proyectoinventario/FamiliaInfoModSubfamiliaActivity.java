package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityFamiliaInfoModSubfamiliaBinding;

public class FamiliaInfoModSubfamiliaActivity extends AppCompatActivity {

    ActivityFamiliaInfoModSubfamiliaBinding binding;
    String nombreSubfamilia;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamiliaInfoModSubfamiliaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent intent = getIntent();
        int idSubfamilia = intent.getIntExtra("IDSUBFAMILIA",0);

        Intent itFam = new Intent(this, FamiliaActivity.class);


        Cursor c = db.rawQuery("SELECT * FROM "+Contract.SubfamiliaEntry.TABLE_NAME+" WHERE "+Contract.SubfamiliaEntry.ID+" = "+idSubfamilia,null);

        while (c.moveToNext()){
            nombreSubfamilia = c.getString(c.getColumnIndex(Contract.SubfamiliaEntry.NOMBRE));
        }

        binding.editTextTextPersonName3.setText(nombreSubfamilia);


        binding.button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombreNuevoSubfamilia = binding.editTextTextPersonName3.getText().toString();
                db.execSQL("UPDATE "+Contract.SubfamiliaEntry.TABLE_NAME+" SET "+Contract.SubfamiliaEntry.NOMBRE+" = \'"+nombreNuevoSubfamilia+"\' WHERE "+Contract.SubfamiliaEntry.ID+" = "+idSubfamilia);
                Toast.makeText(getApplicationContext(), "Datos guardados", Toast.LENGTH_SHORT).show();
                startActivity(itFam);
            }
        });



    }
}