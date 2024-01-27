package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.proyectoinventario.databinding.ActivitySuperInfoBinding;

public class SuperInfoActivity extends AppCompatActivity {

    ActivitySuperInfoBinding binding;


    public void comprobarNull(Super sp,String cluster){
        if (sp.getNombre()== null){
            binding.textViewNombre.setVisibility(View.GONE);
            binding.textView10.setVisibility(View.GONE);
        }else{
            binding.textViewNombre.setText(sp.getNombre());
        }

        if (cluster == null||cluster == ""){
            binding.textViewCluster.setVisibility(View.GONE);
            binding.textView11.setVisibility(View.GONE);
        }else{
            binding.textViewCluster.setText(cluster);
        }

        if (sp.getCoorX() == 0.0 && sp.getCoorY() == 0.0){
            binding.textViewLoc.setVisibility(View.GONE);
            binding.textView12.setVisibility(View.GONE);
            binding.textViewCoorX.setVisibility(View.GONE);
            binding.textViewCoordY.setVisibility(View.GONE);
            binding.textView43.setVisibility(View.GONE);
            binding.textView44.setVisibility(View.GONE);
        }else{
            binding.textViewLoc.setText(sp.getCoorX()+", "+sp.getCoorY());
            binding.textViewCoorX.setText(sp.getCoorX()+"");
            binding.textViewCoordY.setText(sp.getCoorY()+"");
        }

        if (sp.getDireccion()== null){
            binding.textViewDir.setVisibility(View.GONE);
            binding.textView13.setVisibility(View.GONE);
            binding.textViewProv.setVisibility(View.GONE);
            binding.textViewCiudad.setVisibility(View.GONE);
            binding.textView14.setVisibility(View.GONE);
            binding.textView15.setVisibility(View.GONE);
        }else{
            binding.textViewDir.setText(sp.getDireccion());
            binding.textViewProv.setText(sp.getProvincia());
            binding.textViewCiudad.setText(sp.getCiudad());
        }

        if (sp.getPersonaContacto() == null){
            binding.textViewPersona.setVisibility(View.GONE);
            binding.textView16.setVisibility(View.GONE);
            binding.textViewEmail.setVisibility(View.GONE);
            binding.textViewTelefono.setVisibility(View.GONE);
            binding.textView29.setVisibility(View.GONE);
            binding.textVieww.setVisibility(View.GONE);
        }else{
            binding.textViewPersona.setText(sp.getPersonaContacto());
            binding.textViewEmail.setText(sp.getEmail());
            binding.textViewTelefono.setText(sp.getTelefono()+"");
        }

        if (sp.getFrecuencia() == null){
            binding.textViewFrec.setVisibility(View.GONE);
            binding.textView67.setVisibility(View.GONE);

        }else{
            binding.textViewFrec.setText(sp.getFrecuencia());
        }

        if (sp.getVisitasAno() == 0){
            binding.textViewVisit.setVisibility(View.GONE);
            binding.textView71.setVisibility(View.GONE);

        }else{
            binding.textViewVisit.setText(sp.getVisitasAno()+"");
        }

        if (sp.getObservaciones() == null){
            binding.textViewObser.setVisibility(View.GONE);
            binding.textView19.setVisibility(View.GONE);

        }else{
            binding.textViewObser.setText(sp.getObservaciones());
        }




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuperInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();




        Intent intent = getIntent();
        Super sp = (Super)intent.getSerializableExtra("CLASESUPER");

        Intent it = new Intent(this, SuperInfoModActivity.class);
        it.putExtra("CLASESUPER", sp);




        Cursor c = db.rawQuery("SELECT "+Contract.ClusterEntry.NOMBRE+ " FROM "+Contract.ClusterEntry.TABLE_NAME+" WHERE "+ Contract.ClusterEntry.ID+" IS "+sp.getCluster_id(),null);
        String cluster = "";

        while (c.moveToNext()){
            @SuppressLint("Range") String nombreCluster = c.getString(c.getColumnIndex(Contract.ClusterEntry.NOMBRE));
            cluster = nombreCluster;
        }



        comprobarNull(sp, cluster);


/*
        binding.textViewNombre.setText(sp.getNombre());
        binding.textViewCluster.setText(cluster);
        binding.textViewLoc.setText(sp.getLocalizacion());
        binding.textViewCoorX.setText(sp.getCoorX()+"");
        binding.textViewCoordY.setText(sp.getCoorY()+"");
        binding.textViewDir.setText(sp.getDireccion());
        binding.textViewProv.setText(sp.getProvincia());
        binding.textViewCiudad.setText(sp.getCiudad());
        binding.textViewPersona.setText(sp.getPersonaContacto());
        binding.textViewEmail.setText(sp.getEmail());
        binding.textViewTelefono.setText(sp.getTelefono()+"");
        binding.textViewFrec.setText(sp.getFrecuencia());
        binding.textViewVisit.setText(sp.getVisitasAno()+"");
        binding.textViewObser.setText(sp.getObservaciones());

 */


        binding.buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent itList = new Intent(this, SuperActivity.class);
        startActivity(itList);
    }
}