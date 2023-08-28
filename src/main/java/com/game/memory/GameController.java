package com.game.memory;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.*;
import java.util.random.RandomGenerator;

/* https://www.datainfinities.com/27/pass-event-and-parameter-onclick-in-react#:~:text=To%20pass%20event%20and%20parameter%20onClick%20in%20React%2C%20declare%20an,an%20element(eg%20button).
* How do you pass an event as a parameter?
* To pass event and parameter onClick in React, declare an event handler
* function and then pass it to the onClick prop of the element inside an inline arrow
* function. Using React onClick event handler you can call a function and trigger an
* action when a user clicks an element(eg button).
* */

public class GameController {
    // 4 x 2    --> livello 1: 4 immagini
    // 4 x 3    --> livello 2: 6
    // 4 x 4    --> livello 3: 8
    // 4 x 5    --> livello 4: 10


    @FXML
    private GridPane gridPane;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label lblTheme;
    @FXML
    private Label lblMode;
    @FXML
    private Label lblLevel;

    Game game;

    /**
     * The level of the game. For each level the number of cards increases.
     */

   // @FXML
    private int level;
   // @FXML
    private int mode;
  //  @FXML
    private int theme;

   // @FXML private Button btnReceive;



    @FXML
  public void initialize() {
        game = new Game(1,1,1);

        anchorPane.setStyle("fx-background-color: pink");
   // btnReceive = new Button();

     //   receiveData(new MouseEvent(onAction)));



      anchorPane.setStyle("-fx-background-color: blue;");
      gridPane.setGridLinesVisible(true);

      //  ( liv x 2 + 2 ) x 2
      // controllo sul livello per sapere quanti elementi conterrà l'array e quante righe aggiungere



      HashSet<Integer> cards = new HashSet<>();
      ArrayList<Integer> cardCouples = new ArrayList<>();
      System.out.println("The level is: " + level);
      System.out.println("The theme is: " + theme);
      System.out.println("The mode is: " + mode);
      int length = level * 2 + 2;     // numero di carte diverse
      RandomGenerator randomGenerator = RandomGenerator.getDefault();

    System.out.println("The length is: " + length);
      int j = 0;
      while (j < length){
          if (cards.add(randomGenerator.nextInt(1, 11))){
              j++;
          }
      }


      cardCouples.addAll(cards);
      cardCouples.addAll(cards);

      Collections.shuffle(cardCouples);

      System.out.println(Arrays.toString(cardCouples.toArray()));

      gridPane.addRow(1, new Label("New line"));

  }

    @FXML
    private void receiveData(MouseEvent event) {
        // Step 1
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        // Step 2
        game = (Game) stage.getUserData();
        // Step 3
        theme = game.getTheme();
        mode = game.getMode();
        level = game.getLevel();
    }


  @FXML
  void update() {
      theme = game.getTheme();
      mode = game.getMode();
      level = game.getLevel();
      System.out.println("I say the level is: " + level);

      lblTheme.textProperty().set("" + theme);
      lblMode.textProperty().set("" + mode);
      lblLevel.textProperty().set("" + level);

  }

  public Game getGame() { return game; }
  public void setGame(Game game) {
     this.game = game;
    // update();
  }
}
