package Challenge3;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IDEScreen extends VBox {

    private int gridSize = 20;

    private Block[][] inputGrid = new Block[gridSize][gridSize];

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
        Label consoleBox = new Label("Console\n");

        // Console input
        HBox consoleInputBox = new HBox();
        Label inputLabel = new Label("Input :");
        TextField inputField = new TextField();
        consoleInputBox.getChildren().addAll(inputLabel, inputField);

        VBox rightBox = new VBox(inputBox, consoleBox, consoleInputBox);

        StackBar stackBar = new StackBar();
        // TODO
        HBox buttons = new HBox();

        rightBox.getChildren().addAll(stackBar, buttons);

        mainBox.getChildren().addAll(leftBox, rightBox);
        getChildren().addAll(topLabel, mainBox);
    }


}
