package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivitySuperBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class SuperActivity extends AppCompatActivity {

    ActivitySuperBinding binding;

    AdapterPersonal adapterPersonal;
    ArrayList<InfoAdapter> infoAdapters = new ArrayList<>();
    ListView lv;
    ArrayList<Super> supers;

    boolean LONGCLICK = false;


    public void clickEliminar(SQLiteDatabase db) {

        @SuppressWarnings ("unchecked")

        AdapterPersonal adapter=(AdapterPersonal) lv.getAdapter();

        for (int i= lv.getCount() - 1; i >= 0; i--) {

            if (lv.isItemChecked(i)){
                db.execSQL("DELETE FROM "+Contract.SuperEntry.TABLE_NAME+" WHERE "+Contract.SuperEntry.NOMBRE+" IS \'"+ supers.get(i).getNombre()+"\'");
                infoAdapters.remove(i);
                supers.remove(i);
            }
        }

        lv.getCheckedItemPositions().clear();
        adapter.notifyDataSetChanged();
    }


    public String obtenerFrecuencia(String f){

        String frecuencia = "";

        switch (f){
            case "S":
                frecuencia = "Semanal";
                break;
            case "M":
                frecuencia = "Mensual";
                break;
            case "T":
                frecuencia = "Trimestral";
                break;
            case "SEM":
                frecuencia = "Semestral";
                break;
            case "BM":
                frecuencia = "Bimensual";
                break;
            case "A":
                frecuencia = "Anual";
                break;
            case "Q":
                frecuencia = "Quincenal";
                break;
        }

        return  frecuencia;

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySuperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent it = new Intent(this, SuperInfoActivity.class);
        Intent itAddSuper = new Intent(this, SuperAddActivity.class);


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();




        supers = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+Contract.SuperEntry.TABLE_NAME,null);

        while (c.moveToNext()){

            @SuppressLint("Range") int ID = c.getInt(c.getColumnIndex(Contract.SuperEntry.ID));
            @SuppressLint("Range") String nombre = c.getString(c.getColumnIndex(Contract.SuperEntry.NOMBRE));
            @SuppressLint("Range") int idCluster = c.getInt(c.getColumnIndex(Contract.SuperEntry.CLUSTER_ID));
            @SuppressLint("Range") double coorX = c.getDouble(c.getColumnIndex(Contract.SuperEntry.COORDX));
            @SuppressLint("Range") double coorY = c.getDouble(c.getColumnIndex(Contract.SuperEntry.COORSY));
            String localizacion = coorX+", "+coorY;
            @SuppressLint("Range") String dir = c.getString(c.getColumnIndex(Contract.SuperEntry.DIRECCION));
            @SuppressLint("Range") String prov = c.getString(c.getColumnIndex(Contract.SuperEntry.PROVINCIA));
            @SuppressLint("Range") String ciud = c.getString(c.getColumnIndex(Contract.SuperEntry.CIUDAD));
            @SuppressLint("Range") int codPer = c.getInt(c.getColumnIndex(Contract.SuperEntry.COD_CLIENTE));
            @SuppressLint("Range") String pers = c.getString(c.getColumnIndex(Contract.SuperEntry.PERSONA_CONTACTO));
            @SuppressLint("Range") String email = c.getString(c.getColumnIndex(Contract.SuperEntry.EMAIL));
            @SuppressLint("Range") int num = c.getInt(c.getColumnIndex(Contract.SuperEntry.TELEFONO));
            @SuppressLint("Range") String frecuencia = c.getString(c.getColumnIndex(Contract.SuperEntry.FRECUENCIA));
            @SuppressLint("Range") int visitasAno = c.getInt(c.getColumnIndex(Contract.SuperEntry.VISITAS_ANO));
            @SuppressLint("Range") String obs = c.getString(c.getColumnIndex(Contract.SuperEntry.OBSERVACIONES));

            Super s = new Super();
            s.setId(ID);
            s.setNombre(nombre);
            s.setCluster_id(idCluster);
            s.setLocalizacion(localizacion);
            s.setCoorX(coorX);
            s.setCoorY(coorY);
            s.setDireccion(dir);
            s.setProvincia(prov);
            s.setCiudad(ciud);
            s.setCodCliente(codPer);
            s.setPersonaContacto(pers);
            s.setEmail(email);
            s.setTelefono(num);
            s.setFrecuencia(obtenerFrecuencia(frecuencia));
            s.setVisitasAno(visitasAno);
            s.setObservaciones(obs);

            supers.add(s);

            InfoAdapter ia = new InfoAdapter("#"+ID, nombre,Color.WHITE,Color.BLACK,Color.GRAY);
            infoAdapters.add(ia);
        }



        lv = binding.lvsuper;

        adapterPersonal = new AdapterPersonal(this, infoAdapters);
        lv.setAdapter(adapterPersonal);
        lv.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (LONGCLICK){

                    infoAdapters.get(position).setColor(Color.RED);
                    adapterPersonal.notifyDataSetChanged();
                    lv.setItemChecked(position,true);

                }else{

                    it.putExtra("CLASESUPER", supers.get(position));
                    startActivity(it);
                }




            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                infoAdapters.get(position).setColor(Color.RED);
                adapterPersonal.notifyDataSetChanged();
                lv.setItemChecked(position,true);

                LONGCLICK = true;
                binding.floatingActionButtonDeleteSuper.setVisibility(View.VISIBLE);

                return false;
            }
        });

        binding.buttonAddSuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(itAddSuper);
            }
        });

        binding.floatingActionButtonDeleteSuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEliminar(db);
                LONGCLICK = false;
                binding.floatingActionButtonDeleteSuper.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Cluster eliminado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        int len = lv.getCount();
        SparseBooleanArray checked = lv.getCheckedItemPositions();

        if (checked.size() > 0) {

            for (int i = 0; i < len; i++) {
                if (checked.get(i)) {

                    infoAdapters.get(i).setColor(Color.WHITE);

                }
            }
            adapterPersonal.notifyDataSetChanged();
            lv.clearChoices();
            lv.requestLayout();
            LONGCLICK = false;
            binding.floatingActionButtonDeleteSuper.setVisibility(View.INVISIBLE);

        }else{

            Intent itMain = new Intent(this, MainActivity.class);
            startActivity(itMain);
        }
    }
}