package com.game.memory;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class GameController {
    // 4 x 2    --> livello 1
    // 4 x 3    --> livello 2
    // 4 x 4    --> livello 3
    // 4 x 5    --> livello 4

    @FXML
    private GridPane gridPane;
    @FXML
    private AnchorPane anchorPane;

  //  void initialize()
  @FXML
  public void initialize() {
      anchorPane.setStyle("-fx-background-color: blue;");
      gridPane.setGridLinesVisible(true);

  }



}
