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

import com.example.proyectoinventario.databinding.ActivityClusterBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class ClusterActivity extends AppCompatActivity {

    ActivityClusterBinding binding;

    ListView lv;
    AdapterPersonal adapterPersonal;
    ArrayList<InfoAdapter> infoAdapters = new ArrayList<>();
    ArrayList<Cluster> clusters;


    boolean LONGCLICK = false;



    public void clickEliminar(SQLiteDatabase db) {

        @SuppressWarnings ("unchecked")

        AdapterPersonal adapter=(AdapterPersonal) lv.getAdapter();

        for (int i= lv.getCount() - 1; i >= 0; i--) {

            if (lv.isItemChecked(i)){
                db.execSQL("DELETE FROM "+Contract.ClusterEntry.TABLE_NAME+" WHERE "+Contract.ClusterEntry.NOMBRE+" IS \'"+clusters.get(i).getNombre()+"\'");
                infoAdapters.remove(i);
            }
        }

        lv.getCheckedItemPositions().clear();
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClusterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        lv = binding.lvcluster;


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent it = new Intent(this, ClusterInfoActivity.class);
        Intent itAddCluster = new Intent(this, ClusterAddActivity.class);



        clusters = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+Contract.ClusterEntry.TABLE_NAME,null);

        while (c.moveToNext()){
            @SuppressLint("Range") String name = c.getString(c.getColumnIndex(Contract.ClusterEntry.NOMBRE));
            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.ClusterEntry.ID));
            @SuppressLint("Range") int idCad = c.getInt(c.getColumnIndex(Contract.ClusterEntry.CAD_ID));

            Cluster cl = new Cluster();
            cl.setId(id);
            cl.setNombre(name);
            cl.setCad_id(idCad);

            clusters.add(cl);

            InfoAdapter ia = new InfoAdapter("#"+id, name,Color.WHITE,Color.BLACK,Color.GRAY);
            infoAdapters.add(ia);
        }

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

                }else {

                    it.putExtra("CLASECLUSTER", clusters.get(position));
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
                binding.floatingActionButton.setVisibility(View.VISIBLE);

                return false;
            }
        });

        binding.FloatButtonAddCluster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(itAddCluster);
            }
        });


        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEliminar(db);
                binding.floatingActionButton.setVisibility(View.INVISIBLE);
                LONGCLICK = false;
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
                    lv.setItemChecked(i,false);
                }
            }

            lv.clearChoices();
            lv.requestLayout();
            LONGCLICK = false;
            binding.floatingActionButton.setVisibility(View.INVISIBLE);

        }else{

            Intent itMain = new Intent(this, MainActivity.class);
            startActivity(itMain);
        }
    }
}