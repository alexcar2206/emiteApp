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

import com.example.proyectoinventario.databinding.ActivityInventariosPendBinding;

import java.util.ArrayList;

public class InventariosPendActivity extends AppCompatActivity {

    ActivityInventariosPendBinding binding;

    ArrayList<Inventario> inventariosPend;
    AdapterPersonal adapterPersonal;
    ArrayList<InfoAdapter> infoAdapters;
    int idSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventariosPendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent itInv = new Intent(this, InventariosActivity.class);

        infoAdapters = new ArrayList<>();
        inventariosPend = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM "+Contract.InventarioEntry.TABLE_NAME+ " WHERE "+Contract.InventarioEntry.PENDIENTE+" = 1",null);

        while (c.moveToNext()){

            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.InventarioEntry.ID));
            @SuppressLint("Range") int idSuper = c.getInt(c.getColumnIndex(Contract.InventarioEntry.ID_SUPER));
            @SuppressLint("Range") String fecha = c.getString(c.getColumnIndex(Contract.InventarioEntry.FECHA));
            @SuppressLint("Range") String inicio = c.getString(c.getColumnIndex(Contract.InventarioEntry.INICIO));
            @SuppressLint("Range") String fin = c.getString(c.getColumnIndex(Contract.InventarioEntry.FIN));
            @SuppressLint("Range") int pend = c.getInt(c.getColumnIndex(Contract.InventarioEntry.PENDIENTE));



            Cursor cSuper = db.rawQuery("SELECT * FROM "+Contract.SuperEntry.TABLE_NAME+" WHERE "+Contract.SuperEntry.ID+" = "+idSuper, null);
            cSuper.moveToFirst();
            @SuppressLint("Range") String nombreSuper = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.NOMBRE));

            Inventario inv = new Inventario();
            inv.setId(id);
            inv.setId_super(idSuper);
            inv.setFecha(fecha);
            inv.setIncio(inicio);
            inv.setFin(fin);
            inv.setPendiente(pend);

            inventariosPend.add(inv);

            InfoAdapter ia = new InfoAdapter("#"+id, fecha, nombreSuper,Color.WHITE,Color.RED,Color.RED,Color.RED);
            infoAdapters.add(ia);

        }


        adapterPersonal = new AdapterPersonal(this,infoAdapters);
        binding.lvInvsPend.setAdapter(adapterPersonal);



        binding.lvInvsPend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Inventario inventario = inventariosPend.get(position);
                //idSelected = inventariosPend.get(position).getId();
                itInv.putExtra("INVPEND", inventario);
                startActivity(itInv);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent itMain = new Intent(this, MainActivity.class);
        startActivity(itMain);
    }
}