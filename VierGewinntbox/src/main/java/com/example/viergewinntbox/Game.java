package com.example.viergewinntbox;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;

    private static final String RED_COLOR = "-fx-background-color: red;"; // Spieler 1 (Rot)
    private static final String BLUE_COLOR = "-fx-background-color: blue;"; // Spieler 2 (Blau)

    private static final String DEFAULT_PLAYER1_NAME = "Spieler Rot";
    private static final String DEFAULT_PLAYER2_NAME = "Spieler Blau";

    private final Button[][] buttons = new Button[ROWS][COLUMNS];
    private boolean currentPlayer = true; // Spieler 1 = true (Rot), Spieler 2 = false (Blau)

    private String player1Name = DEFAULT_PLAYER1_NAME;
    private String player2Name = DEFAULT_PLAYER2_NAME;

    private String player1Color = "-fx-background-color: red;";
    private String player2Color = "-fx-background-color: blue;";


    @FXML
    private GridPane grid;

    @FXML
    private Button fullscreenButton;

    @FXML
    private ImageView backgroundImage;


    // Initialisiert das Spiel beim Starten
    @FXML
    public void initialize() {
        getPlayerSettings();
        initializeButtons();
        setupFullscreenButton();
        Platform.runLater(this::setupBackgroundResize);
    }

    private void setupBackgroundResize() {
        // Hol die Hauptbühne (Stage)
        Stage stage = (Stage) backgroundImage.getScene().getWindow();

        // Reagiere auf Änderungen der Fensterbreite
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            backgroundImage.setFitWidth(newValue.doubleValue());
        });

        // Reagiere auf Änderungen der Fensterhöhe
        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            backgroundImage.setFitHeight(newValue.doubleValue());
        });
    }


    // Erstellt die Buttons für das Spielfeld und fügt sie dem GridPane hinzu
    private void initializeButtons() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                createButton(row, col);
            }
        }
    }

    // Erstellt einen einzelnen Button und fügt ihn in die Zelle ein
    private void createButton(int row, int col) {
        Button button = new Button(); // Neuer Button
        button.setMinSize(50, 50); // Größe des Buttons festlegen
        button.setOnAction(event -> handleButtonClick(row, col)); // Aktion, wenn der Button geklickt wird
        buttons[row][col] = button; // Button im Array speichern
        grid.add(button, col, row); // Button im Spielfeld-Grid anzeigen
    }

    // Konfiguriert den Button für den Vollbildmodus
    private void setupFullscreenButton() {
        fullscreenButton.setOnAction(event -> toggleFullscreenMode()); // Vollbildmodus umschalten
    }

    // Wird aufgerufen, wenn ein Spieler auf eine Zelle klickt
    private void handleButtonClick(int row, int col) {
        // Suche die unterste leere Zelle in der Spalte
        for (int r = ROWS - 1; r >= 0; r--) {
            if (buttons[r][col].getStyle().isEmpty()) {
                setButtonColor(buttons[r][col]); // Farbe des aktuellen Spielers setzen
                if (checkGameOver(r, col)) return; // Überprüfen, ob das Spiel beendet ist
                currentPlayer = !currentPlayer; // Spieler wechseln
                break;
            }
        }
    }

    // Setzt die Farbe des Buttons entsprechend dem aktuellen Spieler
    private void setButtonColor(Button button) {
        if (currentPlayer) {
            button.setStyle(player1Color);
        } else {
            button.setStyle(player2Color);
        }
    }

    // Überprüft, ob das Spiel gewonnen oder unentschieden ist
    private boolean checkGameOver(int row, int col) {
        if (checkWin(row, col)) {
            if (currentPlayer) {
                showWinnerAlert(player1Name);
            } else {
                showWinnerAlert(player2Name);
            }
            return true;
        } else if (isBoardFull()) {
            showDrawAlert();
            return true;
        } else {
            return false;
        }
    }

    // Überprüft, ob der aktuelle Spieler eine Gewinnbedingung erfüllt
    private boolean checkWin(int row, int col) {
        String currentColor = currentPlayer ? player1Color : player2Color;

        // Prüft die vier Richtungen: Horizontal, Vertikal, Diagonal (\ und /)
        return (checkDirection(row, col, 0, 1, currentColor) >= 4 || // Horizontal
                checkDirection(row, col, 1, 0, currentColor) >= 4 || // Vertikal
                checkDirection(row, col, 1, 1, currentColor) >= 4 || // Diagonal (\)
                checkDirection(row, col, 1, -1, currentColor) >= 4); // Diagonal (/)
    }

    // Zählt die zusammenhängenden Felder in einer Richtung
    private int checkDirection(int row, int col, int rowDir, int colDir, String color) {
        List<Button> winningButtons = new ArrayList<>(); // Liste für Gewinn-Buttons
        winningButtons.add(buttons[row][col]);
        int count = countInDirection(row, col, rowDir, colDir, color, winningButtons) +
                countInDirection(row, col, -rowDir, -colDir, color, winningButtons) + 1; // +1 für das aktuelle Feld

        if (count >= 4) { // Wenn mindestens 4 Felder zusammenhängen
            highlightWinningButtons(winningButtons); // Diese Felder markieren
        }
        return count;
    }

    // Zählt die zusammenhängenden Felder in einer einzelnen Richtung
    private int countInDirection(int row, int col, int rowDir, int colDir, String color, List<Button> winningButtons) {
        int count = 0;
        int currentRow = row + rowDir;
        int currentCol = col + colDir;

        while (currentRow >= 0 && currentRow < ROWS && currentCol >= 0 && currentCol < COLUMNS) {
            if (buttons[currentRow][currentCol].getStyle().equals(color)) {
                count++;
                winningButtons.add(buttons[currentRow][currentCol]);
                currentRow += rowDir; // Gehe in die Richtung weiter
                currentCol += colDir;
            } else {
                break;
            }
        }

        return count;
    }

    private void highlightWinningButtons(List<Button> winningButtons) {
        String borderColor = currentPlayer ? player1Color : player2Color;
        String winningStyle = "-fx-border-color: " + extractColorFromStyle(borderColor) + "; -fx-border-width: 5px; -fx-background-color: lightgreen;";

        for (Button button : winningButtons) {
            button.setStyle(winningStyle);
        }
    }

    // Hilfsmethode: Extrahiert die RGB-Farbe aus dem CSS-Stil
    private String extractColorFromStyle(String style) {
        // Der Stil sieht z. B. so aus: "-fx-background-color: red;"
        if (style.startsWith("-fx-background-color: ")) {
            return style.replace("-fx-background-color: ", "").replace(";", "").trim();
        }
        return "gray"; // Fallback-Farbe
    }

    // Überprüft, ob das Spielfeld vollständig gefüllt ist
    private boolean isBoardFull() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (buttons[row][col].getStyle().isEmpty()) { // Leeres Feld gefunden
                    return false;
                }
            }
        }
        return true; // Spielfeld ist voll
    }

    // Zeigt eine Nachricht, wenn ein Spieler gewinnt
    private void showWinnerAlert(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spiel beendet");
        alert.setHeaderText(null);
        alert.setContentText(winner + " hat gewonnen!"); // Gewinner-Nachricht
        alert.showAndWait(); // Warte, bis der Benutzer die Nachricht bestätigt
        handleNewGame();
    }

    // Zeigt eine Nachricht bei einem Unentschieden
    private void showDrawAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Unentschieden");
        alert.setHeaderText(null);
        alert.setContentText("Das Spiel endet unentschieden!");
        alert.showAndWait(); // Warte, bis der Benutzer die Nachricht bestätigt
        handleNewGame();
    }

    private void getPlayerSettings() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Spielereinstellungen");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Name und Farbauswahl für Spieler 1
        TextField name1 = new TextField(player1Name);
        ComboBox<String> color1 = new ComboBox<>();
        color1.getItems().addAll("Rot", "Orange", "Gelb", "Grün", "Blau", "Lila", "Braun");
        color1.setValue("Rot");

        // Name und Farbauswahl für Spieler 2
        TextField name2 = new TextField(player2Name);
        ComboBox<String> color2 = new ComboBox<>();
        color2.getItems().addAll("Rot", "Orange", "Gelb", "Grün", "Blau", "Lila", "Braun");
        color2.setValue("Blau");

        grid.add(new Label("Spieler 1 Name:"), 0, 0);
        grid.add(name1, 1, 0);
        grid.add(new Label("Farbe:"), 0, 1);
        grid.add(color1, 1, 1);

        grid.add(new Label("Spieler 2 Name:"), 0, 2);
        grid.add(name2, 1, 2);
        grid.add(new Label("Farbe:"), 0, 3);
        grid.add(color2, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Validierung für gleiche Farben
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                if (color1.getValue().equals(color2.getValue())) {
                    showErrorAlert("Beide Spieler dürfen nicht dieselbe Farbe wählen!");
                    return null; // Rückgabe null blockiert den Dialogabschluss
                }
                return ButtonType.OK;
            }
            return dialogButton;
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (name1.getText().isEmpty()) {
                player1Name = "Spieler 1";
            } else {
                player1Name = name1.getText();
            }

            if (name2.getText().isEmpty()) {
                player2Name = "Spieler 2";
            } else {
                player2Name = name2.getText();
            }

            player1Color = mapColorNameToStyle(color1.getValue());
            player2Color = mapColorNameToStyle(color2.getValue());
        }
    }

    // Hilfsmethode: Zeigt eine Fehlermeldung
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private String mapColorNameToStyle(String colorName) {
        return switch (colorName) {
            case "Rot" -> "-fx-background-color: red;";
            case "Orange" -> "-fx-background-color: orange;";
            case "Gelb" -> "-fx-background-color: yellow;";
            case "Grün" -> "-fx-background-color: green;";
            case "Blau" -> "-fx-background-color: blue;";
            case "Lila" -> "-fx-background-color: purple;";
            case "Braun" -> "-fx-background-color: brown;";
            default -> "-fx-background-color: gray;";
        };
    }



    // Startet ein neues Spiel und setzt das Spielfeld zurück
    @FXML
    public void handleNewGame() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                buttons[row][col].setStyle(""); // Alle Zellen leeren
            }
        }
        currentPlayer = true; // Spieler 1 beginnt
    }

    // Vollbildmodus
    private void toggleFullscreenMode() {
        Stage stage = (Stage) grid.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void handleClose() {
        System.exit(0);
    }

    @FXML
    private void handleAbout() {
        // Erstelle eine neue Alert-Box mit Informationstyp
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Über 4 Gewinnt");
        alert.setHeaderText("Über das Spiel");
        alert.setContentText("4 Gewinnt ist ein Strategiespiel für zwei Spieler.\n" +
                "Ziel ist es, vier Chips in einer Reihe zu platzieren, " +
                "horizontal, vertikal oder diagonal.\n\n");

        // Zeige das Fenster und warte, bis der Benutzer es schließt
        alert.showAndWait();
    }

    @FXML
    private void handleCredits() {
        // Erstelle eine neue Alert-Box mit Informationstyp
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Credits");
        alert.setHeaderText("Entwickler und Mitwirkende");
        alert.setContentText("Dieses Spiel wurde entwickelt:\n" +
                "- Ahmet-Can Korkmaz\n" +
                "- Patrick Nadler\n" +
                "- Berkant-Yasin Sener\n" +
                "- Nemanja Vladejic\n\n" +
                "Besonderer Dank an:\n" +
                "- Andrea Horvath\n" +
                "- Christoph Vogl\n\n" +
                "Erfinder des Spiels 4 Gewinnt:\n" +
                "- Howard Wexler\n" +
                "- Ned Strongin\n\n" +
                "Danke fürs Spielen!");
        alert.showAndWait();  // Zeige das Fenster und warte, bis der Benutzer es schließt
    }
}