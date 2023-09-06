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
         * btnMode turns visible when gridPane is clicked.
         */
        gridPane.setOnMouseClicked(mouseEvent -> btnMode.setVisible(true));

        /**
         * Game.theme is set depending on the clicked image.
         */
        imgPacman.setOnMouseClicked(mouseEvent -> theme = "PacMan");
        imgPokemon.setOnMouseClicked(mouseEvent -> theme = "Pokemon");
        imgSuperMario.setOnMouseClicked(mouseEvent -> theme = "SuperMario");
        imgSpongebob.setOnMouseClicked(mouseEvent -> theme = "Spongebob");

        /**
         * btnStart turns visible when Game.mode is set.
         */
        modeLife.setOnAction(actionEvent -> {
            btnMode.setText(modeLife.getText());
            btnStart.setVisible(true);
        });

        modeTime.setOnAction(actionEvent -> {
            btnMode.setText(modeTime.getText());
            btnStart.setVisible(true);
        });

        /**
         * Game.mode is set as:
         * 1 if "life" was chosen, 2 otherwise
         */
        mode = btnMode.getText().compareTo("Life") == 0 ? 1 : 2;
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
            Parent view = loader.load();
            stage = new Stage();
            stage.setScene(new Scene(view));
            //        stage.setResizable(false);
            stage.setTitle("Memory");
/*
            //   Stage window = PrimaryStage;
            VBox layout = new VBox(10);
            //multiply to set size (0.80 is like 80% of the window)
            layout.prefWidthProperty().bind(stage.widthProperty().multiply(1.00));
*/
            stage.show();

            GameController controller = loader.getController();

            /**
             * Set the game modalities into GameController.
             */
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