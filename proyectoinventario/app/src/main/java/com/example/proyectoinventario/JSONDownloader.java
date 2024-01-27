package com.example.proyectoinventario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONDownloader {
    public static String downloadJSON(String urlString) throws IOException {
        StringBuilder result = new StringBuilder();

        // Crea la URL y establece la conexión
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Lee la respuesta
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        // Cierra la conexión
        connection.disconnect();

        return result.toString();
    }
}
