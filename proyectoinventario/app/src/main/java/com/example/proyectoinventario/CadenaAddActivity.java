package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityCadenaAddBinding;

public class CadenaAddActivity extends AppCompatActivity {

    ActivityCadenaAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadenaAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent it = new Intent(this, CadenaActivity.class);

        binding.button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cadena cad = new Cadena();
                cad.setNombre(binding.editTextTextPersonName2.getText().toString());

                if (binding.editTextTextPersonName2.getText().toString().trim().equals("")){
                    binding.editTextTextPersonName2.setHintTextColor(Color.RED);
                }else{
                    db.execSQL("INSERT INTO "+Contract.CadenaEntry.TABLE_NAME+"("+Contract.CadenaEntry.NOMBRE +") VALUES (\""+cad.getNombre()+"\")");
                    Toast.makeText(getApplicationContext(), "Cadena añadida", Toast.LENGTH_SHORT).show();
                    startActivity(it);
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent itList = new Intent(this, CadenaActivity.class);
        startActivity(itList);
    }
}