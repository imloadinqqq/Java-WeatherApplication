package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiHandler {

    private static final String API_URL = "http://api.weatherstack.com/current?access_key=3599e22e5bc1eb0475e72c6bce3995a7&query=";
    private static final int THREAD_COUNT = 10; // Adjust as needed

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

    public static List<String> getWeatherDataMultiThreaded(List<String> locations) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<String>> futures = new ArrayList<>();

        for (String location : locations) {
            Callable<String> callable = () -> getWeatherData(location);
            Future<String> future = executor.submit(callable);
            futures.add(future);
        }

        executor.shutdown();

        List<String> results = new ArrayList<>();
        for (Future<String> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}