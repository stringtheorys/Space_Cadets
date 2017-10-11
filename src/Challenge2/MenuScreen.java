package Challenge2;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MenuScreen extends VBox implements Interpreter.InterpreterInterface {

    private final int gridSize = 10;

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

        HBox topLayout = new HBox();

        VBox codeLayout = new VBox();
        Label codeLabel = new Label("Code");
        codeArea = new TextArea();
        codeLayout.getChildren().addAll(codeLabel, codeArea);

        VBox codeButtons = new VBox();
        runButton = new Button("Run Code");
        runButton.setOnMouseClicked(event -> runPauseCode());
        Button stopButton = new Button("Stop Code");
        stopButton.setOnMouseClicked(event -> stopCode());
        Button clearButton = new Button("Clear");
        clearButton.setOnMouseClicked(event -> clearCodeArea());
        Button examplesButton = new Button("Examples");
        examplesButton.setOnMouseClicked(event -> showExamples());
        codeButtons.getChildren().addAll(runButton, clearButton, examplesButton);

        topLayout.getChildren().addAll(codeLayout, codeButtons);

        colourGrids = new Cell[gridSize][gridSize];

        HBox colourGridLayout = new HBox();
        for (int x = 0; x < gridSize; x++) {
            VBox gridColumn = new VBox();
            for (int y = 0; y < gridSize; y++) {
                colourGrids[x][y] = new Cell();
            }
            gridColumn.getChildren().addAll(colourGrids[x]);
        }

        getChildren().addAll(topLabel, topLayout, colourGridLayout);
    }

    private void runPauseCode() {
        if (running == 0) {
            String code = codeArea.getText();
            interpreter = new Interpreter(MenuScreen.this, gridSize, code);
            interpreter.runInterpreter();
            runButton.setText("Pause");
            running = 1;
        } else if (running == 1) {
            interpreter.pauseInterpreter();
            runButton.setText("Run");
            running = 2;
        } else {
            interpreter.runInterpreter();
            runButton.setText("Pause");
            running = 1;
        }
    }

    private void stopCode() {
        interpreter.pauseInterpreter();
    }

    private void clearCodeArea() {
        codeArea.setText("");
    }

    private void showExamples() {

    }

    @Override
    public void updateUI(Colour[][] grid) {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                colourGrids[x][y].setColour(grid[x][y]);
            }
        }
    }

    @Override
    public void errorHandling(String error) {
        System.out.println(error);
    }
}
