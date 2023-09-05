package com.game.memory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class MemoryController {

    @FXML
    private MenuButton btnMode;

    @FXML
    private Button btnStart;

    @FXML
    private ImageView imgPacman;

    @FXML
    private ImageView imgPokemon;

    @FXML
    private ImageView imgSpongebob;
    @FXML
    private ImageView imgSuperMario;

    @FXML
    private MenuItem modeTime;

    @FXML
    private MenuItem modeLife;

    @FXML
    private GridPane gridPane;
    private String theme;
    private int mode;

    /** c
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */

    @FXML
    public void initialize() {
        btnMode.setVisible(false);
        btnStart.setVisible(false);

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/cPacMan.jpg")));
        imgPacman.setImage(image);

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/cPokemon.jpg")));
        imgPokemon.setImage(image);

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/cSuperMario.jpg")));
        imgSuperMario.setImage(image);

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/cSpongebob.jpg")));
        imgSpongebob.setImage(image);

        gameSettings();

    }

    private void gameSettings() {

        gridPane.setOnMouseClicked(mouseEvent -> btnMode.setVisible(true));

        imgPacman.setOnMouseClicked(mouseEvent -> theme = "PacMan");

        imgPokemon.setOnMouseClicked(mouseEvent -> theme = "Pokemon");

        imgSuperMario.setOnMouseClicked(mouseEvent -> theme = "SuperMario");

        imgSpongebob.setOnMouseClicked(mouseEvent -> theme = "Spongebob");

        modeLife.setOnAction(actionEvent -> {
            btnMode.setText(modeLife.getText());
            btnStart.setVisible(true);
        });

        modeTime.setOnAction(actionEvent -> {
            btnMode.setText(modeTime.getText());
            btnStart.setVisible(true);
        });

        mode = btnMode.getText().compareTo("Life") == 0 ? 1 : 2;    // life = 1, time = 2
    }

    @FXML
    public void sendData(MouseEvent event) throws RuntimeException {     // startGame
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Parent view = loader.load();
            stage = new Stage();
            stage.setScene(new Scene(view));
            stage.setTitle("Memory");
            stage.show();

            GameController controller = loader.getController();

            // Set the game modalities into the GameController
            controller.receiveData(theme, mode, 1);
            controller.update();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

/*
 @FXML
 private void sendData(MouseEvent event) {
     // Step 1
     game = new Game(theme,mode,1);
     // Step 2
     Node node = (Node) event.getSource();
     // Step 3
     Stage stage = (Stage) node.getScene().getWindow();
     stage.close();
     try {
         // Step 4
         Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("game-view.fxml")));
         // Step 5
         stage.setUserData(game);
         // Step 6
         Scene scene = new Scene(root);
         stage.setTitle("Memory");
         stage.setScene(scene);
         // Step 7
         stage.show();
     } catch (IOException e) {
         System.err.printf("Error: %s%n", e.getMessage());
     }
 }
        */

            /*
 @FXML
 private void sendData(MouseEvent event) {
     game = new Game(theme,mode,1);

     try {
         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(getClass().getResource("game-view.fxml"));
         DialogPane view = loader.load();
         GameController controller = loader.getController();
// Set the person into the controller.
         controller.receiveData(theme, mode, 1);
// Create the dialog
         Dialog<ButtonType> dialog = new Dialog<>();
         dialog.setTitle("Memory");
         dialog.initModality(Modality.WINDOW_MODAL);
         dialog.setDialogPane(view);

         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         stage.close();     // fa chiudere la schermata del menu

         dialog.showAndWait();

         /*

         Scene scene = new Scene(view);
         stage.setTitle("Memory");
         stage.setScene(scene);



 } catch (IOException | InterruptedException e) {
         throw new RuntimeException(e);
     }
 }*/

}