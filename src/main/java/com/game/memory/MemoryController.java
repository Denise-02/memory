package com.game.memory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

    /**
     * Initializes the MemoryController class.
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

    /**
     * Sets the events on each node of the controller.
     */
    private void gameSettings() {
        /**
         * Game.theme is set depending on the clicked image. btnMode turns visible when a theme is clicked.
         */
        imgPacman.setOnMouseClicked(mouseEvent -> { theme = "PacMan"; btnMode.setVisible(true); });
        imgPokemon.setOnMouseClicked(mouseEvent -> { theme = "Pokemon"; btnMode.setVisible(true); });
        imgSuperMario.setOnMouseClicked(mouseEvent -> { theme = "SuperMario"; btnMode.setVisible(true); });
        imgSpongebob.setOnMouseClicked(mouseEvent -> { theme = "Spongebob"; btnMode.setVisible(true); });

        /**
         * btnStart turns visible when Game. Mode is set.
         */
        modeLife.setOnAction(actionEvent -> {
            btnMode.setText(modeLife.getText());
            btnStart.setVisible(true);
            mode = 1;
        });

        modeTime.setOnAction(actionEvent -> {
            btnMode.setText(modeTime.getText());
            btnStart.setVisible(true);
            mode = 2;
        });

        /**
         * Game.mode is set as:
         * 1 if "life" was chosen, 2 otherwise
         */
    //    mode = btnMode.getText().compareTo("Mode") == 0 ? 1 : 2;
    }

    /**
     * This method links this controller to GameController class when btnStart is clicked.
     * @param event btnStart onMouseClicked
     * @throws RuntimeException
     */
    @FXML
    public void sendData(MouseEvent event) throws RuntimeException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 550);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Memory");
            stage.setResizable(false);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/others/brain.png"))));
            FXMLLoader fxmlLoader = new FXMLLoader(MemoryApplication.class.getResource("memory-view.fxml"));
            stage.show();

            GameController controller = loader.getController();

            /**
             * Set the game modalities into GameController.
             */
            controller.receiveData(theme, mode, 1);
            controller.update();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}