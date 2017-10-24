package Challenge3;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IDEScreen extends VBox implements Interpreter.InterpreterInterface {

    enum ConsoleInput {
        NONE,
        INTEGER,
        CHARACTER
    }

    private final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private final char[] digits = "0123456789".toCharArray();

    private int gridSize = 20;

    private Block[][] inputGrid = new Block[gridSize][gridSize];
    private StackBar stackBar;
    private Label console;
    private ConsoleInput consoleInputType = ConsoleInput.NONE;

    private Interpreter interpreter;
    private boolean interpreterRunning = false;
    private boolean interpreterFinished = true;

    public IDEScreen() {

        initGui();
    }

    private void initGui() {

        Label topLabel = new Label("Befunge Interpreter");
        HBox mainBox = new HBox();

        // Left Box
        VBox leftBox = new VBox();
        // Input box
        VBox inputBox = new VBox();
        for (int x = 0; x < gridSize; x++) {
            HBox row = new HBox();
            for (int y = 0; y < gridSize; y++) {
                inputGrid[x][y] = new Block();
                row.getChildren().add(inputGrid[x][y]);
            }
            inputBox.getChildren().add(row);
        }

        // Console box
        console = new Label("Console\n");

        // Console input
        HBox consoleInputBox = new HBox();
        Label inputLabel = new Label("Input :");
        TextField inputField = new TextField();
        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0 && consoleInputType == ConsoleInput.NONE) {
                inputField.setText("");
            } else if (newValue.length() > 1) {
                inputField.setText(newValue.substring(0,1));
            } else if (newValue.length() == 1) {
                char input = inputField.getText().charAt(0);

                if (consoleInputType == ConsoleInput.CHARACTER) {
                    for (char alpha : alphabet) {
                        if (input == alpha) {
                            inputField.setText(newValue);
                            return;
                        }
                    }
                    inputField.setText("");
                } else if (consoleInputType == ConsoleInput.INTEGER) {
                    for (char digit : digits) {
                        if (input == digit) {
                            inputField.setText(newValue);
                            return;
                        }
                    }
                    inputField.setText("");
                }
            }
        });
        inputField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                // TODO
            }
        });

                consoleInputBox.getChildren().addAll(inputLabel, inputField);

        leftBox.getChildren().addAll(inputBox);

        VBox rightBox = new VBox();

        stackBar = new StackBar();

        HBox buttons = new HBox();

        Button singleRunBtn = new Button("Single Run");
        singleRunBtn.setOnMouseClicked(event -> singleRun());
        Button multiRunBtn = new Button("Multi Run");
        multiRunBtn.setOnMouseClicked(event -> multiRun());
        Button runAllBtn = new Button("Run All");
        runAllBtn.setOnMouseClicked(event -> runAll());
        Button stopBtn = new Button("Stop");
        stopBtn.setOnMouseClicked(event -> stopInterpreter());

        buttons.getChildren().addAll(singleRunBtn, multiRunBtn, runAllBtn, stopBtn);

        rightBox.getChildren().addAll(stackBar, buttons, console, consoleInputBox);

        mainBox.getChildren().addAll(leftBox, rightBox);
        getChildren().addAll(topLabel, mainBox);
    }

    private void singleRun() {
        if (interpreterFinished) {
            if (!interpreterRunning) {
                interpreter = new Interpreter(this, IDEScreen.this, getCode());
                interpreterRunning = true;
            }
            interpreterFinished = false;
            interpreter.runSingle();
        }
    }

    private void multiRun() {
        if (interpreterFinished) {
            if (!interpreterRunning) {
                interpreter = new Interpreter(this, IDEScreen.this, getCode());
                interpreterRunning = true;
            }
            interpreterFinished = false;
            interpreter.runMulti();
        }
    }

    private void runAll() {
        if (interpreterFinished) {
            if (!interpreterRunning) {
                interpreter = new Interpreter(this, IDEScreen.this, getCode());
                interpreterRunning = true;
            }
            interpreterFinished = false;
            interpreter.runAll();
        }
    }

    private void stopInterpreter() {
        interpreter.stopInterpreter();
        interpreterRunning = false;
    }

    private char[][] getCode() {
        char[][] code = new char[gridSize][gridSize];
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                code[x][y] = inputGrid[x][y].getInput();
            }
        }
        return code;
    }

    public void pushElement(int element) {
        stackBar.pushElement(element);
    }

    public void popElement() {
        stackBar.popElement();
    }

    @Override
    public void interpreterFinishedRunning() {
        interpreterFinished = true;
    }

    public char getCharInput() {
        consoleInputType = ConsoleInput.CHARACTER;
        return ' ';
    }
    public int getIntInput() {
        consoleInputType = ConsoleInput.INTEGER;
        return ' ';
    }

    @Override
    public void interpreterHasStopped() {
        interpreterRunning = false;
        console.setText(console.getText() + "\nProgram Ended");
    }

    @Override
    public void printInteger(int num) {
        console.setText(console.getText() + num);
    }

    @Override
    public void printChar(char character) {
        console.setText(console.getText() + character);
    }

    @Override
    public void error(String errorMsg) {
        console.setText(console.getText() + "\n" + errorMsg);
    }

    @Override
    public void unhighlight(int x, int y) {
        inputGrid[x][y].unhighlight();
    }

    @Override
    public void highlight(int x, int y) {
        inputGrid[x][y].highlight();
    }
}
