package de.sbayat.sbtablereservationmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/de/sbayat/sbtablereservationmanagementsystem/primary-window.fxml"));
        Parent     root       = fxmlLoader.load();
        Scene      scene      = new Scene(root, 800, 600);
        stage.setTitle("Restaurant Reservation Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}   