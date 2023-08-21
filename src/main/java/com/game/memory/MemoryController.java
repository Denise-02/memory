package com.game.memory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    Game game;

    private int theme;
    private int mode;

    public void showModeBTN() {
        btnMode.setStyle("-fx-visibitily: visible");
    }

    @FXML
    public void saveSelectionPacman(MouseEvent mouseEvent) {
        theme = 1;
        showModeBTN();
    }

    @FXML
    public void saveSelectionPokemon(MouseEvent mouseEvent) {
        theme = 2;
        showModeBTN();
    }

    @FXML
    public void saveSelectionSuperMario(MouseEvent mouseEvent) {
        theme = 3;
        showModeBTN();
    }

    @FXML
    public void saveSelectionSpongebob(MouseEvent mouseEvent) {
        theme = 4;
        showModeBTN();
    }

    public void showStartBTN() {
        btnStart.setStyle("-fx-visibitily: visible");
    }

    @FXML
    public void saveSelectionLife(ActionEvent actionEvent) {
        mode = 1;
        showStartBTN();
    }

    @FXML
    public void saveSelectionTime(ActionEvent actionEvent) {
        mode = 2;
        showStartBTN();
    }

    @FXML
    public void sendData(ActionEvent actionEvent) throws IOException {     // startGame
        //  invio dati al GameController
            // Step 1

            game = new Game(theme, mode);
            // Step 2
            Node node = (Node) actionEvent.getSource();
            // Step 3
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

        // cambio view
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage = new Stage();
            stage.setUserData(game);
            stage.setScene(new Scene(root1));
            stage.show();


            GameController controller = fxmlLoader.getController();

            // Set an empty person into the controller
            controller.setGame(new Game(theme, mode));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}