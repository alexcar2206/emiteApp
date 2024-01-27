package com.example.proyectoinventario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONFormSender {

    public String sendJSONForm(String urlString, String json) {

        try {

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write("json=" + json);
            writer.flush();
            writer.close();
            outputStream.close();


            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {


                /*
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {

                    response.append(line);
                }


                reader.close();
                return response.toString();

                 */

                return "SOLICTUD REALIZADA";

            } else {

                return "ERROR AL ENVIAR LOS DATOS";

            }


        } catch (IOException e) {
            e.printStackTrace();

            return "ERROR DE CONEXIÓN: "+ e.getMessage();
        }
    }
}