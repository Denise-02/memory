package com.game.memory;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.*;
import java.util.random.RandomGenerator;

/* https://www.datainfinities.com/27/pass-event-and-parameter-onclick-in-react#:~:text=To%20pass%20event%20and%20parameter%20onClick%20in%20React%2C%20declare%20an,an%20element(eg%20button).
 * How do you pass an event as a parameter?
 * To pass event and parameter onClick in React, declare an event handler
 * function and then pass it to the onClick prop of the element inside an inline arrow
 * function. Using React onClick event handler you can call a function and trigger an
 * action when a user clicks an element(e.g. button).
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

    Game game;

    private final int NCOLS = 4;

    private int checked = 0;    // numero di coppie trovate

    /**
     * The level of the game. For each level the number of cards increases.
     */
    @FXML
    public void initialize() {
        game = new Game("", 1, 1);
        gridPane.setGridLinesVisible(true);
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
        lblTheme.textProperty().set("" + game.getTheme());
        lblMode.textProperty().set("" + game.getMode());
        lblLevel.textProperty().set("" + game.getLevel());

        initializeGame();
    }


    @FXML
    private void initializeGame() {
        //  ( liv x 2 + 2 ) x 2
        // controllo sul livello per sapere quanti elementi conterrà l'array e quante righe aggiungere
        //  game.setLevel(4);
        HashSet<Integer> cards = new HashSet<>();
        ArrayList<Integer> cardCouples = new ArrayList<>();
        System.out.println("The level is: " + game.getLevel());
        System.out.println("The theme is: " + game.getTheme());
        System.out.println("The mode is: " + game.getMode());
        int length = game.getLevel() * 2 + 2;     // numero di carte diverse
        RandomGenerator randomGenerator = RandomGenerator.getDefault();

        System.out.println("The length is: " + length);
        int j = 0;
        while (j < length) {
            if (cards.add(randomGenerator.nextInt(1, 11))) {
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

        // numero cols = 4
        // numero rows = game.getLevel() + 1


        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100d / (game.getLevel() + 1));
        rowConstraints.vgrowProperty().set(Priority.valueOf("SOMETIMES"));
        rowConstraints.minHeightProperty().set(10.0);
        rowConstraints.prefHeightProperty().set(30.0);
  /*    for (int rows = 0; rows < game.getLevel() + 1; ++rows) {
          gridPane.getRowConstraints().add(rowConstraints);
      }
*/

        int count = 0;
        for (int cols = 0; cols < NCOLS; ++cols) {
            for (int rows = 0; rows < game.getLevel() + 1; ++rows) {
                //     gridPane.getRowConstraints().add(rowConstraints);
    //            System.out.println("memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg");
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg")), 90, 90, true, true);
                ImageView imageView1 = new ImageView();
                imageView1.setImage(image);
                gridPane.add(imageView1, cols, rows);
                GridPane.setHalignment(imageView1, HPos.CENTER);


                ++count;
            }

        }

        startGame(cardCouples);

    }
/*
  public Game getGame() { return game; }
  public void setGame(Game game) {
     this.game = game;
    // update();
  }
  */

    private void startGame(ArrayList<Integer> cardCouples) {
        ArrayList<Integer> index = new ArrayList<>();
            gridPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {                  // quando viene cliccato la carta deve essere resa visibile
                    index.add(indexCard(event));
                    System.out.println("Stampa array da handle: " + Arrays.toString(index.toArray()));
                    if (index.size() == 2) {
                        gridPane.mouseTransparentProperty();
                        checkCoupleSelected(cardCouples, index);
                    }
                }

            });


    }

    private void saveFirstIndex(int index) {

    }

    @FXML
    private void checkCoupleSelected(ArrayList<Integer> cardCouples, ArrayList<Integer> index) {
        System.out.println("Stampa array da funzione: " + Arrays.toString(index.toArray()));

    }
// nr * NCOLS + nc = indice array
    //   1 * 4 + 3 = 7


    @FXML
    private int indexCard(MouseEvent e) {       // ritorna l'indice nell'array della carta selezionata, calcolato con la formula nr * NCOLS + nc
  /*      Node source = (Node) e.getSource();

        Integer colIndex = GridPane.getColumnIndex((Node) e.getTarget());

        Integer rowIndex = GridPane.getRowIndex((Node) e.getTarget());

        System.out.printf("Mouse entered cell [%d, %d]%n", colIndex, rowIndex);
*/


        Node clickedNode = e.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(clickedNode);
        Integer rowIndex = GridPane.getRowIndex(clickedNode);
    //    System.out.printf("Mouse entered cell [%d, %d]%n", colIndex, rowIndex);



        return rowIndex * NCOLS + colIndex;
    }


}




