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
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityProductosBinding;

import java.util.ArrayList;

public class ProductosActivity extends AppCompatActivity {

    ActivityProductosBinding binding;


    AdapterPersonal adapterPersonal;
    ArrayList<InfoAdapter> infoAdapters = new ArrayList<>();

    ArrayList<Productos> productos;
    boolean LONGCLICK = false;
    ListView lv;



    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


    public void clickEliminar(SQLiteDatabase db) {

        @SuppressWarnings ("unchecked")

        AdapterPersonal adapter=(AdapterPersonal) lv.getAdapter();

        for (int i= lv.getCount() - 1; i >= 0; i--) {

            if (lv.isItemChecked(i)){
                db.execSQL("DELETE FROM "+Contract.ProductosEntry.TABLE_NAME+" WHERE "+Contract.ProductosEntry.NOMBRE_COMPLETO +" IS \'"+ productos.get(i).getNombre()+"\'");
                infoAdapters.remove(i);
            }
        }

        lv.getCheckedItemPositions().clear();
        adapter.notifyDataSetChanged();
    }


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();




        Intent it = new Intent(this, ProductoInfoActivity.class);
        Intent itAdd = new Intent(this, ProductosAddActivity.class);




        lv = binding.lvprod;
        productos = new ArrayList<>();
        ArrayList<Integer> idClusters = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME,null);

        while (c.moveToNext()){

            @SuppressLint("Range") int idProd = c.getInt(c.getColumnIndex(Contract.ProductosEntry.ID));
            @SuppressLint("Range") String nombre = c.getString(c.getColumnIndex(Contract.ProductosEntry.NOMBRE_COMPLETO));
            String[] nombres = nombre.split(" | ");
            nombre = "";
            for (int i = 4; i < nombres.length; i++) {
                nombre = nombre + nombres[i]+" ";
            }

            @SuppressLint("Range") int idFam = c.getInt(c.getColumnIndex(Contract.ProductosEntry.FAMILIA_ID));


            Cursor c2 = db.rawQuery(" SELECT "+ Contract.ProductoClusterEntry.ID_CLUSTER+" FROM "+Contract.ProductoClusterEntry.TABLE_NAME+" WHERE "+Contract.ProductoClusterEntry.ID_PRODUCTO+ " = "+idProd,null);

            while (c2.moveToNext()){

                @SuppressLint("Range") int idCluster = c2.getInt(0);
                idClusters.add(idCluster);
            }

            int[] idClustersArray = new int[idClusters.size()];

            if (idClusters.size() > 0){

                for (int i = 0; i < idClustersArray.length; i++) {
                    idClustersArray[i] = idClusters.get(i);
                }
            }




            Productos pr = new Productos();

            pr.setId(idProd);
            pr.setNombre(nombre);
            pr.setFamiliaID(idFam);
            pr.setIdCluster(idClustersArray);

            productos.add(pr);

            idClusters.clear();

            InfoAdapter ia = new InfoAdapter("#"+idProd,nombre,Color.WHITE,Color.BLACK,Color.GRAY);
            infoAdapters.add(ia);


        }

        adapterPersonal = new AdapterPersonal(this, infoAdapters);
        lv.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
        lv.setAdapter(adapterPersonal);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (LONGCLICK){

                    infoAdapters.get(position).setColor(Color.RED);
                    adapterPersonal.notifyDataSetChanged();
                    lv.setItemChecked(position,true);
                }else{
                    it.putExtra("CLASSPRODUCTO", productos.get(position));
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
                binding.floatingActionButtonDeleteProd.setVisibility(View.VISIBLE);

                return false;
            }
        });


        binding.floatingActionButtonDeleteProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEliminar(db);
                LONGCLICK = false;
                binding.floatingActionButtonDeleteProd.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        binding.floatingActionButtonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(itAdd);
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
            binding.floatingActionButtonDeleteProd.setVisibility(View.INVISIBLE);

        }else{

            Intent itMain = new Intent(this, MainActivity.class);
            startActivity(itMain);
        }
    }
}