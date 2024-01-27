package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyectoinventario.databinding.ActivityFamiliaInfoBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FamiliaInfoActivity extends AppCompatActivity {

    ActivityFamiliaInfoBinding binding;
    Familia fl;


    final ArrayList<String> listaSubfamilias = new ArrayList<>();
    final HashMap<String,Integer> idSubfamilia = new HashMap<>();
    int subfamiliaSelected;

    ArrayAdapter<String> adapterSubfamilias;

    ListView lvSubfamilias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamiliaInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        lvSubfamilias = binding.lvfaminfo;


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent intent = getIntent();
        fl = (Familia) intent.getSerializableExtra("CLASSFAMILIA");
        Intent it = new Intent(this, FamiliaInfoModActivity.class);
        Intent itSubfamilia = new Intent(this, FamiliaInfoModSubfamiliaActivity.class);



        binding.textView23.setText(fl.getNombre());


        Cursor cSubFam = db.rawQuery(" SELECT * FROM "+Contract.SubfamiliaEntry.TABLE_NAME+" WHERE "+Contract.SubfamiliaEntry.ID_FAMILIA+" = "+fl.getId(), null);

        while (cSubFam.moveToNext()){
            @SuppressLint("Range") int id = cSubFam.getInt(cSubFam.getColumnIndex(Contract.SubfamiliaEntry.ID));
            @SuppressLint("Range") String name = cSubFam.getString(cSubFam.getColumnIndex(Contract.SubfamiliaEntry.NOMBRE));
            listaSubfamilias.add(name);
            idSubfamilia.put(name,id);
        }


        if (listaSubfamilias.size() > 0){

            adapterSubfamilias =new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaSubfamilias);
            lvSubfamilias.setAdapter(adapterSubfamilias);
        }else{
            lvSubfamilias.setVisibility(View.GONE);
            binding.textView37.setVisibility(View.GONE);
        }



        binding.lvfaminfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subfamiliaSelected = idSubfamilia.get(listaSubfamilias.get(position));
                itSubfamilia.putExtra("IDSUBFAMILIA",subfamiliaSelected);
                startActivity(itSubfamilia);
            }
        });


        binding.buttonModFam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                it.putExtra("CLASSFAMILIA", fl);
                it.putExtra("NOMBRESUBFAMILIAS", listaSubfamilias);
                startActivity(it);

            }
        });

    }
}