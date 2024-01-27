package com.example.proyectoinventario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectoinventario.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import java.util.ArrayList;

import org.osmdroid.config.Configuration;


public class MainActivity extends AppCompatActivity{




    private ActivityMainBinding binding;




    static int width;
    static int height;

    User user;


    public void opcionOk(String mensaje) {

        Intent itInvPend = new Intent(this, InventariosPendActivity.class);
        startActivity(itInvPend);
    }
    public void opcionCancel(String mensaje) {

        Intent itInvent = new Intent(this, AccederInventarioActivity.class);
        startActivity(itInvent);
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(binding.getRoot());

        // Mantiene la pantalla encendida
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //

        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent itCad = new Intent(this, CadenaActivity.class);
        Intent itCluster = new Intent(this, ClusterActivity.class);
        Intent itSuper = new Intent(this, SuperActivity.class);
        Intent itProd = new Intent(this, ProductosActivity.class);
        Intent itFam = new Intent(this, FamiliaActivity.class);
        Intent itInvs = new Intent(this, InventariosActivity.class);
        Intent itInvent = new Intent(this, AccederInventarioActivity.class);
        Intent itUser = new Intent(this, UserActivity.class);
        Intent itInv = new Intent(this, InventariosPendFinActivity.class);






        Cursor cUser = db.rawQuery("SELECT * FROM "+Contract.Usuario.TABLE_NAME+ " WHERE "+Contract.Usuario.LOGEADO+" = "+1,null);
        cUser.moveToFirst();

        @SuppressLint("Range") int idUser = cUser.getInt(cUser.getColumnIndex(Contract.Usuario.ID));
        @SuppressLint("Range") String email = cUser.getString(cUser.getColumnIndex(Contract.Usuario.EMAIL_USUARIO));
        @SuppressLint("Range") String password = cUser.getString(cUser.getColumnIndex(Contract.Usuario.CONTRA_USUARIO));
        @SuppressLint("Range") int logeado = cUser.getInt(cUser.getColumnIndex(Contract.Usuario.LOGEADO));

        user = new User();
        user.setId(idUser);
        user.setEmail(email);
        user.setPassword(password);
        user.setLogin(logeado);




        Display display = getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {  // > API 12
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }




        binding.includeMain.buttonMenu.setVisibility(View.VISIBLE);
        binding.includeMain.buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.open();
            }
        });






        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                binding.drawerLayout.closeDrawers();


                switch (menuItem.getTitle().toString()) {
                    case "Cadena":
                        startActivity(itCad);
                        break;
                    case "Cluster":
                        startActivity(itCluster);
                        break;
                    case "Super":
                        startActivity(itSuper);
                        break;
                    case "Productos":
                        startActivity(itProd);
                        break;
                    case "Familia":
                        startActivity(itFam);
                        break;
                    case "Subfamilia":
                        break;
                }

                return true;
            }
        });




        /*

        binding.includeBottomNav.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){

                    case R.id.id_home:

                        return true;
                    case R.id.id_perfil:


                        startActivity(itUser);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();

                        return true;
                    case R.id.id_inventarios:
                        itInv.putExtra("FIN", true);
                        itInv.putExtra("USER", user);
                        startActivity(itInv);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        return  true;
                }


                return false;
            }
        });

         */


        binding.includeMain.includeNav.bottomNavView.setSelectedItemId(R.id.id_home);
        binding.includeMain.includeNav.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.id_home:

                        return true;
                    case R.id.id_perfil:

                        startActivity(itUser);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();

                        return true;
                    case R.id.id_inventarios:
                        itInv.putExtra("FIN", true);
                        itInv.putExtra("USER", user);
                        startActivity(itInv);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        return  true;
                }

                return false;
            }
        });



        binding.includeMain.buttonInventNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = db.rawQuery("SELECT * FROM "+Contract.InventarioEntry.TABLE_NAME+ " WHERE "+Contract.InventarioEntry.PENDIENTE+" = 1",null);

                if (c.getCount() > 0){
                    AlertDialogInvsPend dAlerta = new AlertDialogInvsPend ();
                    dAlerta.show(getSupportFragmentManager(), "Alerta");
                }else{

                    startActivity(itInvent);
                }
            }
        });










    }

}