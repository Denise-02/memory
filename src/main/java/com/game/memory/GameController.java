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

    private int level;

  @FXML
  public void initialize() {
      anchorPane.setStyle("-fx-background-color: blue;");
      gridPane.setGridLinesVisible(true);

      //  ( liv x 2 + 2 ) x 2
      // creazione arrayList
      // controllo sul livello per sapere quanti elementi conterrà l'array e quante righe aggiungere

      ArrayList<Integer> cardCouples = new ArrayList<>();
      HashSet<Integer> cards = new HashSet<>();
      int length = level * 2 + 2;     // numero di carte diverse
      RandomGenerator randomGenerator = RandomGenerator.getDefault();
      for (int i = 0; i < length; ++i) {
        cards.add(randomGenerator.nextInt(1, 11));
      }

      cardCouples.addAll(cards);
      cardCouples.addAll(cards);

      Collections.shuffle(cardCouples);

      System.out.println(Arrays.toString(cardCouples.toArray()));

      gridPane.addRow(2, new Label(""));

  }

  void update() {
      lblTheme.textProperty().set("" + game.getTheme());
      lblMode.textProperty().set("" + game.getMode());
      lblLevel.textProperty().set("" + game.getLevel());
      level = Integer.parseInt(lblLevel.getText());
  }



  public Game getGame() { return game; }
    public void setGame(Game game) {
        this.game = game;
        update();
    }

}
