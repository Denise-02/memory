package com.game.memory;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.random.RandomGenerator;


public class GameController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Label lblLevel;
    @FXML
    private Button btnStart;
    @FXML
    private VBox vBox;
    @FXML
    private GridPane gridLife;
    @FXML
    private ImageView imgLife1;
    @FXML
    private ImageView imgLife2;
    @FXML
    private ImageView imgLife3;
    @FXML
    private VBox vBoxTime;
    private int NCOLS = 4;
    private int NROWS;
    private int sizeCard;
    private int sizeConstraint;
    private ProgressBar progressBar;
    private Timeline timelinePB;    // per disattivare la timeline se si finisce un livello prima della fine del tempo
    private int nLife;

    Game game;

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
        lblLevel.textProperty().set("Level: " + game.getLevel());
        disableComponent();

        btnStart.setVisible(true);

        if (game.getLevel() == 4) {
            NCOLS = 5;
            NROWS = game.getLevel();
        } else {
            NROWS = game.getLevel() + 1;
        }

        if (game.getMode() == 1) {          // 1 = Life
            nLife = 3;

            fillGridLife(nLife);
        } else if (game.getMode() == 2) {   // 2 = Time
            gridLife.setVisible(false);
        }

        initializeGame();
    }

    private void disableComponent() {
        gridPane.getChildren().clear();

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

            if (game.getMode() == 2) {
                setProgressBar();
            }

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(getShowTime()), actionEvent1 -> {
                for (int rows = 0; rows < NROWS; ++rows) {
                    for (int cols = 0; cols < NCOLS; ++cols) {
                        coverImage(cols, rows);
                    }
                }

                gridPane.setDisable(false);

                if (game.getMode() == 2) {
                    double seconds = getTime(game.getLevel());
                    double minutes = seconds / 60;
                    startProgress(progressBar, minutes, seconds);
                }
            }));
            timeline.play();

        });

        startGame(cardCouples);
    }

    private void setProgressBar() {
        progressBar = new ProgressBar();
        progressBar.setProgress(0);
        progressBar.setMinWidth(200);
        progressBar.setMaxWidth(500);

        progressBar.setMinHeight(25);
        vBoxTime.getChildren().add(progressBar);
        progressBar.setStyle("-fx-control-inner-background: black;");
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
        if (game.getLevel() == 1) {
            sizeCard = 130;
            sizeConstraint = 160;
        } else if (game.getLevel() == 2) {
            sizeCard = 115;
            sizeConstraint = 125;
        } else if (game.getLevel() == 3 || game.getLevel() == 4) {
            sizeCard = 105;
            sizeConstraint = 110;
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
                            nextLevel();
                        } else {
                            displayWinPopup();
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
        if (Objects.equals(cardCouples.get(index.get(0)), cardCouples.get(index.get(1)))) {
            if (game.getMode() == 2) {
                increaseTime(getIncreaseTime(game.getLevel()));

            }
            return true;
        } else {
            if (game.getMode() == 1) {
                removeLife();
            } else {
                double secondsToSubtract = getIncreaseTime(game.getLevel()) / 2;
                decreseTime(secondsToSubtract);
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

    private void decreseTime(double secondsToSubtract) {
        Duration duration = new Duration(secondsToSubtract * 1000);
        timelinePB.jumpTo(timelinePB.getCurrentTime().add(duration));
    }

    private void increaseTime(double secondsToAdd) {
        Duration duration = new Duration(secondsToAdd * 1000);
        timelinePB.jumpTo(timelinePB.getCurrentTime().subtract(duration));
    }

    private double getTime(int level) {  // in base al livello restituisce il tempo di durata della time bar
        switch (level) {
            case (1) -> { return 15; }
            case (2) -> { return 30; }
            case (3) -> { return 40; }
            case (4) -> { return 50; }
        }
        return 0;
    }

    private double getIncreaseTime(int level) {
        switch (level) {
            case (1) -> { return 1; }
            case (2) -> { return 1.5; }
            case (3) -> { return 2; }
            case (4) -> { return 3; }
        }
        return 0;
    }



@FXML
    private void removeLife() {             // 0, 1, 2, 3, 4, 5
    --nLife;
    switch (nLife) {
        case (2) -> imgLife3.setVisible(false);
        case (1) -> imgLife2.setVisible(false);
        case (0) -> imgLife1.setVisible(false);
    }

        if (nLife == 0) {
            displayLosePopup();
        }
    }

    /**
     * Shows a pop-up announcing the win of the game.
     */
    private void displayWinPopup() {
        if (game.getMode() == 2) {
            timelinePB.stop();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You won!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! You won the game!");

        alert.showAndWait();
    }
    /**
     * Shows a pop-up announcing the loose of the game.
     */
    private void displayLosePopup() {
        gridPane.setDisable(true);
        if (game.getMode() == 2) {
            timelinePB.stop();
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("You lost");
        alert.setHeaderText(null);
        alert.setContentText("Unfortunately you lost the game!");

        alert.show();
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
     */
    private void nextLevel() {
        game.setLevel(game.getLevel() + 1);
        lblLevel.setText("" + game.getLevel());
        if (game.getMode() == 2) {
            timelinePB.stop();
            vBoxTime.getChildren().remove(vBoxTime.getChildren().size() - 1);
        }


        update();
    }

    private void fillGridLife(int nLife) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/others/heart.png")), 70, 70, true, true);
        imgLife3.setImage(image);
        imgLife2.setImage(image);
        imgLife1.setImage(image);
        imgLife3.setVisible(true);
        imgLife2.setVisible(true);
        imgLife1.setVisible(true);
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
        if (game.getMode() == 2) {
            timelinePB.stop();
            vBoxTime.getChildren().remove(vBoxTime.getChildren().size() - 1);
        }
    }


    @FXML
    public void startProgress(ProgressBar progressBar, double minutes, double sec) {
        IntegerProperty seconds = new SimpleIntegerProperty();
        progressBar.progressProperty().bind(seconds.divide(sec));
        timelinePB = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(seconds, 0)),
                new KeyFrame(Duration.minutes(minutes), e-> {
                    // do anything you need here on completion...

                    displayLosePopup();

                }, new KeyValue(seconds, sec))
        );
        timelinePB.setAutoReverse(true);

        timelinePB.play();
        progressBar.setStyle("-fx-accent: blue;");
        progressBar.setStyle("-fx-control-inner-background: black;");

    }

}