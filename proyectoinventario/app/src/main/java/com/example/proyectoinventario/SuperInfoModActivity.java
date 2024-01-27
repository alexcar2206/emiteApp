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
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivitySuperInfoModBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class SuperInfoModActivity extends AppCompatActivity {

    ActivitySuperInfoModBinding binding;


    ArrayList<String> clustersName;
    ArrayList<Cluster> clusters;
    ArrayAdapter<String> adapter;


    ArrayAdapter<String> adapterFrec;
    ArrayList<String> frecuencias;


    String frecuenciaSelect;
    int clusterSelect;




    public String obtenerFrecuencia(String frec){

        String frecuencia = "";

        switch (frec){
            case "Semanal":
                frecuencia = "S";
                break;
            case "Mensual":
                frecuencia = "M";
                break;
            case "Trimestral":
                frecuencia = "T";
                break;
            case "Semestral":
                frecuencia = "SEM";
                break;
            case "Bimensual":
                frecuencia = "BM";
                break;
            case "Anual":
                frecuencia = "A";
                break;
            case "Quincenal":
                frecuencia = "Q";
                break;
            default:
                break;
        }

        return frecuencia;
    }


    public boolean comprobareditTextVacios(EditText editText){
        if (editText.getText().toString().trim().equals("")){
            editText.setHintTextColor(Color.RED);
            return true;
        }else{
            return false;
        }
    }


    public boolean comprobarCamposVacios(){

        boolean vacio = false;

        if (comprobareditTextVacios(binding.editTextTextNombre)) vacio = true;
        if (comprobareditTextVacios(binding.editTextTextCoordX)) vacio = true;
        if (comprobareditTextVacios(binding.editTextTextCoordY)) vacio = true;
        if (comprobareditTextVacios(binding.editTextTextDir)) vacio = true;
        if (comprobareditTextVacios(binding.editTextTextProv))  vacio = true;
        if (comprobareditTextVacios(binding.editTextTextCiudad)) vacio = true;
        if (comprobareditTextVacios(binding.editTextPersona)) vacio = true;
        if (comprobareditTextVacios(binding.editTextEmail)) vacio = true;
        if (comprobareditTextVacios(binding.editTextNumero)) vacio = true;
        if (comprobareditTextVacios(binding.editTextVisitas)) vacio = true;


        return vacio;
    }


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuperInfoModBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent intent = getIntent();
        Super sp = (Super) intent.getSerializableExtra("CLASESUPER");

        Intent it = new Intent(this, SuperActivity.class);


        clustersName = new ArrayList<>();
        clustersName.add("-Selecciona una opcion-");
        clusters = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM "+Contract.ClusterEntry.TABLE_NAME,null);

        while (c.moveToNext()){
            int id = c.getInt(c.getColumnIndex(Contract.ClusterEntry.ID));
            String nombre = c.getString(c.getColumnIndex(Contract.ClusterEntry.NOMBRE));
            int cadId = c.getInt(c.getColumnIndex(Contract.ClusterEntry.CAD_ID));


            clustersName.add(nombre);

            Cluster cl = new Cluster();
            cl.setId(id);
            cl.setNombre(nombre);
            cl.setCad_id(cadId);
            clusters.add(cl);
        }

        adapter= new ArrayAdapter<>(this, R.layout.spinner_item , clustersName);
        binding.spinner.setAdapter(adapter);


        frecuencias = new ArrayList<>();
        frecuencias.add("-Selecciona una opción-");
        frecuencias.add("Semanal");
        frecuencias.add("Mensual");
        frecuencias.add("Trimestral");
        frecuencias.add("Semestral");
        frecuencias.add("Bimensual");
        frecuencias.add("Anual");
        frecuencias.add("Quincenal");

        adapterFrec = new ArrayAdapter<>(this, R.layout.spinner_item, frecuencias);

        binding.spinnerFrec.setAdapter(adapterFrec);



        String cluster = "";
        c = db.rawQuery(" SELECT "+Contract.ClusterEntry.NOMBRE+" FROM "+Contract.ClusterEntry.TABLE_NAME+" WHERE "+Contract.CadenaEntry.ID+" IS "+sp.getCluster_id(), null);

        while (c.moveToNext()){
            cluster = c.getString(c.getColumnIndex(Contract.ClusterEntry.NOMBRE));
        }


        if (cluster!=""){

            int pos = clusters.indexOf(cluster);
            binding.spinner.setSelection(pos);
            clusterSelect = clusters.get(pos - 1).getId();
        }


        if (sp.getNombre()!= null){
            binding.editTextTextNombre.setText(sp.getNombre());
        }

        if (sp.getCoorX()!= 0.0 && sp.getCoorY() != 0.0){
            binding.editTextTextCoordX.setText(sp.getCoorX()+"");
            binding.editTextTextCoordY.setText(sp.getCoorY()+"");
        }

        if (sp.getDireccion() != null){
            binding.editTextTextDir.setText(sp.getDireccion());
            binding.editTextTextProv.setText(sp.getProvincia());
            binding.editTextTextCiudad.setText(sp.getCiudad());
        }

        if (sp.getPersonaContacto()!= null){
            binding.editTextPersona.setText(sp.getPersonaContacto());
            binding.editTextEmail.setText(sp.getEmail());
            binding.editTextNumero.setText(sp.getTelefono()+"");
        }

        if (sp.getFrecuencia() != null){

            int pos;
            pos = frecuencias.indexOf(sp.getFrecuencia());
            binding.spinnerFrec.setSelection(pos);
        }

        if (sp.getVisitasAno() != 0){
            binding.editTextVisitas.setText(sp.getVisitasAno()+"");
        }


        if (sp.getObservaciones() != null){
            binding.editTextObservaciones.setText(sp.getObservaciones());
        }



        binding.spinnerFrec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0){
                    frecuenciaSelect = obtenerFrecuencia(frecuencias.get(position));
                }else{
                    frecuenciaSelect = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!comprobarCamposVacios() && !frecuenciaSelect.equals("") && clusterSelect != -1){

                    db.execSQL("UPDATE "+Contract.SuperEntry.TABLE_NAME+" SET "+Contract.SuperEntry.NOMBRE+" = \'"+binding.editTextTextNombre.getText().toString()+"\',  "+Contract.SuperEntry.CLUSTER_ID+" = "+clusterSelect+", "+Contract.SuperEntry.COORDX+"="+Double.parseDouble(binding.editTextTextCoordX.getText().toString())+" ,"+Contract.SuperEntry.COORSY+"="+Double.parseDouble(binding.editTextTextCoordY.getText().toString())+","+Contract.SuperEntry.DIRECCION+"=\'"+binding.editTextTextDir.getText()+"\',"+Contract.SuperEntry.PROVINCIA+"=\'"+binding.editTextTextProv.getText()+"\',"+Contract.SuperEntry.CIUDAD+"=\'"+binding.editTextTextCiudad.getText()+"\' ,"+Contract.SuperEntry.PERSONA_CONTACTO+"=\'"+binding.editTextPersona.getText()+"\',"+Contract.SuperEntry.EMAIL+"=\'"+binding.editTextEmail.getText()+"\',"+Contract.SuperEntry.TELEFONO+"="+Integer.parseInt(binding.editTextNumero.getText().toString())+","+Contract.SuperEntry.FRECUENCIA+" = \'"+frecuenciaSelect+"\',"+Contract.SuperEntry.VISITAS_ANO+"="+Integer.parseInt(binding.editTextVisitas.getText().toString())+","+Contract.SuperEntry.OBSERVACIONES+"=\'"+binding.editTextObservaciones.getText()+"\'"+" WHERE "+Contract.SuperEntry.ID+" ="+sp.getId());
                    Toast.makeText(getApplicationContext(), "Datos guardados", Toast.LENGTH_SHORT).show();
                    startActivity(it);
                }


            }
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0){
                    clusterSelect = clusters.get(position - 1).getId();
                }else{
                    clusterSelect = -1;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent itList = new Intent(this, SuperActivity.class);
        startActivity(itList);
    }
}