package Challenge2;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.concurrent.ConcurrentLinkedQueue;

// The menuScreen which is the main part of the ui
// It inherits from the VBox which adds widgets vertically down
// Implements the InterpreterInterface which allows the interpreter to communicate with the class
public class MenuScreen extends VBox implements Interpreter.InterpreterInterface {

    // An enum for the UiUpdate Thread
    enum UiUpdateStatus {
        RUNNING, PAUSED, NOT_STARTED;
    }

    // An enum for the interpreter thread
    enum InterpreterStatus {
        RUNNING, FINISHED, MAX_LOOPS, NOT_STARTED;
    }

    // Boolean values of different values to be clear
    private final boolean WHITE_BACKGROUND = false;
    private final boolean BLACK_BACKGROUND = true;

    // The width of the output grid
    private final int gridSize = 40;

    // Run button texts
    private final String defaultRunBtnText = "Run Code";
    private final String runningRunBtnText = "Pause";
    private final String pausedRunBtnText = "Run";

    // Main Attributes
    private InterpreterStatus interpreterStatus = InterpreterStatus.NOT_STARTED; // Interpreter status
    private UiUpdateStatus uiUpdateStatus = UiUpdateStatus.NOT_STARTED; // Ui Updater status
    private ConcurrentLinkedQueue<boolean[][]> UiOutputQueue = new ConcurrentLinkedQueue<>(); // Output grid queue
    private Block[][] outputGrid = new Block[gridSize][gridSize]; // Output grid

    private Interpreter interpreter; // Interpreter

    private Button runButton; // Run Button
    private TextArea codeArea; // Input area for code

    // Constructor
    MenuScreen() {
        initGui();
    }

    // Generates the ui
    private void initGui() {

        Label topLabel = new Label("PaintF*ck Interpreter");

        Label codeLabel = new Label("Code");
        codeArea = new TextArea();

        HBox codeButtons = new HBox();
        runButton = new Button("Run Code"); // Run Button
        runButton.setOnMouseClicked(event -> runButtonClick());
        Button stopButton = new Button("Stop Code"); // Stop Button
        stopButton.setOnMouseClicked(event -> stopButtonClick());
        Button clearButton = new Button("Clear"); // Clear Button
        clearButton.setOnMouseClicked(event -> clearButtonClick());
        Button examplesButton = new Button("Examples"); // Shows examples button
        examplesButton.setOnMouseClicked(event -> exampleButtonClick());

        // Add all of the widgets to the ui
        codeButtons.getChildren().addAll(runButton, clearButton, stopButton, examplesButton);

        // Add all of the output blocks to the ui
        HBox colourGridLayout = new HBox();
        for (int x = 0; x < gridSize; x++) {
            VBox gridColumn = new VBox();
            for (int y = 0; y < gridSize; y++) {
                outputGrid[x][y] = new Block();
            }
            gridColumn.getChildren().addAll(outputGrid[x]);
            colourGridLayout.getChildren().add(gridColumn);
        }

        getChildren().addAll(topLabel, codeLabel, codeArea, codeButtons, colourGridLayout);

    }

    // On Stop Button click
    private void stopButtonClick() {
        uiUpdateStatus = UiUpdateStatus.NOT_STARTED;
        interpreterStatus = InterpreterStatus.NOT_STARTED;

        runButton.setText(defaultRunBtnText);
    }

    // Shows a pop up with a list of default programs
    private void exampleButtonClick() {

    }

    // On Run Button click
    private void runButtonClick() {

        // Pause the ui updater
        if (uiUpdateStatus == UiUpdateStatus.RUNNING) {
            Log.defPrint("MS : UiUpdateStatus Paused");

            uiUpdateStatus = UiUpdateStatus.PAUSED;
            runButton.setText(pausedRunBtnText);

            // Run the ui updater
        } else if (uiUpdateStatus == UiUpdateStatus.PAUSED) {
            Log.defPrint("MS : UiUpdateStatus Running");

            uiUpdateStatus = UiUpdateStatus.RUNNING;
            runButton.setText(runningRunBtnText);
            new UiUpdater(this).run();

            // Start the ui updater
        } else if (uiUpdateStatus == UiUpdateStatus.NOT_STARTED) {
            Log.defPrint("MS : UiUpdateStatus Not Started");

            uiUpdateStatus = UiUpdateStatus.RUNNING;
            interpreterStatus = InterpreterStatus.RUNNING;

            String code = codeArea.getText();
            interpreter = new Interpreter(MenuScreen.this, gridSize, code);
            interpreter.run();
            new UiUpdater(this).run();
        }
    }

    // Turn all blocks to white
    private void clearButtonClick() {
        Log.print("MS : Clear button click");
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                outputGrid[x][y].setColour(WHITE_BACKGROUND);
            }
        }

        uiUpdateStatus = UiUpdateStatus.NOT_STARTED;
        interpreterStatus = InterpreterStatus.NOT_STARTED;
    }

    // Gets the Ui Update status
    UiUpdateStatus getUiUpdateStatus() {
        return uiUpdateStatus;
    }

    // Gets the Interpreter status
    InterpreterStatus getInterpreterStatus() {
        return interpreterStatus;
    }

    // Updates the UI
    void updateUi(boolean[][] grid) {
        Log.print("MS : Updating Ui");

        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                outputGrid[x][y].setColour(grid[x][y]);
            }
        }
    }

    // Ui Updater stops
    void uiUpdaterStopped() {
        Log.print("MS : Ui Updater stopped");

        runButton.setText(defaultRunBtnText);
    }

    @Override
    // Adds output grid to the queue
    public void addOutputGrid(boolean[][] colourGrid) {
        UiOutputQueue.offer(colourGrid);
    }

    @Override
    // When the interpreter has finished
    public void programEnd() {
        runButton.setText("Skip");
        interpreterStatus = InterpreterStatus.FINISHED;
    }

    @Override
    // When the interpreter has reached max loops
    public void maxLoops() {
        runButton.setText("Continue");
        interpreterStatus = InterpreterStatus.MAX_LOOPS;
    }
}
