package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.proyectoinventario.databinding.ActivityClusterInfoBinding;

public class ClusterInfoActivity extends AppCompatActivity {

    ActivityClusterInfoBinding binding;
    String cadena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClusterInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent intent = getIntent();
        Cluster cl = (Cluster) intent.getSerializableExtra("CLASECLUSTER");

        Intent it = new Intent(this, ClusterInfoModActivity.class);
        it.putExtra("CLASECLUSTER", cl);



        Cursor c = db.rawQuery("SELECT "+ Contract.CadenaEntry.NOMBRE+ " FROM "+Contract.CadenaEntry.TABLE_NAME+ " WHERE "+ Contract.CadenaEntry.ID+ " IS "+cl.getCad_id(),null);

        while (c.moveToNext()){
            @SuppressLint("Range") String nombreCadena = c.getString(c.getColumnIndex(Contract.CadenaEntry.NOMBRE));
            cadena = nombreCadena;
        }




        binding.textView6.setText(cl.getNombre());
        binding.textView7.setText(cadena);

        binding.button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent itList = new Intent(this, ClusterActivity.class);
        startActivity(itList);
    }
}