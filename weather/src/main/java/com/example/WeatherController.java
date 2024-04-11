package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import org.json.JSONObject;

public class WeatherController {

    @FXML
    private TextField locationField;

    @FXML
    private Label weatherLabel;

    @FXML
    private Button searchButton;

    @FXML
    public void searchWeather(ActionEvent event) {
        try {
            String location = locationField.getText();
            String weatherData = ApiHandler.getWeatherData(location);
            JSONObject weatherJson = new JSONObject(weatherData);
            int temperature = weatherJson.getJSONObject("current").getInt("temperature");
            weatherLabel.setText("Temperature: " + temperature);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}