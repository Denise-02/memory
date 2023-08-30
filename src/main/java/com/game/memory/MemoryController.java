package com.game.memory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.Optional;

import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
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


    Game game;

    private String theme;
    private int mode;

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */

    @FXML
    public void initialize() {
        btnMode.setVisible(false);
        btnStart.setVisible(false);

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c1.jpg")));
        imgPacman.setImage(image);

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c2.jpg")));
        imgPokemon.setImage(image);

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c3.jpg")));
        imgSuperMario.setImage(image);

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c4.jpg")));
        imgSpongebob.setImage(image);

        gameSettings();

    }

    private void gameSettings() {

        gridPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btnMode.setVisible(true);
            }
        });

        imgPacman.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                theme = "PacMan";
            }
        });

        imgPokemon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                theme = "Pokemon";
            }
        });

        imgSuperMario.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                theme = "SuperMario";
            }
        });

        imgSpongebob.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                theme = "Spongebob";
            }
        });

        modeLife.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                btnMode.setText(modeLife.getText());
                btnStart.setVisible(true);
            }
        });

        modeTime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                btnMode.setText(modeTime.getText());
                btnStart.setVisible(true);
            }
        });

        mode = btnMode.getText().compareTo("Life") == 0 ? 1 : 2;    // life = 1, time = 2

   //     game = new Game(theme, mode);
    }

 /*   @FXML
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
            Parent root1 = (Parent) fxmlLoader.load();      // da cambiare nome a root1
            stage = new Stage();
            stage.setUserData(game);
            stage.setScene(new Scene(root1));
            stage.show();

            GameController controller = fxmlLoader.getController();

            /**
             * Set the game modalities into the GameController

            controller.setGame(game);
            controller.update();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        */
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
         System.err.println(String.format("Error: %s", e.getMessage()));
     }
 }

 @FXML
 private void sendProva(MouseEvent event) {
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

         Optional<ButtonType> clickedButton = dialog.showAndWait();

         /*

         Scene scene = new Scene(view);
         stage.setTitle("Memory");
         stage.setScene(scene);
*/


 } catch (IOException e) {
         throw new RuntimeException(e);
     }
 }

}