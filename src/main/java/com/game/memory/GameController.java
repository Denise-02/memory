package com.game.memory;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.*;
import java.util.random.RandomGenerator;

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

    @FXML
    private int level;
    @FXML
    private int mode;
    @FXML
    private int theme;


    @FXML
  public void initialize() {

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
