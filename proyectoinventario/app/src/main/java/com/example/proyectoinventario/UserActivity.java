package com.example.proyectoinventario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.proyectoinventario.databinding.ActivityUserBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserActivity extends AppCompatActivity {


    ActivityUserBinding binding;
    User user;

    SQLiteDatabase db;

    Intent itSplash;



    public void opcionOK(){
        db.execSQL("UPDATE "+ Contract.Usuario.TABLE_NAME+" SET "+ Contract.Usuario.LOGEADO+" = "+0+" WHERE "+Contract.Usuario.ID+" = "+user.getId());
        startActivity(itSplash);
    }

    public void opcionCancel(){

    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar sesión en '"+user.getEmail()+"'");
        builder.setMessage("¿Estás seguro de que quieres cerrar sesión?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                opcionOK();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                opcionCancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        db = dbHelper.getWritableDatabase();


        itSplash = new Intent(this, SplashActivity.class);
        Intent itMain = new Intent(this, MainActivity.class);
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


        String nombreUser;

        String[] nombreSplit = user.getEmail().split("@");
        nombreUser = nombreSplit[0];
        binding.textView90.setText(nombreUser);
        binding.textView87.setText(user.getEmail());
        binding.buttonCloseSesion.setText("Cerrar sesión  "+user.getEmail());




        binding.buttonCloseSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog();
            }
        });




        BottomNavigationView bottomNavigationView = binding.includeNav.bottomNavView;
        bottomNavigationView.setSelectedItemId(R.id.id_perfil);

        binding.includeNav.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.id_home:
                        startActivity(itMain);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        return true;
                    case R.id.id_inventarios:
                        itInv.putExtra("FIN", true);
                        itInv.putExtra("USER", user);
                        startActivity(itInv);
                        overridePendingTransition(R.anim.slide2_in_left, R.anim.slide2_out_right);
                        return true;


                }

                return false;
            }
        });
    }
}