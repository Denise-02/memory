module com.game.memory {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.game.memory to javafx.fxml;
    exports com.game.memory;
}