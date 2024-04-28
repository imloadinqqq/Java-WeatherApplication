package com.example;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.json.JSONObject;

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

    
    @FXML
    public void searchWeather(ActionEvent event) {
        try {
            String location = locationField.getText();
            String weatherData = ApiHandler.getWeatherData(location);
            JSONObject weatherJson = new JSONObject(weatherData);

            int temperature = getTemperature(weatherJson);
            int realFeel = getRealFeel(weatherJson);
            String iconUrl = getIconUrl(weatherJson);
            String description = getDescription(weatherJson);
            String region = getRegion(weatherJson);

            // Setting values to labels and image
            weatherLabel.setText("Temperature: " + temperature);
            realFeelLabel.setText("Real Feel: " + realFeel);
            weatherDescriptionLabel.setText("Description: " + description);
            locationLabel.setText("Location: " + region);
            weatherIcon.setImage(new Image(iconUrl));

            changeBackgroundColor(weatherIcon.getImage());

            ApiHandler.storeWeatherData(location, weatherData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getTemperature(JSONObject weatherJson) {
        return weatherJson.getJSONObject("current").getInt("temperature");
    }

    private int getRealFeel(JSONObject weatherJson) {
        return weatherJson.getJSONObject("current").getInt("feelslike");
    }

    private String getIconUrl(JSONObject weatherJson) {
        return String.valueOf(weatherJson.getJSONObject("current").getJSONArray("weather_icons"))
                .replaceAll("[\\[\\]\"]", "");
    }

    private String getDescription(JSONObject weatherJson) {
        return String.valueOf(weatherJson.getJSONObject("current").getJSONArray("weather_descriptions"))
                .replaceAll("[\\[\\]\"]", "");
    }

    private String getRegion(JSONObject weatherJson) {
        String city = weatherJson.getJSONObject("location").getString("name");
        String region = weatherJson.getJSONObject("location").getString("region");
        return city + ", " + region;
    }

    public void changeBackgroundColor(Image image) {
        // Get the color of a specific pixel
        PixelReader pixelReader = image.getPixelReader();
        Color color = pixelReader.getColor(1, 1); // replace xCoordinate and yCoordinate with your coordinates
 
        // Change the background color
        BackgroundFill backgroundFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        gridPane.setBackground(background); // replace gridPane with your GridPane object
    }
}