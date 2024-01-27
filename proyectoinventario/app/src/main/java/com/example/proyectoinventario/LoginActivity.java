package com.example.proyectoinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectoinventario.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;



    String jsonStatus;
    boolean LOGIN;
    boolean VIEW;

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ImageButton viewPassButton;

    private static final String LOGIN_URL = "https://emite.tegestiona.es/login/json";
    private static final String TAG = "LoginActivity";
    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";

    Intent itMain;
    SQLiteDatabase db;


    String email;
    String password;
    User usuario = null;


    public void comprobarLogin(){
        if(jsonStatus == null){

            LOGIN = false;
        }else{

            if (jsonStatus.equals("login")){

                LOGIN = true;
            }else{
                LOGIN = false;
            }
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(this);
        db = dbHelper.getWritableDatabase();



        itMain = new Intent(this, MainActivity.class);

        Intent intent = getIntent();
        //usuario = (User) intent.getSerializableExtra("USER");


        emailEditText = binding.editTextLoginEmail;
        passwordEditText = binding.editTextLoginPass;
        loginButton = binding.buttonLogin;
        viewPassButton = binding.buttonViewPass;


        VIEW = false;


        // Restaurar las credenciales almacenadas en SharedPreferences

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString(PREF_EMAIL, null);
        String savedPassword = sharedPreferences.getString(PREF_PASSWORD, null);

        if (savedEmail != null && savedPassword != null) {

            // Cargar las credenciales en los campos de texto
            emailEditText.setText(savedEmail);
            passwordEditText.setText(savedPassword);
        }





        viewPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!VIEW){
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    viewPassButton.setImageResource(R.drawable.ic_view);
                    VIEW = true;
                }else{
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    viewPassButton.setImageResource(R.drawable.ic_viewoff);
                    VIEW = false;
                }
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Obtener los datos de inicio de sesión ingresados por el usuario
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Guardar las credenciales en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREF_EMAIL, email);
                editor.putString(PREF_PASSWORD, password);
                editor.apply();

                // Realizar la solicitud POST en un hilo separado
                new LoginTask().execute(email, password);

            }
        });



    }


    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Datos de inicio de sesión (email y contraseña)
            email = params[0];
            password = params[1];

            try {
                // Crear la URL de la solicitud POST
                URL url = new URL(LOGIN_URL);

                // Crear la conexión HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Establecer el método de solicitud como POST
                connection.setRequestMethod("POST");

                // Habilitar el envío y recepción de datos
                connection.setDoOutput(true);
                connection.setDoInput(true);

                // Establecer los parámetros de la solicitud
                String parameters = "usuario=" + URLEncoder.encode(email, "UTF-8") + "&passwd=" + URLEncoder.encode(password, "UTF-8");

                // Obtener el flujo de salida de la conexión
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                // Escribir los parámetros en el flujo de salida
                writer.write(parameters);
                writer.flush();
                writer.close();
                outputStream.close();

                // Obtener el código de estado de la respuesta
                int statusCode = connection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {

                    // La solicitud fue exitosa
                    // Obtener el flujo de entrada de la conexión
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    // Leer la respuesta JSON
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    inputStream.close();

                    // Obtener el valor de "status" del JSON de respuesta
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String status = jsonResponse.getString("status");
                    return status;

                } else {
                    // La solicitud no fue exitosa
                    return null;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String status) {
            if (status != null) {
                // La solicitud fue exitosa, se obtuvo el valor de "status"
                Log.d(TAG, "Valor de 'status' del JSON de respuesta: " + status);

                // Realizar las acciones necesarias según el valor de "status"
                // ...
            } else {
                // Error al procesar la solicitud
                Log.e(TAG, "Error al realizar la solicitud");
            }

            jsonStatus = status;
            comprobarLogin();

            if (LOGIN){

                Cursor cUser = db.rawQuery("SELECT * FROM "+Contract.Usuario.TABLE_NAME+" WHERE "+Contract.Usuario.EMAIL_USUARIO+" = '"+email.trim()+"' ", null);

                if (cUser.getCount() > 0) {

                    cUser.moveToFirst();

                    @SuppressLint("Range") int id = cUser.getInt(cUser.getColumnIndex(Contract.Usuario.ID));
                    @SuppressLint("Range") String email = cUser.getString(cUser.getColumnIndex(Contract.Usuario.EMAIL_USUARIO));
                    @SuppressLint("Range") String password = cUser.getString(cUser.getColumnIndex(Contract.Usuario.CONTRA_USUARIO));
                    @SuppressLint("Range") int logeado = cUser.getInt(cUser.getColumnIndex(Contract.Usuario.LOGEADO));

                    usuario = new User();
                    usuario.setId(id);
                    usuario.setEmail(email);
                    usuario.setPassword(password);
                    usuario.setLogin(logeado);

                    db.execSQL("UPDATE "+Contract.Usuario.TABLE_NAME+ " SET "+Contract.Usuario.LOGEADO+" = "+1+" WHERE "+Contract.Usuario.ID+" = "+usuario.getId());
                }else{

                    db.execSQL("INSERT INTO "+ Contract.Usuario.TABLE_NAME+"("+ Contract.Usuario.EMAIL_USUARIO+","+ Contract.Usuario.CONTRA_USUARIO+","+Contract.Usuario.LOGEADO+") VALUES('"+email.trim()+"','"+password.trim()+"',"+1+")");
                }

                startActivity(itMain);
            }else{

                emailEditText.setText("");
                passwordEditText.setText("");

                emailEditText.setHintTextColor(getResources().getColor(R.color.red));
                passwordEditText.setHintTextColor(getResources().getColor(R.color.red));

                if (jsonStatus==null){
                    binding.textViewError.setText("ERROR: Fallo de conexión");

                }

                binding.textViewError.setVisibility(View.VISIBLE);

            }

        }
    }

}