package com.example.viergewinntbox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/com/example/viergewinntbox/Oberflaeche.fxml"));

        // Titel der Seite
        primaryStage.setTitle("4 Gewinnt");

        // Erstellen einer Szene
        Scene scene = new Scene(root, 500, 500);

        // Lade die CSS-Datei für das Styling
        scene.getStylesheets().add(getClass().getResource("/com/example/viergewinntbox/style.css").toExternalForm());

        // Setze die feste Größe des Fensters
        primaryStage.setResizable(false);

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