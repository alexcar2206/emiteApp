package com.example.proyectoinventario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.proyectoinventario.databinding.ActivityInventarioProductoBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InventarioProductoActivity extends AppCompatActivity {

    ActivityInventarioProductoBinding binding;
    String nameProd;

    ProductoInventario prodInventario;
    boolean promo = false;
    boolean expo = false;
    boolean rotura = false;



    int idProd;
    int idInvent;
    String ruta;

    final ArrayList<Imagen> imagenes = new ArrayList<>();
    ArrayList<ImageView> imageViews = new ArrayList<>();

    boolean camposVacios = false;

    ArrayList<String> promos;
    ArrayAdapter<String> adapter;



    ArrayList<Productos> productos;


    public boolean comprobarCampos(){

        if (binding.editTextNumberDecimalPrecio.getText().toString().trim().equals("")){
            camposVacios = true;
            binding.editTextNumberDecimalPrecio.setHintTextColor(Color.RED);
        }
        if (binding.editTextNumberFacing.getText().toString().equals("")){
            camposVacios = true;
            binding.editTextNumberFacing.setHintTextColor(Color.RED);
        }

        return camposVacios;

    }


    public Bitmap escalaAnchura(Bitmap bitmap, int nuevoAncho) {
        Bitmap bitmapAux= bitmap;
        if (nuevoAncho==bitmapAux.getWidth()) return bitmapAux;
        return bitmapAux.createScaledBitmap(bitmapAux, nuevoAncho, (bitmapAux.getHeight() * nuevoAncho) /
                bitmapAux.getWidth(), true);
    }



    private File guardarImagen() throws IOException {

        String nombreFoto = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File foto = File.createTempFile(nombreFoto, ".jpg", directorio);
        ruta = foto.getAbsolutePath();
        return foto;

    }




    public void Camara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager())!=null){

            File fotoArchivo = null;

            try{
                fotoArchivo = guardarImagen();

            }catch (IOException ex){
                Log.e("Error", ex.toString());
            }


            if(fotoArchivo != null)
            {
                Uri uri = FileProvider.getUriForFile(this, "com.example.myapplication.fileprovider", fotoArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }


            startActivityForResult(intent, 1); // requestCode 1 para Thumbnail
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LinearLayout ly = binding.layoutImage;
        ImageView iv = new ImageView(this);



        if (requestCode == 1 && resultCode == RESULT_OK) {// Thumbnail


            Imagen img = new Imagen();
            img.setImagen(ruta);
            img.setIdProducto(idProd);
            img.setObjeto(Imagen.Objeto.INVENTARIO);
            img.setIdObtejo(idInvent);
            imagenes.add(img);

            Bitmap imgBitmap = BitmapFactory.decodeFile(ruta);
            imgBitmap = escalaAnchura(imgBitmap, MainActivity.width/2);
            iv.setImageBitmap(imgBitmap);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
            iv.setLayoutParams(params);
            ly.addView(iv);
            imageViews.add(iv);


        }
    }



    @SuppressLint("Range")
    public void actualizarDatos(SQLiteDatabase db){

        Cursor c = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME+" WHERE "+Contract.ProductosEntry.ID+" = "+idProd, null);
        c.moveToFirst();
        nameProd = c.getString(c.getColumnIndex(Contract.ProductosEntry.NOMBRE_COMPLETO));


        binding.textViewNombreProducto.setText(nameProd);
        binding.editTextNumberDecimalPrecio.setText("");
        binding.editTextNumberFacing.setText("");
        binding.editTextTextMultiLine.setText("");
        binding.spinnerPromo.setVisibility(View.GONE);
        binding.toggleButtonProm.setChecked(false);
        binding.toggleButtonExp.setChecked(false);
        binding.toggleButtonRot.setChecked(false);

        for (int i = 0; i < imageViews.size(); i++) {
            binding.layoutImage.removeView(imageViews.get(i));
        }

        imageViews.clear();
        imagenes.clear();

        promo = false;
        expo = false;
        rotura = false;

        camposVacios = false;
    }


    public boolean saveProd(SQLiteDatabase db){

        camposVacios = false;

        if (!comprobarCampos()){
            float precio = Float.parseFloat(binding.editTextNumberDecimalPrecio.getText().toString());
            int facing = Integer.parseInt( binding.editTextNumberFacing.getText().toString());

            if (binding.toggleButtonProm.isChecked()) promo = true;
            if (binding.toggleButtonExp.isChecked()) expo = true;
            if (binding.toggleButtonRot.isChecked()) rotura = true;

            String observaciones = binding.editTextTextMultiLine.getText().toString();


            prodInventario = new ProductoInventario();
            prodInventario.setIdInventario(idInvent);
            prodInventario.setIdProducto(idProd);
            prodInventario.setPrecio(precio);
            prodInventario.setFacing(facing);
            prodInventario.setPromo(promo);
            prodInventario.setExposicion(expo);
            prodInventario.setRotura(rotura);
            prodInventario.setObservaciones(observaciones);

            int promo = 0;
            int exp = 0;
            int rotura = 0;

            if (prodInventario.isExposicion()) exp = 1;
            if (prodInventario.isPromo()) promo = 1;
            if (prodInventario.isRotura()) rotura = 1;

            db.execSQL("INSERT INTO "+Contract.ProcesoInventarioEntry.TABLE_NAME+ "("+ Contract.ProcesoInventarioEntry.ID_INVENTARIO+","+Contract.ProcesoInventarioEntry.ID_PRODUCTO+","+Contract.ProcesoInventarioEntry.PRECIO_PROD+","+Contract.ProcesoInventarioEntry.FACING_PROD+","+Contract.ProcesoInventarioEntry.PROMO_PROD+","+Contract.ProcesoInventarioEntry.OBSERVACIONES+","+Contract.ProcesoInventarioEntry.EXPOSICION+","+Contract.ProcesoInventarioEntry.ROTURA+") VALUES ("+prodInventario.getIdInventario()+","+prodInventario.getIdProducto()+","+prodInventario.getPrecio()+","+prodInventario.getFacing()+","+promo+",'"+prodInventario.getObservaciones()+"',"+exp+","+rotura+")");


            for (int i = 0; i < imagenes.size(); i++) {

                db.execSQL("INSERT INTO "+Contract.ImagenesProdcutoEntry.TABLE_NAME+"("+Contract.ImagenesProdcutoEntry.OBJETO+","+Contract.ImagenesProdcutoEntry.ID_OBJETO+","+Contract.ImagenesProdcutoEntry.ID_PRODUCTO+","+Contract.ImagenesProdcutoEntry.IMAGEN+") VALUES (\'"+imagenes.get(i).getObjeto()+"\',"+imagenes.get(i).getIdObtejo()+","+imagenes.get(i).getIdProducto()+",'"+imagenes.get(i).getImagen()+"')");
            }

            return true;
        }else{
            return false;
        }
    }



    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventarioProductoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent intent = getIntent();
        idProd = intent.getIntExtra("IDPROD",0);
        idInvent = intent.getIntExtra("IDINV", 0);
        productos = (ArrayList<Productos>) intent.getSerializableExtra("PRODUCTS");



        Cursor c = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME+" WHERE "+Contract.ProductosEntry.ID+" = "+idProd,null);

        while (c.moveToNext()){
            nameProd = c.getString(c.getColumnIndex(Contract.ProductosEntry.NOMBRE_COMPLETO));

        }

        binding.textViewNombreProducto.setText(nameProd);


        binding.buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camara();
            }
        });



        binding.buttonSaveNextProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean saved;

                saved = saveProd(db);
                int newIdProd = 0;



                if (saved){

                    for (int i = 0; i < productos.size(); i++) {

                        if (productos.get(i).getId()== idProd){

                            if (i+1 >= productos.size()){


                                Intent intentSec=new Intent();
                                setResult(RESULT_OK, intentSec);
                                finish();

                            }else{
                                newIdProd = productos.get(i+1).getId();
                                idProd = newIdProd;
                                i = productos.size();
                                actualizarDatos(db);
                            }
                        }
                    }


                }



            }
        });


        binding.buttonGuardarProductoInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (saveProd(db)){
                   Intent intentSec=new Intent();
                   setResult(RESULT_OK, intentSec);
                   finish();
               }
            }
        });


        binding.toggleButtonProm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.toggleButtonProm.isChecked()){
                    binding.spinnerPromo.setVisibility(View.VISIBLE);
                }else{
                    binding.spinnerPromo.setVisibility(View.GONE);
                }
            }
        });

        promos = new ArrayList<>();
        promos.add("2x1");
        promos.add("3x1");


        adapter = new ArrayAdapter<>(this, R.layout.spinner_item_center, promos);
        binding.spinnerPromo.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        Intent intentSec=new Intent();
        intentSec.putExtra("ONBACK", true);
        setResult(RESULT_OK, intentSec);
        finish();
    }
}