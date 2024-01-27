package com.example.proyectoinventario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.proyectoinventario.databinding.ActivityInventariosPendFinBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class InventariosPendFinActivity extends AppCompatActivity {


    ActivityInventariosPendFinBinding binding;

    User user;



    public ViewPager2 viewPager;
    public TabLayout tabLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventariosPendFinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();




        Intent itHome = new Intent(this, MainActivity.class);
        Intent itPerfil = new Intent(this, UserActivity.class);



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




        viewPager = binding.viewPager;
        tabLayout = binding.tabLayout;


        PagerAdapter pagerAdapter = new PagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Finalizados");
                    break;
                case 1:
                    tab.setText("Pendientes");
                    break;
            }

            // Configura el texto de las pestañas si es necesario
        }).attach();




        binding.includeNav.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){


                    case R.id.id_home:
                        startActivity(itHome);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return  true;
                    case R.id.id_perfil:
                        itPerfil.putExtra("USER",user);
                        startActivity(itPerfil);
                        overridePendingTransition(R.anim.slide2_in_right, R.anim.slide2_out_left);
                        return true;
                }


                return false;
            }
        });

    }
}