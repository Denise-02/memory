package com.game.memory;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.*;
import java.util.random.RandomGenerator;

import static java.lang.Thread.sleep;

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
    @FXML
    private Button btnStart;

    Game game;

    private final int NCOLS = 4;

    private int checked = 0;    // numero di coppie trovate


    private Node[][] gridPaneArray = null;

    /**
     * The level of the game. For each level the number of cards increases.
     */
    @FXML
    public void initialize() {
        game = new Game("", 1, 1);
        gridPane.setGridLinesVisible(true);
    }

    public void receiveData(String theme, int mode, int level) throws InterruptedException {
        game.setTheme(theme);
        game.setMode(mode);
        game.setLevel(level);

        update();
    }

    @FXML
    void update() throws InterruptedException {
        lblTheme.textProperty().set("" + game.getTheme());
        lblMode.textProperty().set("" + game.getMode());
        lblLevel.textProperty().set("" + game.getLevel());
        btnStart.setVisible(true);
        initializeGame();
    }

    @FXML
    private void initializeGame() throws InterruptedException {
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

        System.out.println("Stampa prima di shuffle " + Arrays.toString(cardCouples.toArray()));
        Collections.shuffle(cardCouples);


        System.out.println("stampa dopo shuffle " + Arrays.toString(cardCouples.toArray()));
        for (int i = 1; i <= game.getLevel(); ++i) {

            gridPane.addRow(i, new ImageView(), new ImageView(), new ImageView(), new ImageView());
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100d / (game.getLevel() + 1));
        rowConstraints.vgrowProperty().set(Priority.valueOf("SOMETIMES"));
        rowConstraints.minHeightProperty().set(10.0);
        rowConstraints.prefHeightProperty().set(30.0);

        gridPane.setVisible(false);

        int count = 0;
        for (int rows = 0; rows < game.getLevel() + 1; ++rows) {
            for (int cols = 0; cols < NCOLS; ++cols) {
                showImage(cardCouples, cols, rows, count);
                ++count;
            }
        }
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gridPane.setVisible(true);
                btnStart.setVisible(false);

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        for (int rows = 0; rows < game.getLevel() + 1; ++rows) {
                            for (int cols = 0; cols < NCOLS; ++cols) {
                                coverImage(cols, rows);
                            }
                        }
                    }
                }));


                timeline.play();
            }

        });


        startGame(cardCouples);

    }

    private void startGame(ArrayList<Integer> cardCouples) {
        ArrayList<Integer> indexList = new ArrayList<>();
        ArrayList<Integer> uncoveredIndex = new ArrayList<>();

        int[] index = new int[1];
        final boolean[] ris = {false};

        gridPane.setOnMouseClicked(event -> {                  // quando viene cliccato la carta deve essere resa visibile
            index[0] = (indexCard(event));
            System.out.println("index = " + index[0]);

            showImage(cardCouples, index[0] % NCOLS, index[0] / NCOLS, index[0]);
            if (!uncoveredIndex.contains(index[0]) && !indexList.contains(index[0])) {
                indexList.add(index[0]);
            }
            if (indexList.size() == 2) {        // se sono uguali torna a initializeGame. se sono diverse svuoto la lista e ne può  selezionare altre 2
                //        gridPane.removeEventHandler(MouseEvent.MOUSE_PRESSED, this);

                try {
                    ris[0] = checkCoupleSelected(cardCouples, indexList);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!ris[0]) {
                    System.out.println("diverse");
                } else {
                    uncoveredIndex.addAll(indexList);
                    if (uncoveredIndex.size() == cardCouples.size()) {
                        if (game.getLevel() < 4) {
                            try {
                                nextLevel();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            win();
                        }
                    }
                }
                indexList.clear();
            }
        });

    }


    @FXML
    private boolean checkCoupleSelected(ArrayList<Integer> cardCouples, ArrayList<Integer> index) throws InterruptedException {
        System.out.println("Stampa array da funzione: " + Arrays.toString(index.toArray()));
        if (Objects.equals(cardCouples.get(index.get(0)), cardCouples.get(index.get(1)))) {
            System.out.printf("uguali [%d][%d]", cardCouples.get(index.get(0)), cardCouples.get(index.get(1)));

            return true;    // vanno rese non cliccabili
        } else {
            System.out.printf("diversi [%d][%d]%n%n", cardCouples.get(index.get(0)), cardCouples.get(index.get(1)));
            // da aggiungere delay!!
            //   sleep(2);

            //       showImage(cardCouples, index.get(1) % NCOLS, index.get(1) / NCOLS, index.get(1) / NCOLS * NCOLS + index.get(1) % NCOLS);
            //    sleep(1500);

            int col1 = index.get(0) % NCOLS;
            int row1 = index.get(0) / NCOLS;
            int col2 = index.get(1) % NCOLS;
            int row2 = index.get(1) / NCOLS;
            gridPane.setDisable(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    coverCouple(col1, row1, col2, row2);
                    // funzione che disabilita tutto ??
                    gridPane.setDisable(false);
                }
            }));

            timeline.play();
        }
        return false;
    }
// nr * NCOLS + nc = indice array
    //   1 * 4 + 3 = 7


    @FXML
    private int indexCard(MouseEvent e) {       // ritorna l'indice nell'array della carta selezionata, calcolato con la formula nr * NCOLS + nc
        Node clickedNode = e.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(clickedNode);
        Integer rowIndex = GridPane.getRowIndex(clickedNode);
        System.out.printf("Mouse entered cell [%d, %d]%n", colIndex, rowIndex);

        return rowIndex * NCOLS + colIndex;
        // indice / NCOLS = rowIndex
        // indice % NCOLS = colindex
    }

    private void showImage(ArrayList<Integer> cardCouples, int cols, int rows, int count) {
        System.out.println("showing memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg")), 90, 90, true, true);
        ImageView imageView1 = new ImageView();
        //   System.out.println("immagine: " + imageView1.getImage().getUrl());
        imageView1.setImage(image);
        gridPane.add(imageView1, cols, rows);
        GridPane.setHalignment(imageView1, HPos.CENTER);
    }

    private void coverImage(int cols, int rows) {
        System.out.println("showing memoryImages/" + game.getTheme() + "/c" + game.getTheme() + ".jpg");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c" + game.getTheme() + ".jpg")), 90, 90, true, true);
        ImageView imageView1 = new ImageView();
        imageView1.setImage(image);
        gridPane.add(imageView1, cols, rows);
        GridPane.setHalignment(imageView1, HPos.CENTER);
    }

    private void coverCouple(int cols1, int rows1, int cols2, int rows2) {
        coverImage(cols1, rows1);
        coverImage(cols2, rows2);
    }

    private void initializeGridPaneArray() {
        this.gridPaneArray = new Node[game.getLevel() + 1][NCOLS];
        System.out.println("%n%n*********************************************************************************");
        for (Node node : this.gridPane.getChildren()) {

            //     this.gridPaneArray[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = node;
            System.out.println("node = " + node);
        }
        System.out.println("*********************************************************************************%n%n");
    }


    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren())
            if (GridPane.getColumnIndex(node) != null
                    && GridPane.getColumnIndex(node) != null
                    && GridPane.getRowIndex(node) == row
                    && GridPane.getColumnIndex(node) == col)
                return node;
        return null;
    }

    private void disableNode(Node node) {
        assert node != null;
        System.out.println("DISATTIVO NODO" + node);
        node.setDisable(true);
        node.disableProperty();
        node.disabledProperty();
        node.setStyle("pointer-events: none");
        node.autosize();
        node.setStyle("border-style: solid; border-color: red;");
        // node.cursorProperty().removeListener((ChangeListener<? super Cursor>) this);

    }

    private void nextLevel() throws InterruptedException {

        game.setLevel(game.getLevel() + 1);
        System.out.println("Nuovo livello: " + game.getLevel());
        update();
    }

    private void win() {

    }

}




