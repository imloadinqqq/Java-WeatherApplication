package com.example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ApiHandler {

    private static final String API_URL = "http://api.weatherstack.com/current?access_key=3599e22e5bc1eb0475e72c6bce3995a7&query=New York";~

    public static String getWeather(String location) {
        String urlString = API_URL + location;
        StringBuilder result = new StringBuilder();

        // Make a GET request to the API
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Open a connection to the URL
            connection.setRequestMethod("GET"); // Set the request method to GET

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); // Get the response from the API
            String line; 
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close(); // Close the response reader
        } catch (Exception e) { // Catch any exceptions that occur
            e.printStackTrace();
        }

        return result.toString(); // Return the response from the API
    }
}
