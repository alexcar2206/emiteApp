package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityInventariosBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class InventariosActivity extends AppCompatActivity {


    ActivityInventariosBinding binding;

    SQLiteDatabase db;
    Inventario inv;
    boolean inventPend;
    boolean delete;
    boolean VIEWPAGER;

    Intent itMain;

    ArrayList<ImageView> images = new ArrayList<>();
    public void opcionOk(String mensaje) {
        delete = true;
        Intent itInvs = new Intent(this, InventariosPendFinActivity.class);
        Toast.makeText(this, ""+mensaje, Toast.LENGTH_SHORT).show();
        db.execSQL("DELETE FROM "+Contract.InventarioEntry.TABLE_NAME+ " WHERE "+Contract.InventarioEntry.ID+" IS "+inv.getId());
        db.execSQL("DELETE  FROM "+Contract.ProcesoInventarioEntry.TABLE_NAME+" WHERE "+Contract.ProcesoInventarioEntry.ID_INVENTARIO+" IS "+inv.getId());
        db.execSQL("DELETE  FROM "+Contract.ImagenesProdcutoEntry.TABLE_NAME+ " WHERE "+ Contract.ImagenesProdcutoEntry.OBJETO + " IS \'"+Imagen.Objeto.INVENTARIO+"\' AND "+Contract.ImagenesProdcutoEntry.ID_OBJETO+" IS "+inv.getId());
        startActivity(itInvs);
    }
    public void opcionCancel(String mensaje) {
        delete = false;
        Toast.makeText(this, ""+mensaje, Toast.LENGTH_SHORT).show();
    }


    public Bitmap escalaAnchura(Bitmap bitmap, int nuevoAncho) {
        Bitmap bitmapAux= bitmap;
        if (nuevoAncho==bitmapAux.getWidth()) return bitmapAux;
        return bitmapAux.createScaledBitmap(bitmapAux, nuevoAncho, (bitmapAux.getHeight() * nuevoAncho) /
                bitmapAux.getWidth(), true);
    }


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        db = dbHelper.getWritableDatabase();


        Intent intent = getIntent();
        Inventario InvPend = (Inventario) intent.getSerializableExtra("INVPEND");
        Inventario InvFin = (Inventario) intent.getSerializableExtra("INVFIN");
        VIEWPAGER = intent.getBooleanExtra("VIEWPAGER", false);
        Intent itInvent = new Intent(this, InventarioActivity.class);
        itMain = new Intent(this, MainActivity.class);



        inventPend = true;
        delete = false;

        inv = new Inventario();

        if (InvPend!=null){
           inv = InvPend;
        }

        if (InvFin!= null){
            inventPend = false;
            inv = InvFin;
        }


        String nombreSuper = "";

        Cursor cSuper = db.rawQuery("SELECT * FROM "+Contract.SuperEntry.TABLE_NAME+" WHERE "+Contract.SuperEntry.ID+ " = "+inv.getId_super(), null);
        cSuper.moveToFirst();
        nombreSuper = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.NOMBRE));


        binding.textViewFechaInv.setText(inv.getFecha());
        binding.textViewSuper.setText(nombreSuper);
        binding.textViewIni.setText(inv.getIncio());
        binding.textViewFin.setText(inv.getFin());

        if (inventPend){
            binding.textView70.setVisibility(View.GONE);
        }

        binding.buttonEditInv.setVisibility(View.VISIBLE);
        binding.buttonRemoveInv.setVisibility(View.VISIBLE);


        binding.buttonEditInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itInvent.putExtra("MOD", true);
                itInvent.putExtra("CLASEINVENTARIO",inv);
                itInvent.putExtra("IDSUPER",inv.getId_super());
                startActivity(itInvent);
            }
        });



        ArrayList<Integer> idsProd = new ArrayList<>();
        ArrayList<String> rutas = new ArrayList<>();
        ArrayList<Imagen> imagenes = new ArrayList<>();
        Cursor c2 = db.rawQuery("SELECT * FROM "+Contract.ImagenesProdcutoEntry.TABLE_NAME+" WHERE "+Contract.ImagenesProdcutoEntry.OBJETO+" = \'"+Imagen.Objeto.INVENTARIO+"\' AND "+Contract.ImagenesProdcutoEntry.ID_OBJETO+" = "+inv.getId(),null);

        while (c2.moveToNext()){
            @SuppressLint("Range") String ruta = c2.getString(c2.getColumnIndex(Contract.ImagenesProdcutoEntry.IMAGEN));
            @SuppressLint("Range") int idProd = c2.getInt(c2.getColumnIndex(Contract.ImagenesProdcutoEntry.ID_PRODUCTO));
            @SuppressLint("Range") String fin = c2.getString(c2.getColumnIndex(Contract.ImagenesProdcutoEntry.FOTOSFIN));

            Imagen ig = new Imagen();
            ig.setObjeto(Imagen.Objeto.INVENTARIO);
            ig.setIdObtejo(inv.getId());
            ig.setIdProducto(idProd);
            ig.setFotosFin(fin);
            ig.setImagen(ruta);

            imagenes.add(ig);

            rutas.add(ruta);
            idsProd.add(idProd);
        }

        if (rutas.size()==0){

            binding.textView65.setVisibility(View.GONE);

        }else{

            for (int i = 0; i < imagenes.size(); i++) {

                LinearLayout ly = binding.layoutImageInv;
                ImageView iv = new ImageView(this);
                iv.setTag(imagenes.get(i).getIdProducto());
                Bitmap imgBitmap = BitmapFactory.decodeFile(imagenes.get(i).getImagen());
                imgBitmap = escalaAnchura(imgBitmap, MainActivity.width/2);
                iv.setImageBitmap(imgBitmap);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                iv.setLayoutParams(params);
                ly.addView(iv);
                images.add(iv);
            }
        }


        Intent itProdInf = new Intent(this, ProductoInfoInventActivity.class);

        for (int i = 0; i < images.size(); i++) {

            int pos = i;

            images.get(pos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idProd = Integer.parseInt(images.get(pos).getTag().toString());

                    idProd = imagenes.get(pos).getIdProducto();
                    String fotosFin = imagenes.get(pos).getFotosFin();

                    itProdInf.putExtra("IDINV", inv.getId());

                    if (fotosFin!= null && fotosFin.equals(Imagen.fotosFinales.SI)){
                        itProdInf.putExtra("FOTOSFIN", true);

                    }else{
                        itProdInf.putExtra("FOTOSFIN", false);
                        itProdInf.putExtra("IDPROD",idProd);
                    }


                    startActivity(itProdInf);
                }
            });
        }


        binding.buttonRemoveInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogoAlerta dAlerta = new DialogoAlerta ();
                dAlerta.show(getSupportFragmentManager(), "Alerta");

            }
        });




    }



    @Override
    public void onBackPressed() {

        if (inventPend&&!VIEWPAGER){
            Intent itInvPend = new Intent(this, InventariosPendActivity.class);
            startActivity(itInvPend);
        }else{
            Intent itInvs = new Intent(this, InventariosPendFinActivity.class);
            startActivity(itInvs);
        }

    }
}