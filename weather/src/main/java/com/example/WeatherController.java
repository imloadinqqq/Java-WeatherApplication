package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import org.json.JSONObject;

public class WeatherController {

    @FXML
    private TextField locationField;

    @FXML
    private Label locationLabel;

    @FXML
    private Label weatherLabel;

    @FXML
    private Label highTempLabel;

    @FXML
    private Label lowTempLabel;

    @FXML
    private ImageView weatherIcon;

    @FXML
    private Label weatherDescriptionLabel;

    @FXML
    private Button searchButton;

    @FXML
    public void searchWeather(ActionEvent event) {
        try {
            String location = locationField.getText();
            String weatherData = ApiHandler.getWeatherData(location);
            JSONObject weatherJson = new JSONObject(weatherData);
            int temperature = weatherJson.getJSONObject("current").getInt("temperature");
            int highTemp = weatherJson.getJSONObject("forecast").getJSONObject("daily").getInt("high_temp");
            int lowTemp = weatherJson.getJSONObject("forecast").getJSONObject("daily").getInt("low_temp");
            String iconUrl = weatherJson.getJSONObject("current").getString("icon");
            String description = weatherJson.getJSONObject("current").getString("description");
            String region = weatherJson.getJSONObject("location").getString("region");

            // Setting values to labels and image
            locationLabel.setText("Location: " + location + ", " + region);
            weatherLabel.setText("Temperature: " + temperature + "°C");
            highTempLabel.setText("High: " + highTemp + "°C");
            lowTempLabel.setText("Low: " + lowTemp + "°C");
            weatherIcon.setImage(new Image(iconUrl));
            weatherDescriptionLabel.setText(description);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
