package Challenge2;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MenuScreen extends VBox implements Interpreter.InterpreterInterface {

    private final int gridSize = 40;

    private int running = 0;

    private Button runButton;
    private TextArea codeArea;

    private Cell[][] colourGrids;

    private Interpreter interpreter;

    MenuScreen() {
        initGui();
    }

    private void initGui() {

        Label topLabel = new Label("PaintF*ck Interpreter");

        Label codeLabel = new Label("Code");
        codeArea = new TextArea();

        HBox codeButtons = new HBox();
        runButton = new Button("Run Code");
        runButton.setOnMouseClicked(event -> runPauseCode());
        Button stopButton = new Button("Stop Code");
        stopButton.setOnMouseClicked(event -> stopCode());
        Button clearButton = new Button("Clear");
        clearButton.setOnMouseClicked(event -> clearColourGrid());
        Button examplesButton = new Button("Examples");
        examplesButton.setOnMouseClicked(event -> showExamples());
        codeButtons.getChildren().addAll(runButton, clearButton, examplesButton);

        colourGrids = new Cell[gridSize][gridSize];

        HBox colourGridLayout = new HBox();
        for (int x = 0; x < gridSize; x++) {
            VBox gridColumn = new VBox();
            for (int y = 0; y < gridSize; y++) {
                colourGrids[x][y] = new Cell();
            }
            gridColumn.getChildren().addAll(colourGrids[x]);
            colourGridLayout.getChildren().add(gridColumn);
        }

        //rndGridColours();

        getChildren().addAll(topLabel, codeLabel, codeArea, codeButtons, colourGridLayout);
    }

    private void runPauseCode() {
        if (running == 0) {
            System.out.println("MC : Start running interpreter");
            String code = codeArea.getText();
            interpreter = new Interpreter(MenuScreen.this, gridSize, code);
            interpreter.run();
            runButton.setText("Pause");
            running = 1;
        } else if (running == 1) {
            System.out.println("MC : Pausing interpreter");
            interpreter.pauseInterpreter();
            runButton.setText("Run");
            running = 2;
        } else {
            System.out.println("MC : Restarting interpreter");
            interpreter.run();
            runButton.setText("Pause");
            running = 1;
        }
    }

    private void stopCode() {
        System.out.println("MC : Stop the interpreter");
        interpreter.pauseInterpreter();
        running = 0;
    }

    private void clearColourGrid() {
        System.out.println("MC : Clear Colour Grid");
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                colourGrids[x][y].setColour(Colour.WHITE);
            }
        }
        interpreter.pauseInterpreter();
        running = 0;
        runButton.setText("Run Code");
    }

    private void showExamples() {
        System.out.println("MC : Show Examples");
    }

    private void rndGridColours() {
        System.out.println("MC : Setting Rnd Grid Colours");
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                colourGrids[x][y].setBackground(Colour.getRndBackground());
            }
        }
    }

    @Override
    public void updateUI(Colour[][] grid) {
        System.out.println("MC : Updating UI");
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                colourGrids[x][y].setColour(grid[x][y]);
            }
        }
    }

    @Override
    public void programEnd() {
        System.out.println("MC : Resetting Run button");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                running = 0;
                runButton.setText("Run Code");
            }
        });
    }
}
