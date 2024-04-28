package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiHandler {

    private static final String API_URL = "http://api.weatherstack.com/current?access_key=3599e22e5bc1eb0475e72c6bce3995a7&query=";
    private static final int THREAD_COUNT = 10;
    private static HashMap<String, String> cache = new HashMap<>();

    public static String getWeatherData(String location) {
        if (cache.containsKey(location)) {
            long cacheStartTime = System.currentTimeMillis();
            String cachedData = cache.get(location);
            long cacheEndTime = System.currentTimeMillis();
            System.out.println("Cache retrieval time: " + (cacheEndTime - cacheStartTime) + " ms");
            return cachedData;
        }

        String urlString = API_URL + location;
        StringBuilder result = new StringBuilder();

        try {
            long apiStartTime = System.currentTimeMillis();

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            long apiEndTime = System.currentTimeMillis();
            System.out.println("API call time: " + (apiEndTime - apiStartTime) + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }

        String weatherData = result.toString();
        cache.put(location, weatherData);
        System.out.println("Cache contents: " + cache);

        return weatherData;
    }

    public static void storeWeatherData(String location, String weatherData) {
    // Store in cache
    cache.put(location, weatherData);

    // Store in database
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://Class:3306");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS cache (location TEXT PRIMARY KEY, weatherData TEXT)");

        PreparedStatement pstmt = conn.prepareStatement("INSERT OR REPLACE INTO cache (location, weatherData) VALUES (?, ?)");
        pstmt.setString(1, location);
        pstmt.setString(2, weatherData);
        pstmt.executeUpdate();

        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}