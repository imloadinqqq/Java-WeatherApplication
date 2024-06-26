package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class ApiHandler {

    private static final String API_URL = "http://api.weatherstack.com/current?access_key=&query=";

    // Initialize the database with a new cache table
    public static void initializeDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/weather?user=root&password=");

            // Drop existing cache table if it exists
            PreparedStatement dropTableStmt = conn.prepareStatement("DROP TABLE IF EXISTS cache");
            dropTableStmt.executeUpdate();
            System.out.println("Existing cache table dropped");

            // Create new cache table
            PreparedStatement createTableStmt = conn.prepareStatement("CREATE TABLE cache (location VARCHAR(255) PRIMARY KEY, weatherData TEXT)");
            createTableStmt.executeUpdate();
            System.out.println("New cache table created");

            conn.close();
            System.out.println("Database initialized successfully\n");
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
            long endTime = System.currentTimeMillis();
            System.out.println("Database retrieval time: " + (endTime - startTime) + " ms");
            return weatherDataFromDB;
        }

        // If data not found in database, fetch from API
        String weatherDataFromAPI = fetchWeatherDataFromAPI(location);
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
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/weather?user=root&password=");

            PreparedStatement pstmt = conn.prepareStatement("SELECT weatherData FROM cache WHERE location = ?");
            pstmt.setString(1, location);
            ResultSet rs = pstmt.executeQuery();

            // Fetching information from result set
            if (rs.next()) {
                String weatherData = rs.getString("weatherData");
                conn.close();
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
        // Store new data in database if it doesn't already exist
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/weather?user=root&password=");

            // Check if location already exists in the database
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM cache WHERE location = ?");
            checkStmt.setString(1, location);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            checkStmt.close();

            // If location doesn't exist, insert new record
            if (count == 0) {
                // Insert new record
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO cache (location, weatherData) VALUES (?, ?)");
                insertStmt.setString(1, location);
                insertStmt.setString(2, weatherData);
                insertStmt.executeUpdate();
                System.out.println("New data stored in the database for location: " + location + "\n");
            } else {
                // Location does exist then fetch the database info
                fetchWeatherDataFromDatabase(location);
                System.out.println("Existing data retrieved for location: " + location + "\n");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
