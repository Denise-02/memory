package com.game.memory;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

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
    private Label lblTheme;
    @FXML
    private Label lblMode;
    @FXML
    private Label lblLevel;
    @FXML
    private Button btnStart;

    Game game;

    private int NCOLS = 4;
    private int NROWS;
    private int sizeCard;
    private int sizeConstraint;

    @FXML
    private VBox vBox;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private GridPane gridLife;

    @FXML
    private ImageView imgLife1;
    @FXML
    private ImageView imgLife2;
    @FXML
    private ImageView imgLife3;

    private int nLife;

    /**
     * Initializes the GameController class.
     */
    @FXML
    public void initialize() {
        game = new Game("", 1, 1);
        //   gridPane.setGridLinesVisible(true);
        //   anchorPane.setStyle("-fx-background-image: url('')");
    }

    /**
     * Receives the game settings from MemoryController.
     *
     * @param theme
     * @param mode  1 is life, 2 is time
     * @param level
     * @throws InterruptedException
     */
    public void receiveData(String theme, int mode, int level) throws InterruptedException {
        game.setTheme(theme);
        game.setMode(mode);
        game.setLevel(level);

        update();   //VIENE CHIAMATO ANCHE IN MemoryController
    }

    @FXML
    void update() {
        lblTheme.textProperty().set("" + game.getTheme());
        lblMode.textProperty().set("" + game.getMode());
        lblLevel.textProperty().set("" + game.getLevel());
        btnStart.setVisible(true);

        if (game.getLevel() == 4) {
            NCOLS = 5;
            NROWS = game.getLevel();
        } else {
            NROWS = game.getLevel() + 1;
        }

        if (game.getMode() == 1) {  // 1 = vite
            progressBar.setVisible(false);
            nLife = 3;
            fillGridLife();
            gridLife.setGridLinesVisible(true);
        } else if (game.getMode() == 2) {
            gridLife.setVisible(false);
        }


        initializeGame();
    }

    /**
     * Sets the game scene.
     */
    @FXML
    private void initializeGame() {

        /**
         * The Set is used to randomize the choice of cards without duplicates.
         */
        HashSet<Integer> cards = new HashSet<>();

        /**
         * cardCouples is an array containing all the cards of this level.
         */
        ArrayList<Integer> cardCouples = new ArrayList<>();

        System.out.println("The level is: " + game.getLevel());
        System.out.println("The theme is: " + game.getTheme());
        System.out.println("The mode is: " + game.getMode());

        /**
         * The number of different cards.
         */
        int length = game.getLevel() * 2 + 2;

        RandomGenerator randomGenerator = RandomGenerator.getDefault();
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


        if (game.getLevel() == 1 || game.getLevel() == 2) {
            sizeCard = 130;
            sizeConstraint = 160;
        } else if (game.getLevel() == 3 || game.getLevel() == 4) { //su   sx      giu       dx
            sizeCard = 115;
            sizeConstraint = 125;
        }

        ColumnConstraints colConstraint = new ColumnConstraints(sizeConstraint);
        colConstraint.setHalignment(HPos.LEFT);

        RowConstraints rowConstraints = new RowConstraints(sizeConstraint);
        rowConstraints.setValignment(VPos.CENTER);

        gridPane.prefWidthProperty().bind(vBox.widthProperty());

        gridPane.getColumnConstraints().clear();

        //     gridPane.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint, colConstraint);
        for (int i = 0; i < NCOLS; ++i) {
            gridPane.getColumnConstraints().add(colConstraint);
        }
        System.out.println("constraint colonne: " + gridPane.getColumnConstraints());

        gridPane.getRowConstraints().clear();
        for (int i = 0; i < NROWS; ++i) {
            gridPane.getRowConstraints().add(rowConstraints);
            //    gridPane.addRow(i, new ImageView());
        }
        System.out.println("constraint righe: " + gridPane.getRowConstraints());
        System.out.println("count = " + (long) gridPane.getRowConstraints().size());

        //   gridPane.setVisible(false);

        for (int rows = 0; rows < NROWS; ++rows) {
            for (int cols = 0; cols < NCOLS; ++cols) {
                coverImage(cols, rows);
            }
        }

        gridPane.setDisable(true);
        btnStart.setOnAction(actionEvent -> {
      /*      gridPane.getRowConstraints().clear();
            for (int i = 0; i < NROWS; ++i) {
                gridPane.getRowConstraints().add(rowConstraints);
                gridPane.addRow(i, new ImageView());
            }
*/
            //    gridPane.setVisible(true);
            btnStart.setVisible(false);

            /**
             * The images are shown for three seconds, then covered.
             */

            int count = 0;
            for (int rows = 0; rows < NROWS; ++rows) {
                for (int cols = 0; cols < NCOLS; ++cols) {
                    showImage(cardCouples, cols, rows, count);
                    ++count;
                }
            }


            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), actionEvent1 -> {
                for (int rows = 0; rows < NROWS; ++rows) {
                    for (int cols = 0; cols < NCOLS; ++cols) {
                        coverImage(cols, rows);
                    }
                }
                gridPane.setDisable(false);
            }));
            timeline.play();
        });

        startGame(cardCouples);
    }

    private void startGame(ArrayList<Integer> cardCouples) {
        /**
         * The indexList ArrayList contains the couple of cards the user chooses.
         */
        ArrayList<Integer> indexList = new ArrayList<>();
 /*       Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/" + game.getTheme() + "Background.jpg")));
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true));
anchorPane.setBackground(new Background(backgroundImage)); */
        /**
         * The uncoveredIndex ArrayList contains the indexes of the card that are uncovered cause the player found
         * the right couple. The images of this array can't be covered anymore.
         */
        ArrayList<Integer> uncoveredIndex = new ArrayList<>();

        int[] index = new int[1];
        final boolean[] ris = {false};

        /**
         * Event handler: the card is revealed when an ImageView has been clicked.
         */
        gridPane.setOnMouseClicked(event -> {
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

                    /**
                     * The level increases when all pairs have been uncovered.
                     */
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

    /**
     * @param cardCouples the ArrayList containing all the images.
     * @param index       the ArrayList containing the cards to be compared
     * @return true if the selected ImageViews contain the same image, false otherwise.
     * @throws InterruptedException
     */
    @FXML
    private boolean checkCoupleSelected(ArrayList<Integer> cardCouples, ArrayList<Integer> index) throws InterruptedException {
        //      System.out.println("Stampa array da funzione: " + Arrays.toString(index.toArray()));
        if (Objects.equals(cardCouples.get(index.get(0)), cardCouples.get(index.get(1)))) {
            //         System.out.printf("uguali [%d][%d]", cardCouples.get(index.get(0)), cardCouples.get(index.get(1)));
            if (game.getMode() == 2) {
                System.out.println("aumento tempo");
            }


            return true;
        } else {
            //          System.out.printf("diversi [%d][%d]%n%n", cardCouples.get(index.get(0)), cardCouples.get(index.get(1)));
            if (game.getLevel() == 1) {
                System.out.println("togliamo una vita");
                removeLife();
            }
            int col1 = index.get(0) % NCOLS;
            int row1 = index.get(0) / NCOLS;
            int col2 = index.get(1) % NCOLS;
            int row2 = index.get(1) / NCOLS;
            gridPane.setDisable(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), actionEvent -> {
                coverCouple(col1, row1, col2, row2);
                // funzione che disabilita tutto ??
                gridPane.setDisable(false);
            }));
            timeline.play();
        }
        return false;
    }

    private void removeLife() {
        --nLife;

        switch (nLife) {
            case (2) -> imgLife3.setVisible(false);
            case (1) -> imgLife2.setVisible(false);
            case (0) -> imgLife1.setVisible(false);
        }

        if (nLife == 0) {
            lose();
        }

        //gridLife.getChildren().get(nLife).setVisible(false);
        //      gridLife.getChildren().remove(0);
        System.out.println("getChildren: " + gridLife.getChildren() + ", nLife = " + nLife);

    }

    private void lose() {
        System.out.println("Hai perso");
    }
// nr * NCOLS + nc = indice array
    //   1 * 4 + 3 = 7

    /**
     * This method computes the index of matrix cell using matrix algorithm.
     *
     * @param e Event: gridPane.setOnMouseClicked
     * @return the index, in GridPane matrix, of the selected card.
     */
    @FXML
    private int indexCard(MouseEvent e) {       // ritorna l'indice nell'array della carta selezionata, calcolato con la formula nr * NCOLS + nc
        Node clickedNode = e.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(clickedNode);
        Integer rowIndex = GridPane.getRowIndex(clickedNode);
        //       System.out.printf("Mouse entered cell [%d, %d]%n", colIndex, rowIndex);

        return rowIndex * NCOLS + colIndex;
        // indice / NCOLS = rowIndex
        // indice % NCOLS = colindex
    }

    /**
     * Shows the card taking it from cardCouples ArrayList
     *
     * @param cardCouples the ArrayList containing all the images.
     * @param cols        the column index of the cell
     * @param rows        the row index of the cell
     * @param count       the index of the image in cardCouples ArrayList
     */
    private void showImage(ArrayList<Integer> cardCouples, int cols, int rows, int count) {
        //     System.out.println("showing memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg")), sizeCard, sizeCard, true, true);
        ImageView imageView1 = new ImageView();
        //System.out.println("immagine: " + imageView1.getImage().getUrl());
        imageView1.setImage(image);
        gridPane.add(imageView1, cols, rows);
        GridPane.setHalignment(imageView1, HPos.CENTER);
    }

    /**
     * Cover the card at indicated (cols, rows) cell.
     *
     * @param cols the column index of the cell to be covered.
     * @param rows the row index of the cell to be covered.
     */
    private void coverImage(int cols, int rows) {
        System.out.println("showing memoryImages/" + game.getTheme() + "/c" + game.getTheme() + ".jpg");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c" + game.getTheme() + ".jpg")), sizeCard, sizeCard, true, true);
        ImageView imageView1 = new ImageView();
        imageView1.setImage(image);
        gridPane.add(imageView1, cols, rows);
        GridPane.setHalignment(imageView1, HPos.CENTER);
    }

    /**
     * Sends column and row indexes of two cards to coverImage method.
     *
     * @param cols1 the column index of the first card
     * @param rows1 the row index of the first card
     * @param cols2 the column index of the second card
     * @param rows2 the row index of the second card
     */
    private void coverCouple(int cols1, int rows1, int cols2, int rows2) {
        coverImage(cols1, rows1);
        coverImage(cols2, rows2);
    }


    /**
     * Increases the level and restarts the game calling the update() method
     *
     * @throws InterruptedException
     */
    private void nextLevel() throws InterruptedException {
        game.setLevel(game.getLevel() + 1);
        System.out.println("Nuovo livello: " + game.getLevel());

        gridPane.getChildren().clear();
        update();
    }

    private void fillGridLife() {
        System.out.println("showing memoryImages/others/heart.png");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/others/heart.png")), 70, 70, true, true);
        imgLife3.setImage(image);
        imgLife2.setImage(image);
        imgLife1.setImage(image);
        imgLife3.setVisible(true);
        imgLife2.setVisible(true);
        imgLife1.setVisible(true);
    }

    /**
     * Shows a pop-up announcing the win of the game.
     */
    private void win() {

    }
}