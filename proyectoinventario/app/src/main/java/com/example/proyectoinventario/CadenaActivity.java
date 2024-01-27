package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityCadenaBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class CadenaActivity extends AppCompatActivity {


    ArrayList<Cadena> cadenas;
    ActivityCadenaBinding binding;

    AdapterPersonal adapterPersonal;
    ArrayList<InfoAdapter> infoAdapters;
    ListView lv;

    boolean LONGCLICK = false;



    public void clickEliminar(SQLiteDatabase db) {

        @SuppressWarnings ("unchecked")

        AdapterPersonal adapterPersonal = (AdapterPersonal) lv.getAdapter();

        for (int i= lv.getCount() - 1; i >= 0; i--) {

            if (lv.isItemChecked(i)){
                db.execSQL("DELETE FROM "+Contract.CadenaEntry.TABLE_NAME+" WHERE "+Contract.CadenaEntry.NOMBRE+" IS \'"+cadenas.get(i).getNombre()+"\'");
                infoAdapters.remove(i);
            }
        }

        lv.getCheckedItemPositions().clear();
        adapterPersonal.notifyDataSetChanged();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadenaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();



        Cursor c = db.rawQuery("SELECT * FROM "+Contract.CadenaEntry.TABLE_NAME,null);
        cadenas = new ArrayList<>();


        Intent it = new Intent(this, CadenaAddActivity.class);

        lv = binding.lvcadenas;
        infoAdapters = new ArrayList<InfoAdapter>();

        if (db != null){

            while(c.moveToNext()){
                @SuppressLint("Range") String name = c.getString(c.getColumnIndex(Contract.CadenaEntry.NOMBRE));
                @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.CadenaEntry.ID));

                Cadena cad = new Cadena();
                cad.setId(id);
                cad.setNombre(name);
                cadenas.add(cad);

                InfoAdapter ia = new InfoAdapter("#"+id,name,Color.WHITE,Color.BLACK,Color.GRAY);
                infoAdapters.add(ia);
            }
        }



        adapterPersonal = new AdapterPersonal(this,infoAdapters);
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

                    //Toast.makeText(CadenaActivity.this, ""+lv.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

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
                binding.floatingActionButtonDelete.setVisibility(View.VISIBLE);
                return false;
            }
        });

        binding.floatingActionButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEliminar(db);
                binding.floatingActionButtonDelete.setVisibility(View.INVISIBLE);
                LONGCLICK = false;
                Toast.makeText(getApplicationContext(), "Cadena/s eliminada", Toast.LENGTH_SHORT).show();


            }
        });

        binding.buttonAddCadena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(it);
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
                    lv.setItemChecked(i,false);
                }
            }

            lv.clearChoices();
            lv.requestLayout();
            LONGCLICK = false;
            binding.floatingActionButtonDelete.setVisibility(View.INVISIBLE);

        }else{

            Intent itMain = new Intent(this, MainActivity.class);
            startActivity(itMain);
        }
    }
}