package com.example.proyectoinventario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventariosFin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventariosFin extends Fragment {



    public ListView listView;
    public FloatingActionButton buttonShare;
    public String superName;
    public TextView textJSON;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InventariosFin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventariosFin.
     */
    // TODO: Rename and change types and number of parameters
    public static InventariosFin newInstance(String param1, String param2) {
        InventariosFin fragment = new InventariosFin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventarios_fin, container, false);

        listView = view.findViewById(R.id.listInvFin);
        buttonShare = view.findViewById(R.id.buttonShare);
        textJSON = view.findViewById(R.id.textJSON);


        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();



        Intent itInv = new Intent(getContext(), InventariosActivity.class);


        Cursor cUser = db.rawQuery("SELECT * FROM "+Contract.Usuario.TABLE_NAME+" WHERE "+Contract.Usuario.LOGEADO+" = "+1,null);
        cUser.moveToFirst();
        @SuppressLint("Range") int idUser = cUser.getInt(cUser.getColumnIndex(Contract.Usuario.ID));
        String email = cUser.getString(cUser.getColumnIndex(Contract.Usuario.EMAIL_USUARIO));
        String contra = cUser.getString(cUser.getColumnIndex(Contract.Usuario.CONTRA_USUARIO));
        int logueado = cUser.getInt(cUser.getColumnIndex(Contract.Usuario.LOGEADO));


        ArrayList<Inventario> inventarios = new ArrayList<>();
        ArrayList<InfoAdapter> infoAdapters = new ArrayList<>();
        AdapterPersonal adapterPersonal;

        Cursor c = db.rawQuery("SELECT * FROM "+Contract.InventarioEntry.TABLE_NAME+ " WHERE "+Contract.InventarioEntry.PENDIENTE+" = "+0+" AND "+Contract.InventarioEntry.ID_USUARIO+" = "+idUser,null);

        while (c.moveToNext()){

            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.InventarioEntry.ID));
            @SuppressLint("Range") String fecha = c.getString(c.getColumnIndex(Contract.InventarioEntry.FECHA));
            @SuppressLint("Range") String ini = c.getString(c.getColumnIndex(Contract.InventarioEntry.INICIO));
            @SuppressLint("Range") String fin = c.getString(c.getColumnIndex(Contract.InventarioEntry.FIN));
            @SuppressLint("Range") String obs = c.getString(c.getColumnIndex(Contract.InventarioEntry.OBS_FINALES));
            @SuppressLint("Range") int userId = c.getInt(c.getColumnIndex(Contract.InventarioEntry.ID_USUARIO));
            @SuppressLint("Range") int pend = c.getInt(c.getColumnIndex(Contract.InventarioEntry.PENDIENTE));
            @SuppressLint("Range") int idSuper = c.getInt(c.getColumnIndex(Contract.InventarioEntry.ID_SUPER));


            Cursor cSuper = db.rawQuery("SELECT * FROM "+Contract.SuperEntry.TABLE_NAME+" WHERE "+Contract.SuperEntry.ID+ " = "+idSuper, null);
            cSuper.moveToFirst();
            superName = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.NOMBRE));

            Inventario inventario = new Inventario();
            inventario.setId(id);
            inventario.setFecha(fecha);
            inventario.setIncio(ini);
            inventario.setFin(fin);
            inventario.setObsFin(obs);
            inventario.setId_super(idSuper);
            inventario.setIdUser(userId);
            inventario.setPendiente(pend);

            inventarios.add(inventario);

            InfoAdapter ia = new InfoAdapter("#" + id, fecha, superName , Color.WHITE, Color.BLACK, Color.GRAY, Color.GRAY);
            infoAdapters.add(ia);

        }

        adapterPersonal = new AdapterPersonal(getContext(), infoAdapters);
        listView.setAdapter(adapterPersonal);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Inventario inv = inventarios.get(position);
                itInv.putExtra("INVFIN", inv);
                startActivity(itInv);
            }
        });



        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        buttonShare.setOnClickListener(new View.OnClickListener() {



            //Obtener JSON de los inventarios

            JSONArray jsonArrayInvs = new JSONArray();




            @Override
            public void onClick(View v) {


                Cursor cInv = db.rawQuery("SELECT * FROM "+Contract.InventarioEntry.TABLE_NAME+" WHERE "+Contract.InventarioEntry.PENDIENTE+" = "+0+ " AND "+Contract.InventarioEntry.ID_USUARIO+" = "+idUser, null);


                while (cInv.moveToNext()){


                    JSONObject jsonObjectInv = new JSONObject();

                    int columnCountInv = cInv.getColumnCount();

                    for (int i = 1; i < columnCountInv; i++) {

                        String columnName = cInv.getColumnName(i);
                        String columnValue = cInv.getString(i);

                        try {

                            jsonObjectInv.put(columnName, columnValue);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }



                    Cursor cProd = db.rawQuery("SELECT * FROM "+Contract.ProcesoInventarioEntry.TABLE_NAME+" WHERE "+Contract.ProcesoInventarioEntry.ID_INVENTARIO+" = "+cInv.getInt(cInv.getColumnIndex(Contract.InventarioEntry.ID)),null);


                    JSONArray jsonArrayProds = new JSONArray();


                    if (cProd.moveToFirst()) {
                        do {

                            JSONObject jsonObjectProd = new JSONObject();
                            int columnCount = cProd.getColumnCount();

                            for (int i = 1; i < columnCount; i++) {

                                String columnName = cProd.getColumnName(i);
                                String columnValue = cProd.getString(i);

                                try {
                                    jsonObjectProd.put(columnName, columnValue);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            jsonArrayProds.put(jsonObjectProd);
                        } while (cProd.moveToNext());
                    }


                    try {
                        jsonObjectInv.put("productos", jsonArrayProds);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    Cursor cImg = db.rawQuery("SELECT * FROM "+Contract.ImagenesProdcutoEntry.TABLE_NAME+" WHERE "+Contract.ImagenesProdcutoEntry.OBJETO+" = '"+Imagen.Objeto.INVENTARIO+"' AND "+Contract.ImagenesProdcutoEntry.ID_OBJETO+" = "+cInv.getInt(cInv.getColumnIndex(Contract.InventarioEntry.ID)),null);

                    JSONArray jsonArrayImg = new JSONArray();

                    if (cImg.moveToFirst()){

                        do {

                            JSONObject jsonObjectImg = new JSONObject();

                            int columnCount = cImg.getColumnCount();

                            for (int i = 3; i < columnCount; i++) {

                                String columnName = cImg.getColumnName(i);
                                String columnValue = "";

                                if (i==5){

                                    Bitmap bitmap = BitmapFactory.decodeFile(cImg.getString(i),options);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] imageBytes = baos.toByteArray();
                                    String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                                    columnValue = base64Image;

                                }else{

                                    columnValue = cImg.getString(i);
                                }


                                try {

                                    jsonObjectImg.put(columnName, columnValue);

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }

                            jsonArrayImg.put(jsonObjectImg);

                        }while (cImg.moveToNext());
                    }

                    try {
                        jsonObjectInv.put("imagenes",jsonArrayImg);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    jsonArrayInvs.put(jsonObjectInv);

                }




                String jsonString = jsonArrayInvs.toString();
                new SendTask().execute(jsonString, email,contra, Integer.toString(logueado));

            }
        });

        return view;


    }

    private class SendTask extends AsyncTask<String, Void, String>{


        String JSON;
        String emailUser;
        String contraUser;
        String logueado;



        @Override
        protected String doInBackground(String... strings) {

            JSON = strings[0];
            emailUser = strings[1];
            contraUser = strings[2];
            logueado = strings[3];

            try {


                CustomTrustManager.disableCertificateValidation();


                //URL url = new URL("https://192.168.200.43/servidor/index.php");
                URL url = new URL("https://192.168.0.19/servidor/index.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");




                Map<String, String> params = new HashMap<>();
                params.put("jsonArray",JSON);
                params.put("emailUser", emailUser);
                params.put("contraUser", contraUser);
                params.put("logueado", logueado);




                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, String> param : params.entrySet()) {

                    if (postData.length() != 0) {
                        postData.append('&');
                    }

                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(param.getValue(), "UTF-8"));

                }





                OutputStream os = connection.getOutputStream();

                // Escribe los datos en el flujo de salida
                os.write(postData.toString().getBytes("UTF-8"));
                os.flush();
                os.close();



                int responseCode = connection.getResponseCode();


                if (responseCode == HttpURLConnection.HTTP_OK) {


                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }

                    br.close();
                    connection.disconnect();

                    return response.toString();



                } else {

                    return "ERROR AL ENVIAR LOS DATOS";

                }


            } catch (IOException e) {
                e.printStackTrace();

                return "ERROR DE CONEXIÓN: "+ e.getMessage();
            }


        }


        @Override
        protected void onPostExecute(String result) {

            textJSON.setText(result);
            Log.i("RESPONSE", result);

        }
    }
}