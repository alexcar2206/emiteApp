package com.example.proyectoinventario;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.example.proyectoinventario.databinding.ActivitySplashBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SplashActivity extends AppCompatActivity  {


    ActivitySplashBinding binding;



    boolean LOGIN;
    User usuario;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();




        if (db != null) {


            Cursor cCad = db.rawQuery("SELECT * FROM " + Contract.CadenaEntry.TABLE_NAME, null);

            if (cCad.getCount() == 0) {

                InsertarDatos.insertarCadenas(db);
            }


            Cursor cCluster = db.rawQuery("SELECT * FROM " + Contract.ClusterEntry.TABLE_NAME, null);

            if (cCluster.getCount() == 0) {

                InsertarDatos.insertarClusters(db);

            }

            Cursor cSuper = db.rawQuery("SELECT * FROM " + Contract.SuperEntry.TABLE_NAME, null);

            if (cSuper.getCount() == 0) {

                InsertarDatos.insertarSupers(db);
                db.execSQL(" UPDATE "+Contract.SuperEntry.TABLE_NAME+" SET "+Contract.SuperEntry.COORDX+" = "+42.21042224876457+","+Contract.SuperEntry.COORSY+" = "+ -8.74794021792389+ " WHERE "+Contract.SuperEntry.ID+" = 257");
                db.execSQL(" UPDATE "+Contract.SuperEntry.TABLE_NAME+" SET "+Contract.SuperEntry.COORDX+" = "+42.39194645798104+","+Contract.SuperEntry.COORSY+" = "+ -8.697129067819857+ " WHERE "+Contract.SuperEntry.ID+" = 9");

            }



            Cursor cProd = db.rawQuery("SELECT * FROM " + Contract.ProductosEntry.TABLE_NAME, null);

            if (cProd.getCount() == 0) {

                InsertarDatos.insertarProductos(db);
            }


            Cursor cFam = db.rawQuery("SELECT * FROM " + Contract.FamiliaEntry.TABLE_NAME, null);

            if (cFam.getCount() == 0) {

                InsertarDatos.insertarFamilias(db);
            }


            Cursor cSub = db.rawQuery("SELECT * FROM " + Contract.SubfamiliaEntry.TABLE_NAME, null);

            if (cSub.getCount() == 0) {

                InsertarDatos.insertarSubfamilia(db);
            }


            Cursor cProdClust = db.rawQuery("SELECT * FROM " + Contract.ProductoClusterEntry.TABLE_NAME, null);

            if (cProdClust.getCount() == 0) {

                InsertarDatos.insertarProdCluster(db);

            }

            Cursor cFamSub = db.rawQuery("SELECT * FROM " + Contract.FamiliaSubmailiaEntry.TABLE_NAME, null);

            if (cFamSub.getCount() == 0) {

                InsertarDatos.insertarFamSub(db);
            }

            Cursor cFamProd = db.rawQuery("SELECT * FROM " + Contract.FamiliaProductoEntry.TABLE_NAME, null);

            if (cFamProd.getCount() == 0) {

                InsertarDatos.insertarFamProd(db);

            }


        }



        Cursor c = db.rawQuery("SELECT * FROM "+ Contract.Usuario.TABLE_NAME+ " WHERE "+Contract.Usuario.LOGEADO+" = "+1,null);

        if (c.getCount() > 0){

            c.moveToFirst();

            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.Usuario.ID));
            @SuppressLint("Range") String email = c.getString(c.getColumnIndex(Contract.Usuario.EMAIL_USUARIO));
            @SuppressLint("Range") String password = c.getString(c.getColumnIndex(Contract.Usuario.CONTRA_USUARIO));
            @SuppressLint("Range") int logeado = c.getInt(c.getColumnIndex(Contract.Usuario.LOGEADO));
            usuario = new User();
            usuario.setId(id);
            usuario.setEmail(email);
            usuario.setPassword(password);
            usuario.setLogin(logeado);


           LOGIN = true;

        }else{

            LOGIN = false;
        }






        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {


                Intent itMain = new Intent(SplashActivity.this, MainActivity.class);
                Intent itLog = new Intent(SplashActivity.this, LoginActivity.class);



                if (!LOGIN){
                    startActivity(itLog);
                }else{
                    itMain.putExtra("USER", usuario);
                    startActivity(itMain);
                }

                finish();
            }
        }, 3000);

    }

}