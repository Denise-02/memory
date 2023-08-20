package com.game.memory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class MemoryController {

    @FXML
    private MenuButton btnMode;

    @FXML
    private Button btnStart;


// per controlli dentro a showStartBTN
    @FXML
    private ImageView imgPacman;

    @FXML
    private ImageView imgPokemon;

    @FXML
    private ImageView imgSpongebob;
    @FXML
    private ImageView imgSuperMario;

    public void showModeBTN(MouseEvent mouseEvent) {
        btnMode.setStyle("-fx-visibitily: visible");
        // da fare controlli su id per sapere con che immagini riempire l'array

    }

    public void showStartBTN(ActionEvent actionEvent) {
        btnStart.setStyle("-fx-visibitily: visible");
        // da fare controlli su id per sapere se tempo o vite


    }

    public void startGame(ActionEvent actionEvent) throws IOException {
        // cambio view
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}