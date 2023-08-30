package com.game.memory;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
    private Label lblTheme;
    @FXML
    private Label lblMode;
    @FXML
    private Label lblLevel;

    @FXML private AnchorPane anchorPane;
    Game game;

    @FXML private HBox hBox;



    @FXML private ImageView imageView;


    private String theme;
    private int mode;
    private int level;

    /**
     * The level of the game. For each level the number of cards increases.
     */
    @FXML
    public void initialize() {
        game = new Game("", 1, 1);
     //   gridPane.setGridLinesVisible(true);
        anchorPane = new AnchorPane();
        anchorPane.setStyle("fx-background-color: pink");
/*
        hBox = new HBox(15);
        hBox.setPadding(new Insets(50, 150, 50, 60));
        hBox.getChildren().addAll(gridPane, lblTheme, lblMode, lblLevel);
*/

    }
    public void receiveData(String theme, int mode, int level) {
        game.setTheme(theme);
        game.setMode(mode);
        game.setLevel(level);

        update();
    }

    @FXML
    void update() {
        theme = game.getTheme();
        mode = game.getMode();
        level = game.getLevel();
        System.out.println("I say the theme is: " + theme);

        lblTheme.textProperty().set("" + theme);
        lblMode.textProperty().set("" + mode);
        lblLevel.textProperty().set("" + level);

        initializeGame();
    }


  @FXML
    private void initializeGame() {
        //  ( liv x 2 + 2 ) x 2
        // controllo sul livello per sapere quanti elementi conterrà l'array e quante righe aggiungere

        HashSet<Integer> cards = new HashSet<>();
        ArrayList<Integer> cardCouples = new ArrayList<>();
        System.out.println("The level is: " + game.getLevel());
        System.out.println("The theme is: " + game.getTheme());
        System.out.println("The mode is: " + game.getMode());
        int length = game.getLevel() * 2 + 2;     // numero di carte diverse
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
        for (int i = 1; i <= game.getLevel(); ++i) {
            StackPane stackPane = new StackPane();
            stackPane.setStyle("fx-background-color: green");
            gridPane.addRow(i, new StackPane(), new StackPane(), new StackPane(), new StackPane());
        }

        int count = 0;
        for (int cols = 0; cols < 4; ++cols) {    // 4 costante e
          for (int rows = 0; rows < game.getLevel() + 1; ++rows) {
                System.out.println("memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg");
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg")), 200, 200, true, true);
                ImageView imageView1 = new ImageView();
                imageView1.setImage(image);

                gridPane.add(imageView1, cols, rows);
              ++count;

          }
        }
/* a
        // timer
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

            }
        }
*/

    }

  public Game getGame() { return game; }
  public void setGame(Game game) {
     this.game = game;
    // update();
  }
}
