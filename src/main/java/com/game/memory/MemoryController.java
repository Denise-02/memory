package com.game.memory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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
}