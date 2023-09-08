package com.game.memory;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.random.RandomGenerator;


public class GameController extends Thread{
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
    @FXML
    private AnchorPane anchorPopup;
    @FXML
    private Label lblWinOrLose;

   // @FXML
   // private ProgressIndicator progressIndicator;

    private int nLife;
    TimeBar timeBar;


    /**
     * Initializes the GameController class.
     */
    @FXML
    public void initialize() {
        game = new Game("", 1, 1);

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

        update();
    }

    @FXML
    void update() {
        lblTheme.textProperty().set("" + game.getTheme());
        lblMode.textProperty().set("" + game.getMode());
        lblLevel.textProperty().set("" + game.getLevel());
        disableComponent();

        btnStart.setVisible(true);

        if (game.getLevel() == 4) {
            NCOLS = 5;
            NROWS = game.getLevel();
        } else {
            NROWS = game.getLevel() + 1;
        }

        if (game.getMode() == 1) {          // 1 = Life
            progressBar.setVisible(false);
            nLife = 3;
            fillGridLife();
        } else if (game.getMode() == 2) {   // 2 = Time
            gridLife.setVisible(false);
            progressBar = new ProgressBar();
            System.out.println("lo è? " + progressBar.isIndeterminate());
            progressBar.indeterminateProperty();
            System.out.println("lo è? " + progressBar.isIndeterminate());
            timeBar = new TimeBar(progressBar);

        }


        initializeGame();
    }

    private void disableComponent() {
        gridPane.getChildren().clear();
        anchorPopup.setDisable(true);
        anchorPopup.setVisible(false);
        gridPane.setDisable(true);
    }

    /**
     * Sets the game scene.
     */
    @FXML
    private void initializeGame() {
        ArrayList<Integer> cardCouples = randomCards();

        setContraints();

        for (int rows = 0; rows < NROWS; ++rows) {
            for (int cols = 0; cols < NCOLS; ++cols) {
                coverImage(cols, rows);
            }
        }

        /**
         * The images are shown for three seconds, then covered.
         */
        btnStart.setOnAction(actionEvent -> {

            btnStart.setVisible(false);

            int count = 0;
            for (int rows = 0; rows < NROWS; ++rows) {
                for (int cols = 0; cols < NCOLS; ++cols) {
                    showImage(cardCouples, cols, rows, count);
                    ++count;
                }
            }

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(getShowTime()), actionEvent1 -> {
                for (int rows = 0; rows < NROWS; ++rows) {
                    for (int cols = 0; cols < NCOLS; ++cols) {
                        coverImage(cols, rows);
                    }
                }

                gridPane.setDisable(false);
            }));
            timeline.play();
        progressBar.setProgress(0.5);
        progressBar.progressProperty().set(0.7);

        System.out.println("get " + progressBar.getProgress());
         //   startProgress(actionEvent);
        });


        startGame(cardCouples);
    }

    private double getShowTime() {
        double showTime = 0;
        switch (game.getLevel()) {
            case (1) -> showTime = 2;
            case (2) -> showTime = 3.5;
            case (3) -> showTime = 4.5;
            case (4) -> showTime = 5.5;
        }
        return showTime;
    }

    private ArrayList<Integer> randomCards() {
        /**
         * The Set is used to randomize the choice of cards without duplicates.
         */
        HashSet<Integer> cards = new HashSet<>();

        /**
         * cardCouples is an array containing all the cards of this level.
         */
        ArrayList<Integer> cardCouples = new ArrayList<>();

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

        Collections.shuffle(cardCouples);
        return cardCouples;
    }

    private void setContraints() {
        if (game.getLevel() == 1 || game.getLevel() == 2) {
            sizeCard = 130;
            sizeConstraint = 160;
        } else if (game.getLevel() == 3 || game.getLevel() == 4) {
            sizeCard = 115;
            sizeConstraint = 125;
        }

        ColumnConstraints colConstraint = new ColumnConstraints(sizeConstraint);
        colConstraint.setHalignment(HPos.LEFT);

        RowConstraints rowConstraints = new RowConstraints(sizeConstraint);
        rowConstraints.setValignment(VPos.CENTER);

        gridPane.prefWidthProperty().bind(vBox.widthProperty());

        gridPane.getColumnConstraints().clear();
        for (int i = 0; i < NCOLS; ++i) {
            gridPane.getColumnConstraints().add(colConstraint);
        }

        gridPane.getRowConstraints().clear();
        for (int i = 0; i < NROWS; ++i) {
            gridPane.getRowConstraints().add(rowConstraints);
        }
    }

    private void startGame(ArrayList<Integer> cardCouples) {
        /**
         * The indexList ArrayList contains the couple of cards the user chooses.
         */
        ArrayList<Integer> indexList = new ArrayList<>();

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

            showImage(cardCouples, index[0] % NCOLS, index[0] / NCOLS, index[0]);
            if (!uncoveredIndex.contains(index[0]) && !indexList.contains(index[0])) {
                indexList.add(index[0]);
            }

            if (indexList.size() == 2) {        // se sono uguali torna a initializeGame. se sono diverse svuoto la lista e ne può  selezionare altre 2
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
            if (game.getMode() == 1) {
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
    }

    /**
     * Shows a pop-up announcing the lose of the game.
     */
    @FXML
    private void lose() {
        System.out.println("Hai perso");
        gridPane.setDisable(true);
        anchorPopup.setDisable(false);
        anchorPopup.setVisible(true);
        lblWinOrLose.setText("Lose");
    }

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

        return rowIndex * NCOLS + colIndex;
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
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg")), sizeCard, sizeCard, true, true);
        ImageView imageView1 = new ImageView();
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
        lblLevel.setText("" + game.getLevel());

        update();
    }

    private void fillGridLife() {
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
        System.out.println("Hai vinto");
        gridPane.setDisable(true);
        anchorPopup.setDisable(false);
        anchorPopup.setVisible(true);
        lblWinOrLose.setText("Win");
    }

    public void returnToMenu(ActionEvent actionEvent) throws IOException {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(MemoryApplication.class.getResource("memory-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Memory Menu");
        stage.setResizable(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/others/brain.png"))));
        stage.setScene(scene);
        stage.show();

    }

    public void restart(ActionEvent actionEvent) {
        game.setLevel(1);
        NCOLS = 4;
        update();
    }

    public void startProgress(ActionEvent actionEvent) {
        Thread thread = new Thread(timeBar);
        thread.setDaemon(true);
        thread.start();
    }

    public class TimeBar implements Runnable{
        ProgressBar progressBar;

        public TimeBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }
        @Override
        public void run() {
            //   System.out.println("progress = " + progressBar.getProgress());
            while(progressBar.getProgress() <= 1) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progressBar.getProgress() + 0.1);

                        System.out.println("progress = " + progressBar.getProgress());

                    }
                });
                synchronized (this) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
            System.out.println("finito");

        }
    }

}