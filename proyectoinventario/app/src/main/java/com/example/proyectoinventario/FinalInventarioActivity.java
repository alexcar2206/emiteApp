package com.example.proyectoinventario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.proyectoinventario.databinding.ActivityFinalInventarioBinding;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FinalInventarioActivity extends AppCompatActivity {


    ActivityFinalInventarioBinding binding;

    String ruta;
    int idInventario;
    int idSuper;
    Inventario inventario;
    ArrayList<Productos> productos;
    ArrayList<Integer> idsProdOrden;
    ArrayList<Imagen> imagenes = new ArrayList<>();


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

        LinearLayout ly = binding.layoutImageFin;
        ImageView iv = new ImageView(this);



        if (requestCode == 1 && resultCode == RESULT_OK) {// Thumbnail


            Imagen img = new Imagen();
            img.setImagen(ruta);
            img.setObjeto(Imagen.Objeto.INVENTARIO);
            img.setIdObtejo(idInventario);
            img.setFotosFin(Imagen.fotosFinales.SI);
            imagenes.add(img);

            Bitmap imgBitmap = BitmapFactory.decodeFile(ruta);
            imgBitmap = escalaAnchura(imgBitmap, MainActivity.width/2);
            iv.setImageBitmap(imgBitmap);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
            iv.setLayoutParams(params);
            ly.addView(iv);


        }
    }


    public boolean comprobarCamposVacios(){

        if (imagenes.size() > 0){
            return false;
        }else{
            return true;
        }
    }


    public void saveInv(SQLiteDatabase db){



        inventario.setObsFin(binding.editTextObsFinales.getText().toString());

        if (!comprobarCamposVacios()){

            for (int i = 0; i < imagenes.size(); i++) {

                db.execSQL("INSERT INTO "+Contract.ImagenesProdcutoEntry.TABLE_NAME+"("+Contract.ImagenesProdcutoEntry.OBJETO+","+Contract.ImagenesProdcutoEntry.ID_OBJETO+","+Contract.ImagenesProdcutoEntry.ID_PRODUCTO+","+Contract.ImagenesProdcutoEntry.FOTOSFIN +","+Contract.ImagenesProdcutoEntry.IMAGEN+") VALUES (\'"+imagenes.get(i).getObjeto()+"\',"+imagenes.get(i).getIdObtejo()+", NULL ,"+imagenes.get(i).getFotosFin()+",'"+imagenes.get(i).getImagen()+"')");
            }
        }



        LocalDateTime locaDate = LocalDateTime.now();
        int hours  = locaDate.getHour();
        int minutes = locaDate.getMinute();
        int seconds = locaDate.getSecond();

        String horaFin = hours+":"+minutes+":"+seconds;
        inventario.setFin(horaFin);



        //Actualizar datos restantes de inventario
        db.execSQL("UPDATE "+ Contract.InventarioEntry.TABLE_NAME+" SET "+Contract.InventarioEntry.FIN+" = \'"+inventario.getFin()+"\',"+Contract.InventarioEntry.PENDIENTE+ " =  0 ,"+Contract.InventarioEntry.OBS_FINALES+" = '"+inventario.getObsFin()+ "' WHERE "+ Contract.InventarioEntry.ID+" = "+idInventario);



        //Obtener ids del orden de seleccion de productos
        if (productos.size() > 0){
            for (int i = 0; i < productos.size(); i++) {

                idsProdOrden.add(productos.get(i).getId());
            }
        }


        //Actualizar orden de seleccion de inventario
        db.execSQL("DELETE FROM "+Contract.OrdenSuperInventario.TABLE_NAME+" WHERE "+Contract.OrdenSuperInventario.ID_SUPER+" = "+idSuper);

        for (int i = 0; i < idsProdOrden.size(); i++) {

            db.execSQL(" INSERT INTO "+Contract.OrdenSuperInventario.TABLE_NAME+"("+Contract.OrdenSuperInventario.ID_SUPER+","+Contract.OrdenSuperInventario.ID_PROD+") VALUES ("+idSuper+","+ idsProdOrden.get(i)+")");
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinalInventarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();



        Intent intent = getIntent();
        idInventario = intent.getIntExtra("IDINVENTARIO",0);
        inventario = (Inventario) intent.getSerializableExtra("INVENTARIO");
        productos = (ArrayList<Productos>) intent.getSerializableExtra("PRODUCTOS");
        idsProdOrden = (ArrayList<Integer>) intent.getSerializableExtra("IDPRODUCTOS");
        idSuper = intent.getIntExtra("IDSUPER",0);


        Intent itMain = new Intent(this, MainActivity.class);


        binding.btnImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camara();
            }
        });

        binding.btnFinInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInv(db);
                startActivity(itMain);
            }
        });

    }
}