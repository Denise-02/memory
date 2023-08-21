package com.game.memory;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameController {
    // 4 x 2    --> livello 1
    // 4 x 3    --> livello 2
    // 4 x 4    --> livello 3
    // 4 x 5    --> livello 4

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


  @FXML
  public void initialize() {
      anchorPane.setStyle("-fx-background-color: blue;");
      gridPane.setGridLinesVisible(true);

      // creazione arrayList

      gridPane.addRow(2, new Label(""));

  }

  void update() {
      lblTheme.textProperty().set("" + game.getTheme());
      lblMode.textProperty().set("" + game.getMode());
      lblLevel.textProperty().set("" + game.getLevel());
  }



  public Game getGame() { return game; }
    public void setGame(Game game) {
        this.game = game;
        update();
    }



}
