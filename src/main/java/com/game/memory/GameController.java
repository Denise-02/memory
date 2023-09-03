package com.game.memory;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
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


    private Node[][] gridPaneArray = null;

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

/*
        int count = 0;
        for (int rows = 0; rows < game.getLevel() + 1; ++rows) {
            for (int cols = 0; cols < NCOLS; ++cols) {
                showImages(cardCouples, cols, rows, count);
                ++count;
            }
        }
*/
/*
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask(){

       //     @Override
            public void run() {
                System.out.println("mostro carte");
                int count = 0;
                for (int rows = 0; rows < game.getLevel() + 1; ++rows) {
                    for (int cols = 0; cols < NCOLS; ++cols) {
                        showImages(cardCouples, cols, rows, count);
                        ++count;
                    }
                }

            }
        }, 0);
*/
        for (int rows = 0; rows < game.getLevel() + 1; ++rows) {
            for (int cols = 0; cols < NCOLS; ++cols) {
                coverImages(cols, rows);
            }
        }

  //      initializeGridPaneArray();

        int checked = 0;
        while (checked != length / 2) {
            startGame(cardCouples);
            System.out.println("Ritorno a initializeGame");
            ++checked;
        }
    }

    private void startGame(ArrayList<Integer> cardCouples) {
        ArrayList<Integer> indexList = new ArrayList<>();
        int count = 0;      // numero di carte cliccate
        int[] index = new int[1];
        final boolean[] ris = {false};
     //   if (!ris[0]) {      // se non sono state cliccate 2 carte o le due carte sono diverse

            gridPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {                  // quando viene cliccato la carta deve essere resa visibile
                    index[0] = (indexCard(event));
                    System.out.println("index = " + index[0]);

                    showImages(cardCouples, index[0] % NCOLS, index[0] / NCOLS, index[0]);
                    indexList.add(index[0]);
                    System.out.println("size = " + indexList.size());
                    System.out.println("Stampa array da handle: " + Arrays.toString(indexList.toArray()));
                    if (indexList.size() == 2) {        // se sono uguali torna a initializeGame. se sono diverse svuoto la lista e ne può  selezionare altre 2
                //        gridPane.removeEventHandler(MouseEvent.MOUSE_PRESSED, this);
                        ris[0] = checkCoupleSelected(cardCouples, indexList);

                        if (!ris[0]) {
                            System.out.println("entro nell'if!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        } else {
                            System.out.println("%n%n*********************************************************************************");
                            Node n = getNodeFromGridPane(gridPane, indexList.get(0) % NCOLS, indexList.get(0) / NCOLS);
                            System.out.println("trovato nodo 0: " + n);
                            assert n != null;
                            disableNode(n);

                            n = getNodeFromGridPane(gridPane, indexList.get(0) % NCOLS, indexList.get(0) / NCOLS);
                            disableNode(n);
                        }
                        indexList.clear();
                    }
                }

            });
            ++count;
        }


    @FXML
    private boolean checkCoupleSelected(ArrayList<Integer> cardCouples, ArrayList<Integer> index) {
        System.out.println("Stampa array da funzione: " + Arrays.toString(index.toArray()));
        if (Objects.equals(cardCouples.get(index.get(0)), cardCouples.get(index.get(1)))) {
            System.out.printf("uguali [%d][%d]", cardCouples.get(index.get(0)), cardCouples.get(index.get(1)));

            System.out.println("%n%n*********************************************************************************");
            Node n = getNodeFromGridPane(gridPane, index.get(0) % NCOLS, index.get(0) / NCOLS);
            assert n != null;
            disableNode(n);

            n = getNodeFromGridPane(gridPane, index.get(1) % NCOLS, index.get(1) / NCOLS);
            assert n != null;
            disableNode(n);


            System.out.println("*********************************************************************************%n%n");
            return true;    // vanno rese non cliccabili
        } else {
            System.out.printf("diversi [%d][%d]", cardCouples.get(index.get(0)), cardCouples.get(index.get(1)));
            // da aggiungere delay!!
            coverImages(index.get(0) % NCOLS, index.get(0) / NCOLS);
            coverImages(index.get(1) % NCOLS, index.get(1) / NCOLS);

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

    private void showImages(ArrayList<Integer> cardCouples, int cols, int rows, int count) {
        System.out.println("memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/" + game.getTheme() + "/" + cardCouples.get(count) + ".jpg")), 90, 90, true, true);
        ImageView imageView1 = new ImageView();
        imageView1.setImage(image);
        gridPane.add(imageView1, cols, rows);
        GridPane.setHalignment(imageView1, HPos.CENTER);
    }

    private void coverImages(int cols, int rows) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("memoryImages/c" + game.getTheme() + ".jpg")), 90, 90, true, true);
        ImageView imageView1 = new ImageView();
        imageView1.setImage(image);
        gridPane.add(imageView1, cols, rows);
        GridPane.setHalignment(imageView1, HPos.CENTER);
    }

    private void initializeGridPaneArray()
    {
        this.gridPaneArray = new Node[game.getLevel() + 1][NCOLS];
        System.out.println("%n%n*********************************************************************************");
        for(Node node : this.gridPane.getChildren())
        {

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


}




