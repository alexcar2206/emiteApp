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

import com.example.proyectoinventario.databinding.ActivitySuperAddBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SuperAddActivity extends AppCompatActivity {

    ActivitySuperAddBinding binding;


    ArrayList<String> clustersName;
    ArrayList<Cluster> clusters;

    ArrayList<String> frecuencia;
    String[] frecArray;
    String frecSelect;

    ArrayAdapter<String> adapter;

    int clusterID;



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
        if (comprobareditTextVacios(binding.editTextTextCoorX)) vacio = true;
        if (comprobareditTextVacios(binding.editTextCoorY)) vacio = true;
        if (comprobareditTextVacios(binding.editTextTextDir)) vacio = true;
        if (comprobareditTextVacios(binding.editTextTextProv))  vacio = true;
        if (comprobareditTextVacios(binding.editTextTextCiudad)) vacio = true;
        if (comprobareditTextVacios(binding.editTextCodClient)) vacio = true;
        if (comprobareditTextVacios(binding.editTextPersona)) vacio = true;
        if (comprobareditTextVacios(binding.editTextEmail)) vacio = true;
        if (comprobareditTextVacios(binding.editTextNumero)) vacio = true;
        if (comprobareditTextVacios(binding.editTextVisitAno)) vacio = true;


     return vacio;
    }


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuperAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();



        Intent it = new Intent(this, SuperActivity.class);





        clustersName = new ArrayList<>();
        clustersName.add("-Selecciona un cluster-");
        clusters = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM "+Contract.ClusterEntry.TABLE_NAME, null);

        while (c.moveToNext()){
            @SuppressLint("Range") int idCluster = c.getInt(c.getColumnIndex(Contract.ClusterEntry.ID));
            @SuppressLint("Range") String cluster = c.getString(c.getColumnIndex(Contract.ClusterEntry.NOMBRE));
            @SuppressLint("Range") int cadId = c.getInt(c.getColumnIndex(Contract.ClusterEntry.CAD_ID));


            clustersName.add(cluster);

            Cluster cl = new Cluster();
            cl.setId(idCluster);
            cl.setNombre(cluster);
            cl.setCad_id(cadId);
            clusters.add(cl);

        }



        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, clustersName);
        binding.spinner.setAdapter(adapter);


        frecArray = new String[]{"-Selecciona una opción- ","Semanal", "Mensual", "Trimestral","Semestral","Bimensual","Anual","Quincenal"};

        frecuencia = new ArrayList<>();
        frecuencia.addAll(Arrays.asList(frecArray));


        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, frecuencia);
        binding.spinnerFrecuen.setAdapter(adapter);


       binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("Range")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0){

                    clusterID = clusters.get(position - 1).getId();
                }else{

                    clusterID = -1;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



       binding.spinnerFrecuen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               if (position > 0){

                   frecSelect = obtenerFrecuencia(frecuencia.get(position));
               }else{
                   frecSelect = "";
               }

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });



        binding.button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (clusterID != -1 && !comprobarCamposVacios() && frecSelect != ""){

                    Super s = new Super();
                    s.setCluster_id(clusterID);
                    s.setNombre(binding.editTextTextNombre.getText().toString());
                    s.setDireccion(binding.editTextTextDir.getText().toString());
                    s.setCoorX(Double.parseDouble(binding.editTextTextCoorX.getText().toString()));
                    s.setCoorY(Double.parseDouble(binding.editTextCoorY.getText().toString()));
                    s.setProvincia(binding.editTextTextProv.getText().toString());
                    s.setCiudad(binding.editTextTextCiudad.getText().toString());
                    s.setPersonaContacto(binding.editTextPersona.getText().toString());
                    s.setCodCliente(Integer.parseInt(binding.editTextCodClient.getText().toString()));
                    s.setTelefono(Integer.parseInt(binding.editTextNumero.getText().toString()));
                    s.setEmail(binding.editTextEmail.getText().toString());
                    s.setFrecuencia(frecSelect);
                    s.setVisitasAno(Integer.parseInt(binding.editTextVisitAno.getText().toString()));
                    s.setObservaciones(binding.editTextObservaciones.getText().toString());

                    db.execSQL("INSERT INTO "+Contract.SuperEntry.TABLE_NAME+" ("+Contract.SuperEntry.NOMBRE+","+Contract.SuperEntry.CLUSTER_ID+","+Contract.SuperEntry.COORDX+","+Contract.SuperEntry.COORSY+","+Contract.SuperEntry.DIRECCION+","+Contract.SuperEntry.PROVINCIA+","+Contract.SuperEntry.CIUDAD+","+Contract.SuperEntry.PERSONA_CONTACTO+","+Contract.SuperEntry.COD_CLIENTE+","+Contract.SuperEntry.EMAIL+","+Contract.SuperEntry.TELEFONO+","+Contract.SuperEntry.FRECUENCIA+","+Contract.SuperEntry.VISITAS_ANO+","+Contract.SuperEntry.OBSERVACIONES+" ) VALUES ( \'"+s.getNombre()+"\',"+clusterID+","+s.getCoorX()+","+s.getCoorY()+",\'"+s.getDireccion()+"\',\'"+s.getProvincia()+"\',\'"+s.getCiudad()+"\',\'"+s.getPersonaContacto()+"\',"+s.getCodCliente()+",\'"+s.getEmail()+"\',"+s.getTelefono()+",\'"+s.getFrecuencia()+"\',"+s.getVisitasAno()+",\'"+s.getObservaciones()+"\')");
                    Toast.makeText(getApplicationContext(), "Datos añadidos", Toast.LENGTH_SHORT).show();
                    startActivity(it);
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent itList = new Intent(this, SuperActivity.class);
        startActivity(itList);
    }
}