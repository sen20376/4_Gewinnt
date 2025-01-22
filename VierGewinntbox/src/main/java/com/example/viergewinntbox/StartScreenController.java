package com.example.viergewinntbox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartScreenController {
    @FXML
    private void handleStartGame(ActionEvent event) {
        try {
            // Lade die Hauptspielszene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/viergewinntbox/GUI.fxml"));
            Scene gameScene = new Scene(loader.load());

            // CSS-Datei für das Hauptspiel hinzufügen
            gameScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/viergewinntbox/style.css")).toExternalForm());

            // Erstelle eine neue Bühne (Stage) für das Hauptspiel
            Stage gameStage = new Stage();
            gameStage.setScene(gameScene);
            gameStage.setTitle("4 Gewinnt");
            gameStage.setResizable(false);
            gameStage.show();

            // Setze das Icon für die GUI-Stage
            gameStage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/viergewinntbox/icon.png"))));


            // Schließe den aktuellen Startbildschirm
            Stage startStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            startStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}