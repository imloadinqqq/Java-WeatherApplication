package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;

public class ApiHandler {

    private static final String API_URL = "http://api.weatherstack.com/current?access_key=3599e22e5bc1eb0475e72c6bce3995a7&query=";
    private static final HashMap<String, String> cache = new HashMap<>();

    // Initialize the database with a new cache table
    public static void initializeDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/weather?user=root&password=Sirsonic1234");

            // Drop existing cache table if it exists
            PreparedStatement dropTableStmt = conn.prepareStatement("DROP TABLE IF EXISTS cache");
            dropTableStmt.executeUpdate();
            System.out.println("Existing cache table dropped");

            // Create new cache table
            PreparedStatement createTableStmt = conn.prepareStatement("CREATE TABLE cache (location VARCHAR(255) PRIMARY KEY, weatherData TEXT)");
            createTableStmt.executeUpdate();
            System.out.println("New cache table created");

            conn.close();
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve weather data either from cache, database, or API
    public static String getWeatherData(String location) {
        long startTime = System.currentTimeMillis();

        // If data not found in cache, try fetching from database
        String weatherDataFromDB = fetchWeatherDataFromDatabase(location);
        if (weatherDataFromDB != null) {
            cache.put(location, weatherDataFromDB); // Cache the data
            long endTime = System.currentTimeMillis();
            System.out.println("Database retrieval time: " + (endTime - startTime) + " ms");
            return weatherDataFromDB;
        }

        // If data not found in database, fetch from API
        String weatherDataFromAPI = fetchWeatherDataFromAPI(location);
        cache.put(location, weatherDataFromAPI); // Cache the data from the API
        long endTime = System.currentTimeMillis();
        System.out.println("API retrieval time: " + (endTime - startTime) + " ms");
        return weatherDataFromAPI;
    }

    // Fetch weather data from the API
    private static String fetchWeatherDataFromAPI(String location) {
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
        System.out.println("Weather data fetched from API: " + weatherData);
        return weatherData;
    }

    // Fetch weather data from the database
    private static String fetchWeatherDataFromDatabase(String location) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/weather?user=root&password=Sirsonic1234");

            PreparedStatement pstmt = conn.prepareStatement("SELECT weatherData FROM cache WHERE location = ?");
            pstmt.setString(1, location);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String weatherData = rs.getString("weatherData");
                conn.close();
                System.out.println("Weather data retrieved from database for location: " + location);
                return weatherData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Data not found in database!");
        return null;
    }

    // Store weather data in cache and database
    public static void storeWeatherData(String location, String weatherData) {
        // Store new data in cache
        cache.put(location, weatherData);

        // Store new data in database if it doesn't already exist
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/weather?user=root&password=Sirsonic1234");

            // Check if location already exists in the database
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM cache WHERE location = ?");
            checkStmt.setString(1, location);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            checkStmt.close();

            if (count == 0) { // If location doesn't exist, insert new record
                // Insert new record
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO cache (location, weatherData) VALUES (?, ?)");
                insertStmt.setString(1, location);
                insertStmt.setString(2, weatherData);
                insertStmt.executeUpdate();
                System.out.println("New data stored in the database for location: " + location);
            } else {
                fetchWeatherDataFromDatabase(location);
                System.out.println("Existing data retrieved for location: " + location);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
