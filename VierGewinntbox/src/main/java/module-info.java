module com.example.viergewinntbox {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.viergewinntbox to javafx.fxml;
    exports com.example.viergewinntbox;
}

