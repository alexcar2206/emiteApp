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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityFamiliaBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class FamiliaActivity extends AppCompatActivity {

    ActivityFamiliaBinding binding;


    ListView lv;
    ArrayList<Familia> familias;


    AdapterPersonal adapterPersonal;
    ArrayList<InfoAdapter> infoAdapters;
    boolean LONGCLICK = false;



    public void clickEliminar(SQLiteDatabase db) {

        @SuppressWarnings ("unchecked")

        AdapterPersonal adapterPersonal = (AdapterPersonal) lv.getAdapter();

        for (int i= lv.getCount() - 1; i >= 0; i--) {

            if (lv.isItemChecked(i)){
                db.execSQL("DELETE FROM "+Contract.CadenaEntry.TABLE_NAME+" WHERE "+Contract.CadenaEntry.NOMBRE+" IS \'"+familias.get(i).getNombre()+"\'");
                infoAdapters.remove(i);
            }
        }

        lv.getCheckedItemPositions().clear();

        adapterPersonal.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamiliaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent it = new Intent(this, FamiliaInfoActivity.class);
        Intent itAdd = new Intent(this, FamiliaAddActivity.class);

        lv = binding.lvfamilia;
        familias = new ArrayList<>();



        infoAdapters = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM "+Contract.FamiliaEntry.TABLE_NAME,null);

        while (c.moveToNext()){
            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.FamiliaEntry.ID));
            @SuppressLint("Range") String nombre = c.getString(c.getColumnIndex(Contract.FamiliaEntry.NOMBRE));

            Familia fl = new Familia();
            fl.setId(id);
            fl.setNombre(nombre);
            familias.add(fl);


            InfoAdapter ia = new InfoAdapter("#"+id, nombre, Color.WHITE,Color.BLACK,Color.GRAY);
            infoAdapters.add(ia);

        }


        adapterPersonal = new AdapterPersonal(this, infoAdapters);
        lv.setAdapter(adapterPersonal);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (LONGCLICK){

                    infoAdapters.get(position).setColor(Color.RED);
                    adapterPersonal.notifyDataSetChanged();
                    lv.setItemChecked(position,true);

                }else{

                    it.putExtra("CLASSFAMILIA", familias.get(position));
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
                binding.btnDeleteFamilia.setVisibility(View.VISIBLE);

                return false;
            }
        });


        binding.btnAddFamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(itAdd);
            }
        });

       binding.btnDeleteFamilia.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               clickEliminar(db);
               binding.btnDeleteFamilia.setVisibility(View.GONE);
               LONGCLICK = false;
               Toast.makeText(getApplicationContext(), "Familia/s eliminada/s", Toast.LENGTH_SHORT).show();
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
            binding.btnDeleteFamilia.setVisibility(View.GONE);

        }else{

            Intent itMain = new Intent(this, MainActivity.class);
            startActivity(itMain);
        }
    }
}