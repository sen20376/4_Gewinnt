package com.example.viergewinntbox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/viergewinntbox/StartScreen.fxml"));
        Scene scene = new Scene(loader.load(), 500, 500);
        // Titel der Seite
        primaryStage.setTitle("4 Gewinnt");

        // Lade die CSS-Datei für das Styling
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/viergewinntbox/style.css")).toExternalForm());

        // Setze die feste Größe des Fensters
        primaryStage.setResizable(false);

        // Setze das Icon
        primaryStage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/viergewinntbox/icon.png"))));

        // Setze die Szene auf die Hauptbühne
        primaryStage.setScene(scene);

        // Zeige das Fenster an
        primaryStage.show();

        // Listener für Vollbild-Status (optional, um bei Bedarf Aktionen zu ergänzen)
        primaryStage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Vollbildmodus aktiviert");
            } else {
                System.out.println("Vollbildmodus deaktiviert");
            }
        });
    }

    public static void main(String[] args) {
        // Starte die JavaFX-Anwendung
        launch(args);
    }
}