package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Box;


import java.io.IOException;

/**
 * JavaFX App
 */
public class WeatherApplication extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        GridPane gridPane = new GridPane();
        Label location = new Label("Location");
        Label temp = new Label("Temp");
        Label h = new Label("H");
        Label l = new Label("L");
        Label search = new Label("Search");
        TextField searchField = new TextField();
        Label desc = new Label("Desc.");
        Label descInfo = new Label("Desc. Info");
        Label forecast = new Label("Forecast");
        Box forecastBox = new Box();
        forecastBox.setHeight(50);
        forecastBox.setWidth(150);

        GridPane.setConstraints(location, 0, 0);
        GridPane.setConstraints(temp, 1, 0);
        GridPane.setConstraints(h, 2, 0);
        GridPane.setConstraints(l, 3, 0);
        GridPane.setConstraints(search, 4, 0);
        GridPane.setConstraints(searchField, 5, 0);
        GridPane.setConstraints(desc, 0, 1);
        GridPane.setConstraints(descInfo, 1, 1);
        GridPane.setConstraints(forecast, 2, 1);
        GridPane.setConstraints(forecastBox, 3, 1);

        Scene scene = new Scene(gridPane, 500, 300);

        gridPane.getChildren().addAll(location, temp, h, l, search, searchField, desc, descInfo, forecast, forecastBox);

        stage.setTitle("Weather");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}