package com.game.memory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MemoryApplication extends Application {
    /**
     * "memory-view.fxml" is set to be shown when the application is launched. It is managed by MemoryController.java
     * @param stage the primary stage for this application, onto which the application scene can be set.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MemoryApplication.class.getResource("memory-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Memory Menu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}