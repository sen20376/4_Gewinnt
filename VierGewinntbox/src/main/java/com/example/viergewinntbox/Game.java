package com.example.viergewinntbox;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;

    private static final String RED_COLOR = "-fx-background-color: red;"; // Spieler 1 (Rot)
    private static final String BLUE_COLOR = "-fx-background-color: blue;"; // Spieler 2 (Blau)
    private static final String WINNER_STYLE = "-fx-border-color: lightgreen; -fx-border-width: 3px;"; // Stil der Gewinn-Markierung

    private static final String DEFAULT_PLAYER1_NAME = "Spieler Rot";
    private static final String DEFAULT_PLAYER2_NAME = "Spieler Blau";

    private Button[][] buttons = new Button[ROWS][COLUMNS];
    private boolean currentPlayer = true; // Spieler 1 = true (Rot), Spieler 2 = false (Blau)
    private String player1Name = DEFAULT_PLAYER1_NAME;
    private String player2Name = DEFAULT_PLAYER2_NAME;

    @FXML
    private GridPane grid;

    @FXML
    private Button fullscreenButton;

    // Initialisiert das Spiel beim Starten
    @FXML
    public void initialize() {
        getPlayerNames();
        initializeButtons();
        setupFullscreenButton();
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
            button.setStyle(RED_COLOR); // Spieler 1 (Rot)
        } else {
            button.setStyle(BLUE_COLOR); // Spieler 2 (Blau)
        }
    }

    // Überprüft, ob das Spiel gewonnen oder unentschieden ist
    private boolean checkGameOver(int row, int col) {
        if (checkWin(row, col)) {
            showWinnerAlert(currentPlayer ? player1Name : player2Name);
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
        String currentColor;
        if (currentPlayer) {
            currentColor = RED_COLOR;
        } else {
            currentColor = BLUE_COLOR;
        }

        // Prüft die vier Richtungen: Horizontal, Vertikal, Diagonal (\ und /)
        return (checkDirection(row, col, 0, 1, currentColor) >= 4 || // Horizontal
                checkDirection(row, col, 1, 0, currentColor) >= 4 || // Vertikal
                checkDirection(row, col, 1, 1, currentColor) >= 4 || // Diagonal (\)
                checkDirection(row, col, 1, -1, currentColor) >= 4); // Diagonal (/)
    }

    // Zählt die zusammenhängenden Felder in einer Richtung
    private int checkDirection(int row, int col, int rowDir, int colDir, String color) {
        List<Button> winningButtons = new ArrayList<>(); // Liste für Gewinn-Buttons
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

    // Hebt die Gewinn-Buttons hervor
    private void highlightWinningButtons(List<Button> winningButtons) {
        for (int i = 0; i < winningButtons.size(); i++) {
            winningButtons.get(i).setStyle(WINNER_STYLE);
        }
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

    // Fragt die Namen der Spieler ab
    private void getPlayerNames() {
        player1Name = getPlayerName("Spieler Rot");
        player2Name = getPlayerName("Spieler Blau");
    }

    // Öffnet ein Eingabefeld für die Namensabfrage
    private String getPlayerName(String defaultName) {
        TextInputDialog dialog = new TextInputDialog(defaultName);
        dialog.setTitle("Spieler-Namen");
        dialog.setHeaderText(null);
        dialog.setContentText("Name für " + defaultName + ":");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(defaultName); // Falls keine Eingabe, Standardname verwenden
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
}
