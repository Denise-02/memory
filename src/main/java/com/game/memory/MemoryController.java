package com.game.memory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import java.net.*;

public class MemoryController { //implements Initializable {

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

    @FXML private AnchorPane Anchor;

    @FXML private Label lblProva;


    Game game;

    private int theme;
    private int mode;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */

    //@Override
    @FXML
    public void initialize() {
 //   imgPacman.setImage(new Image("src/main/resources/com/game/memory/memoryImages/c1.png"));
        //imgPacman.setImage("memoryImages/c1.png");
     //   imgPacman = imgPacman;
     //   Path imageFile = (Path) Paths.get("/src/main/resources/com/game/memory/memoryImages/c1.png");
       // src/main/resources/com/game/memory/memoryImages/c1.png
/*        URL url = null;
        try {
            url = new URL("/c1.png");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }*/
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
 //       System.out.println("url = " + imageFile);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
  //      imgPacman.setImage(new Image(url.toURI().toURL().toExternalForm()));


//@memoryImages/c1.png

 //       String url = "file:///Users/denis/OneDrive/Desktop/JPImages/JPImages/Stella.png";   // QUESTO VA! --> OK
    //    String url = "file:///c1.png";
  //      boolean backgroundLoading = true;

// The image is being loaded in the background
 //       imgPacman.setImage(new Image(url, backgroundLoading));

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c1.jpg")));
        imgPacman.setImage(image);

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c2.jpg")));
        imgPokemon.setImage(image);

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c3.jpg")));
        imgSuperMario.setImage(image);

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c4.jpg")));
        imgSpongebob.setImage(image);
    }



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
        btnMode.setText(modeLife.getText());
        showStartBTN();
    }

    @FXML
    public void saveSelectionTime(ActionEvent actionEvent) {
        mode = 2;
        btnMode.setText(modeTime.getText());
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
            Parent root1 = (Parent) fxmlLoader.load();      // da cambiare nome a root1
            stage = new Stage();
            stage.setUserData(game);
            stage.setScene(new Scene(root1));
            stage.show();

            GameController controller = fxmlLoader.getController();

            /**
             * Set the game modalities into the GameController
             */
            controller.setGame(game);
            controller.update();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}