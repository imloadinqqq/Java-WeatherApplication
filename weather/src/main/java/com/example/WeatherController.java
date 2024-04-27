package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeatherController {

    @FXML
    private TextField locationField;

    @FXML
    private Label locationLabel;

    @FXML
    private Label weatherLabel;

    @FXML
    private Label realFeelLabel;

    @FXML
    private ImageView weatherIcon;

    @FXML
    private Label weatherDescriptionLabel;

    @FXML
    private Button searchButton;

    @FXML
    private GridPane gridPane;


    // finish this
//    public void changeBackgroundColor(String weatherDesc) {
//        if (weatherDesc.equals("Sunny")) {
//            gridPane.setBackground(new Background(new BackgroundFill(Color.rgb(150, 180, 228), null, null)));
//        } else if (weatherDesc.equals("Partly cloudy")){
//            gridPane.setBackground(new Background(new BackgroundFill(Color.rgb(150, 180, 228), null, null)));
//        } else {
//            gridPane.setBackground(new Background(new BackgroundFill((Color.ALICEBLUE), null, null)));
//        }
//    }

    @FXML
    public void searchWeather(ActionEvent event) {
        try {
            String location = locationField.getText();
            List<String> locations = new ArrayList<>();
            locations.add(location);
            System.out.println(locations);

            List<String> weatherData = ApiHandler.getWeatherDataMultiThreaded(locations);
            for (String data : weatherData) {
                JSONObject weatherJson = new JSONObject(data);
                int temperature = weatherJson.getJSONObject("current").getInt("temperature");
                int realFeel = weatherJson.getJSONObject("current").getInt("feelslike");
                String iconUrl = String.valueOf(weatherJson.getJSONObject("current").getJSONArray("weather_icons"))
                        .replaceAll("[\\[\\]\"]", "");
                String description = String.valueOf(weatherJson.getJSONObject("current").getJSONArray("weather_descriptions"))
                        .replaceAll("[\\[\\]\"]", "");
                String region = weatherJson.getJSONObject("location").getString("region");

                // Setting values to labels and image
                locationLabel.setText("Location: " + location + ", " + region);
                weatherLabel.setText("Temperature: " + temperature + "°C");
                realFeelLabel.setText("Real Feel: " + realFeel + "°C");
                weatherIcon.setImage(new Image(iconUrl));
                weatherDescriptionLabel.setText(description);

//                changeBackgroundColor(description);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}