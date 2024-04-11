package com.example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ApiHandler {

    private static final String API_URL = "http://api.weatherstack.com/current?access_key=3599e22e5bc1eb0475e72c6bce3995a7&query=";

    public static String getWeatherData(String location) {
        String urlString = API_URL + location;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}