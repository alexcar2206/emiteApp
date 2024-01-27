package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityProductoInfoInventBinding;

import java.util.ArrayList;

public class ProductoInfoInventActivity extends AppCompatActivity {


    ActivityProductoInfoInventBinding binding;

    int idProd;
    int idInv;

    boolean FOTOSFIN;


    ArrayList<String> rutas;
    ArrayList<ImageView> images;
    ImageView ivSelect;




    public void obsFin(){

        binding.textViewNom.setVisibility(View.GONE);
        binding.textViewExp.setVisibility(View.GONE);
        binding.textViewRot.setVisibility(View.GONE);
        binding.textViewProm.setVisibility(View.GONE);
        binding.textViewFac.setVisibility(View.GONE);
        binding.textViewPrec.setVisibility(View.GONE);
        binding.textView63.setVisibility(View.GONE);
        binding.textView75.setVisibility(View.GONE);
        binding.textView77.setVisibility(View.GONE);
        binding.textView79.setVisibility(View.GONE);
        binding.textView81.setVisibility(View.GONE);
        binding.textView83.setVisibility(View.GONE);

    }

    public String intToString(int num){

        if (num == 0){
            return "No";
        }else{
            return "Sí";
        }
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
        binding = ActivityProductoInfoInventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Intent intent = getIntent();


        idInv = intent.getIntExtra("IDINV",0);
        FOTOSFIN = intent.getBooleanExtra("FOTOSFIN",false);
        rutas = new ArrayList<>();

        if (!FOTOSFIN){

            binding.textViewObsFin.setVisibility(View.GONE);

            idProd = intent.getIntExtra("IDPROD",0);

            Cursor c = db.rawQuery(" SELECT * FROM "+Contract.ProcesoInventarioEntry.TABLE_NAME+" WHERE "+Contract.ProcesoInventarioEntry.ID_INVENTARIO+" = "+idInv+" AND "+Contract.ProcesoInventarioEntry.ID_PRODUCTO+" = "+idProd, null);


            while (c.moveToNext()){
                double precio = c.getDouble(c.getColumnIndex(Contract.ProcesoInventarioEntry.PRECIO_PROD));
                int facing = c.getInt(c.getColumnIndex(Contract.ProcesoInventarioEntry.FACING_PROD));
                int promo = c.getInt(c.getColumnIndex(Contract.ProcesoInventarioEntry.PROMO_PROD));
                int exp = c.getInt(c.getColumnIndex(Contract.ProcesoInventarioEntry.EXPOSICION));
                int rot = c.getInt(c.getColumnIndex(Contract.ProcesoInventarioEntry.ROTURA));
                String obs = c.getString(c.getColumnIndex(Contract.ProcesoInventarioEntry.OBSERVACIONES));

                binding.textViewPrec.setText(precio+"");
                binding.textViewFac.setText(facing+"");
                binding.textViewProm.setText(intToString(promo));
                binding.textViewRot.setText(intToString(rot));
                binding.textViewExp.setText(intToString(exp));
                binding.textViewObs.setText(obs);
            }



            Cursor cName = db.rawQuery("SELECT * FROM "+Contract.ProductosEntry.TABLE_NAME+" WHERE "+Contract.ProductosEntry.ID+" = "+idProd, null);

            while (cName.moveToNext()){

                String name = cName.getString(cName.getColumnIndex(Contract.ProductosEntry.NOMBRE_COMPLETO));
                binding.textViewNom.setText(name);
            }




            Cursor cImg = db.rawQuery("SELECT * FROM "+Contract.ImagenesProdcutoEntry.TABLE_NAME+" WHERE "+Contract.ImagenesProdcutoEntry.OBJETO+" = \'"+Imagen.Objeto.INVENTARIO+"\' AND "+Contract.ImagenesProdcutoEntry.ID_OBJETO+" = "+idInv+" AND "+Contract.ImagenesProdcutoEntry.ID_PRODUCTO+" = "+idProd, null);

            while(cImg.moveToNext()){
                @SuppressLint("Range") String ruta = cImg.getString(cImg.getColumnIndex(Contract.ImagenesProdcutoEntry.IMAGEN));
                rutas.add(ruta);
            }
        }else{


            obsFin();


            Cursor cInv = db.rawQuery("SELECT * FROM "+Contract.InventarioEntry.TABLE_NAME+" WHERE "+Contract.InventarioEntry.ID+" = "+idInv, null);
            cInv.moveToFirst();
            String obs = cInv.getString(cInv.getColumnIndex(Contract.InventarioEntry.OBS_FINALES));



            Cursor cImg = db.rawQuery("SELECT * FROM "+Contract.ImagenesProdcutoEntry.TABLE_NAME+" WHERE "+Contract.ImagenesProdcutoEntry.OBJETO+" = \'"+Imagen.Objeto.INVENTARIO+"\' AND "+Contract.ImagenesProdcutoEntry.ID_OBJETO+" = "+idInv+" AND "+Contract.ImagenesProdcutoEntry.FOTOSFIN+" = '"+Imagen.fotosFinales.SI+"'", null);

            while(cImg.moveToNext()){
                @SuppressLint("Range") String ruta = cImg.getString(cImg.getColumnIndex(Contract.ImagenesProdcutoEntry.IMAGEN));
                rutas.add(ruta);
            }


            binding.textViewObsFin.setText("Observaciones finales");
            binding.textView86.setText(obs);

        }






        images = new ArrayList<>();

        if (rutas.size() > 0){
            for (int i = 0; i < rutas.size(); i++) {

                LinearLayout ly = binding.layoutImgInvent;
                ImageView iv = new ImageView(this);
                Bitmap imgBitmap = BitmapFactory.decodeFile(rutas.get(i));
                imgBitmap = escalaAnchura(imgBitmap, MainActivity.width/2);
                iv.setImageBitmap(imgBitmap);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                iv.setLayoutParams(params);
                ly.addView(iv);
                images.add(iv);
            }
        }





        for (int i = 0; i < images.size(); i++) {
            int pos = i;
            images.get(pos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ivSelect = images.get(pos);

                    AlertDialogImage alertDialogImage = new AlertDialogImage();
                    alertDialogImage.show(getSupportFragmentManager(), "Alerta");
                    Toast.makeText(getApplicationContext(), idProd+"", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}