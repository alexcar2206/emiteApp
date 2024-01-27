package com.example.proyectoinventario;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityInventarioBinding;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class InventarioActivity extends AppCompatActivity {


    ActivityInventarioBinding binding;



    AdapterInventario adapterPersonal;
    ArrayList<InfoAdapterInventario> infoAdapters;



    static SQLiteDatabase db;



    ArrayList<String> productosName;
    ArrayList<Productos> productos;
    ArrayList<Productos> productosActuales = new ArrayList<>();
    ArrayAdapter<String> adapter;


    int idSelect;
    int posSelect;
    int idSuper;
    int idCluster;

    Inventario inventario;
    int idInventario;

    boolean mod;
    boolean view;


    ArrayList<Integer> idsProdOrden = new ArrayList<>();


    orderSelected orderSelect;



    public enum orderSelected{
        ALFABETIC,
        CATEGORY,
        PERSONAL
    }




    public void opcionOk(int id) {
        
        db.execSQL("INSERT INTO "+Contract.ProductoClusterEntry.TABLE_NAME+ "("+Contract.ProductoClusterEntry.ID_PRODUCTO+","+Contract.ProductoClusterEntry.ID_CLUSTER+") VALUES ("+id+","+ idCluster+")");
        Toast.makeText(this, "Producto añadido", Toast.LENGTH_SHORT).show();

        if (view){
            actualizarProdsView();
        }else{
            actualizarProds();
        }
    }
    public void opcionCancel(String mensaje) {

        Toast.makeText(this, ""+mensaje, Toast.LENGTH_SHORT).show();
    }


    public void orderByCat(){

        orderSelect = orderSelected.CATEGORY;

        productos.clear();
        productosName.clear();
        infoAdapters.clear();

        Cursor c = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME+ " WHERE "+Contract.ProductosEntry.ID+" IN ( SELECT "+Contract.ProductoClusterEntry.ID_PRODUCTO +" FROM "+Contract.ProductoClusterEntry.TABLE_NAME+" WHERE "+Contract.ProductoClusterEntry.ID_CLUSTER+" = "+idCluster+")  AND "+Contract.ProductosEntry.ID+ " NOT IN ( SELECT "+Contract.ProcesoInventarioEntry.ID_PRODUCTO+" FROM "+Contract.ProcesoInventarioEntry.TABLE_NAME+" WHERE "+Contract.ProcesoInventarioEntry.ID_INVENTARIO+" = "+idInventario+")" + " ORDER BY "+Contract.ProductosEntry.FAMILIA_ID+" ASC",null);

        while (c.moveToNext()){

            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.ProductosEntry.ID));
            @SuppressLint("Range") String name = c.getString(c.getColumnIndex(Contract.ProductosEntry.NOMBRE));
            if (name.length()>35) name = name.substring(0,33)+"..";



            productosName.add(name);

            Productos pr = new Productos();
            pr.setId(id);
            pr.setNombre(name);
            productos.add(pr);

            InfoAdapterInventario ia = new InfoAdapterInventario(name,R.drawable.ic_check,View.GONE);
            infoAdapters.add(ia);
        }

        productosActuales.clear();
        productosActuales.addAll(productos);



       // adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, productosName);
        //binding.lvInventario.setAdapter(adapter);
        adapterPersonal = new AdapterInventario(this, infoAdapters);
        binding.lvInventario.setAdapter(adapterPersonal);
        adapterPersonal.notifyDataSetChanged();
        //adapter.notifyDataSetChanged();

    }


    public void orderByAlf(){


        orderSelect = orderSelected.ALFABETIC;

        productos.clear();
        productosName.clear();
        infoAdapters.clear();

        Cursor c = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME+ " WHERE "+Contract.ProductosEntry.ID+" IN ( SELECT "+Contract.ProductoClusterEntry.ID_PRODUCTO +" FROM "+Contract.ProductoClusterEntry.TABLE_NAME+" WHERE "+Contract.ProductoClusterEntry.ID_CLUSTER+" = "+idCluster+") AND "+Contract.ProductosEntry.ID+ " NOT IN ( SELECT "+Contract.ProcesoInventarioEntry.ID_PRODUCTO+" FROM "+Contract.ProcesoInventarioEntry.TABLE_NAME+" WHERE "+Contract.ProcesoInventarioEntry.ID_INVENTARIO+" = "+idInventario+")"+ " ORDER BY "+Contract.ProductosEntry.NOMBRE +" ASC",null);

        while (c.moveToNext()){

            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.ProductosEntry.ID));
            @SuppressLint("Range") String name = c.getString(c.getColumnIndex(Contract.ProductosEntry.NOMBRE));
            if (name.length()>35) name = name.substring(0,33)+"..";




            productosName.add(name);

            Productos pr = new Productos();
            pr.setId(id);
            pr.setNombre(name);
            productos.add(pr);


            InfoAdapterInventario ia = new InfoAdapterInventario(name,R.drawable.ic_check,View.GONE);
            infoAdapters.add(ia);
        }

        productosActuales.clear();
        productosActuales.addAll(productos);


        //adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, productosName);
        //binding.lvInventario.setAdapter(adapter);

        adapterPersonal = new AdapterInventario(this, infoAdapters);
        binding.lvInventario.setAdapter(adapterPersonal);
        adapterPersonal.notifyDataSetChanged();
        //adapter.notifyDataSetChanged();
    }

    public void orderByPers(){

        orderSelect = orderSelected.PERSONAL;

        productos.clear();
        productosName.clear();
        infoAdapters.clear();

        ArrayList<Integer> idsProds = new ArrayList<>();

        Cursor cProd = db.rawQuery("SELECT "+Contract.OrdenSuperInventario.ID_PROD +" FROM "+Contract.OrdenSuperInventario.TABLE_NAME+" WHERE "+Contract.OrdenSuperInventario.ID_SUPER+" = "+idSuper+ " AND "+Contract.OrdenSuperInventario.ID_PROD+ " NOT IN ( SELECT "+Contract.ProcesoInventarioEntry.ID_PRODUCTO+" FROM "+Contract.ProcesoInventarioEntry.TABLE_NAME+" WHERE "+Contract.ProcesoInventarioEntry.ID_INVENTARIO+" = "+idInventario+")",null);

        while (cProd.moveToNext()){

            @SuppressLint("Range") int id = cProd.getInt(cProd.getColumnIndex(Contract.OrdenSuperInventario.ID_PROD));
            idsProds.add(id);
        }


        for (int i = 0; i < idsProds.size(); i++) {
            Cursor c = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME+ " WHERE "+Contract.ProductosEntry.ID+" = "+idsProds.get(i),null);

            while (c.moveToNext()){

                @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.ProductosEntry.ID));
                @SuppressLint("Range") String name = c.getString(c.getColumnIndex(Contract.ProductosEntry.NOMBRE));
                if (name.length()>35) name = name.substring(0,33)+"..";



                productosName.add(name);

                Productos pr = new Productos();
                pr.setId(id);
                pr.setNombre(name);
                productos.add(pr);

                InfoAdapterInventario ia = new InfoAdapterInventario(name,R.drawable.ic_check,View.GONE);
                infoAdapters.add(ia);
            }
        }

        productosActuales.clear();
        productosActuales.addAll(productos);



        //adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, productosName);
        //binding.lvInventario.setAdapter(adapter);


        adapterPersonal = new AdapterInventario(this, infoAdapters);
        binding.lvInventario.setAdapter(adapterPersonal);
        adapterPersonal.notifyDataSetChanged();
        //adapter.notifyDataSetChanged();
    }

    public void actualizarProds(){


        switch (orderSelect){

            case ALFABETIC:
                orderByAlf();
                break;

            case CATEGORY:
                orderByCat();
                break;

            case PERSONAL:
                orderByPers();
                break;
        }

    }



    @SuppressLint("Range")
    public void actualizarProdsView(){

        productos.clear();
        productosName.clear();
        infoAdapters.clear();


        ArrayList<Productos> productosSelected = new ArrayList<>();

        Cursor cAll = null;


        switch (orderSelect){

            case ALFABETIC:
                cAll = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME+ " WHERE "+Contract.ProductosEntry.ID+" IN ( SELECT "+Contract.ProductoClusterEntry.ID_PRODUCTO +" FROM "+Contract.ProductoClusterEntry.TABLE_NAME+" WHERE "+Contract.ProductoClusterEntry.ID_CLUSTER+" = "+idCluster+")"+ " ORDER BY "+Contract.ProductosEntry.NOMBRE +" ASC",null);
                break;

            case CATEGORY:
                cAll = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME+ " WHERE "+Contract.ProductosEntry.ID+" IN ( SELECT "+Contract.ProductoClusterEntry.ID_PRODUCTO +" FROM "+Contract.ProductoClusterEntry.TABLE_NAME+" WHERE "+Contract.ProductoClusterEntry.ID_CLUSTER+" = "+idCluster+")"  + " ORDER BY "+Contract.ProductosEntry.FAMILIA_ID+" ASC",null);
                break;

            case PERSONAL:
                cAll = db.rawQuery(" SELECT * FROM "+Contract.OrdenSuperInventario.TABLE_NAME+" WHERE "+Contract.OrdenSuperInventario.ID_SUPER+" = "+idSuper,null);
                break;
        }



        while (cAll.moveToNext()){

            String name = "";
            int id = 0;



            if(orderSelect == orderSelected.PERSONAL){


                id = cAll.getInt(cAll.getColumnIndex(Contract.OrdenSuperInventario.ID_PROD));

                Cursor cName = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME+" WHERE "+Contract.ProductosEntry.ID+" = "+id, null);
                cName.moveToFirst();

                name = cName.getString(cName.getColumnIndex(Contract.ProductosEntry.NOMBRE));

            }else{

                id = cAll.getInt(cAll.getColumnIndex(Contract.ProductosEntry.ID));
                name = cAll.getString(cAll.getColumnIndex(Contract.ProductosEntry.NOMBRE));
            }



            if (name.length()>35) name = name.substring(0,33)+"..";


            productosName.add(name);

            Productos pr = new Productos();
            pr.setId(id);
            pr.setNombre(name);
            productos.add(pr);

            InfoAdapterInventario ia = new InfoAdapterInventario(name,R.drawable.ic_check, View.VISIBLE);
            infoAdapters.add(ia);
        };


        //adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_checked, productosName);
        //binding.lvInventario.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE );


        adapterPersonal = new AdapterInventario(this, infoAdapters);
        binding.lvInventario.setAdapter(adapterPersonal);
        //binding.lvInventario.setAdapter(adapter);




        Cursor cSelected = db.rawQuery("SELECT * FROM "+Contract.ProcesoInventarioEntry.TABLE_NAME+" WHERE "+Contract.ProcesoInventarioEntry.ID_INVENTARIO+" = "+idInventario, null);

        while (cSelected.moveToNext()){

            @SuppressLint("Range") int id = cSelected.getInt(cSelected.getColumnIndex(Contract.ProcesoInventarioEntry.ID_PRODUCTO));

            Productos pr = new Productos();
            pr.setId(id);
            productosSelected.add(pr);

        }





        productosActuales.clear();

        boolean selected;

        for (int i = 0; i < binding.lvInventario.getCount(); i++) {

            selected = false;

            for (int j = 0; j < productosSelected.size(); j++) {

                if (productos.get(i).getId()==productosSelected.get(j).getId()){
                    infoAdapters.get(i).setImageCheck(R.drawable.ic_check_on);
                    adapterPersonal.notifyDataSetChanged();
                    selected = true;

                }
            }

            if (!selected){

                productosActuales.add(productos.get(i));
            }

        }



        //adapter.notifyDataSetChanged();
        adapterPersonal.notifyDataSetChanged();
    }



    ActivityResultLauncher<Intent> launcher=registerForActivityResult(new
            ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode()==RESULT_OK){
                Intent intent=result.getData();
                boolean ONBACK = intent.getBooleanExtra("ONBACK",false);

                if (!ONBACK){
                    idsProdOrden.add(productosActuales.get(posSelect).getId());
                }

                if (view){
                    actualizarProdsView();
                }else{
                    actualizarProds();
                }


            }
        }
    });





    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        view = false;


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        db = dbHelper.getWritableDatabase();




        Intent intent = getIntent();
        idSuper = intent.getIntExtra("IDSUPER", 0);
        inventario = (Inventario) intent.getSerializableExtra("CLASEINVENTARIO");
        mod = intent.getBooleanExtra("MOD", false);

        Intent itProd = new Intent(this, InventarioProductoActivity.class);
        Intent it = new Intent(this, MainActivity.class);
        Intent itFinal = new Intent(this, FinalInventarioActivity.class);






        //Insertar/obtener id de Inventario
        if (!mod){
            db.execSQL("INSERT INTO "+ Contract.InventarioEntry.TABLE_NAME+" ("+Contract.InventarioEntry.ID_SUPER+","+Contract.InventarioEntry.FECHA+","+Contract.InventarioEntry.INICIO+","+Contract.InventarioEntry.PENDIENTE+","+Contract.InventarioEntry.ID_USUARIO+") VALUES ("+ inventario.getId_super()+",'"+inventario.getFecha()+"','"+inventario.getIncio()+"',"+inventario.getPendiente()+","+inventario.getIdUser()+")");
            Cursor cIdInv = db.rawQuery("SELECT * FROM "+ Contract.InventarioEntry.TABLE_NAME+ " ORDER BY "+Contract.InventarioEntry.ID+" DESC",null);
            cIdInv.moveToFirst();
            idInventario = cIdInv.getInt(cIdInv.getColumnIndex(Contract.InventarioEntry.ID));
        }else{
            idInventario = inventario.getId();
        }




        //Obtener cluster del supermercado
        Cursor c2 = db.rawQuery("SELECT * FROM "+Contract.SuperEntry.TABLE_NAME+ " WHERE "+Contract.SuperEntry.ID+" = "+idSuper,null);

        while (c2.moveToNext()){
             idCluster = c2.getInt(c2.getColumnIndex(Contract.SuperEntry.CLUSTER_ID));
        }



        //Rellenar listview con productos
        orderSelect = orderSelected.CATEGORY;
        productosName = new ArrayList<>();
        productos = new ArrayList<>();
        infoAdapters = new ArrayList<>();
        actualizarProds();



        //Seleccionar un producto
        binding.lvInventario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                for (int i = 0; i < productosActuales.size(); i++) {
                    if (productosActuales.get(i).getId()==productos.get(position).getId()){
                        idSelect = productosActuales.get(i).getId();
                        posSelect = i;
                        itProd.putExtra("IDPROD", idSelect);
                        itProd.putExtra("IDINV", idInventario);
                        itProd.putExtra("PRODUCTS", productosActuales);
                        launcher.launch(itProd);
                    };
                }


            }
        });


        //Finalizar inventario
        binding.buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itFinal.putExtra("INVENTARIO" , inventario);
                itFinal.putExtra("IDINVENTARIO", idInventario);
                itFinal.putExtra("PRODUCTOS", productos);
                itFinal.putExtra("IDPRODUCTOS", idsProdOrden);
                itFinal.putExtra("IDSUPER", idSuper);

                startActivity(itFinal);

            }
        });




        //Ordenar alfabeticamente
        binding.imageButtonAlf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                orderSelect = orderSelected.ALFABETIC;

                if (view){
                    actualizarProdsView();
                }else{
                    actualizarProds();
                }

                binding.imageButtonAlf.setBackgroundResource(R.drawable.filtrer);
                binding.imageButtonAlf.setColorFilter(Color.rgb(15,103,172));

                binding.imageButtonCateg.setBackgroundColor( getResources().getColor(R.color.filtrer));
                binding.imageButtonCateg.setColorFilter(Color.WHITE);

                binding.imageButtonPers.setBackgroundColor( getResources().getColor(R.color.filtrer));
                binding.imageButtonPers.setColorFilter(Color.WHITE);



            }
        });



        //Ordenar por categoria
        binding.imageButtonCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                orderSelect = orderSelected.CATEGORY;

                if (view){
                    actualizarProdsView();
                }else{
                    actualizarProds();
                }

                binding.imageButtonCateg.setBackgroundResource(R.drawable.filtrer);
                binding.imageButtonCateg.setColorFilter(Color.rgb(15,103,172));

                binding.imageButtonAlf.setBackgroundColor( getResources().getColor(R.color.filtrer));
                binding.imageButtonAlf.setColorFilter(Color.WHITE);

                binding.imageButtonPers.setBackgroundColor( getResources().getColor(R.color.filtrer));
                binding.imageButtonPers.setColorFilter(Color.WHITE);


            }
        });



        //Ordenar por orden personal
        binding.imageButtonPers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                orderSelect = orderSelected.PERSONAL;

                if (view){
                    actualizarProdsView();
                }else{
                    actualizarProds();
                }

                binding.imageButtonPers.setBackgroundResource(R.drawable.filtrer);
                binding.imageButtonPers.setColorFilter(Color.rgb(15,103,172));

                binding.imageButtonAlf.setBackgroundColor( getResources().getColor(R.color.filtrer));
                binding.imageButtonAlf.setColorFilter(Color.WHITE);

                binding.imageButtonCateg.setBackgroundColor( getResources().getColor(R.color.filtrer));
                binding.imageButtonCateg.setColorFilter(Color.WHITE);

            }
        });



        //Ver todos los productos
        binding.imageButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!view){
                    actualizarProdsView();
                    view = true;
                    binding.imageButtonView.setImageResource(R.drawable.ic_view);
                }else{
                    actualizarProds();
                    view = false;
                    binding.imageButtonView.setImageResource(R.drawable.ic_viewoff);

                }
            }
        });



        binding.imageButtonAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialogAddProd alertDialogAddProd = new AlertDialogAddProd();
                alertDialogAddProd.show(getSupportFragmentManager(), "Tag");

            }
        });

    }


}