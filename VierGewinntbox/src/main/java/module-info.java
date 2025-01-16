module com.example.viergewinntbox {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.viergewinntbox to javafx.fxml;
    exports com.example.viergewinntbox;
}