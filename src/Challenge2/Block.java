package Challenge2;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

// Class for a label which represents a square on the output grid
class Block extends Label {

    // Background possiblities
    private static final Background blackBackground = new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)
    );
    private static final Background whiteBackground = new Background(
            new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
    );

    // The size of a cell
    private static int cellSize = 10;

    // The current background colour
    private boolean currentColour = true;

    // Constructor
    Block() {

        setMinSize(cellSize, cellSize);
        setMaxSize(cellSize, cellSize);

        setColour(false);

    }

    // Changes the backgrounds
    void setColour(boolean colour) {
        if (colour != currentColour) {
            if (colour) {
                setBackground(blackBackground);
            } else {
                setBackground(whiteBackground);
            }
            currentColour = colour;
        }
    }

    // Changes the block size
    static void setSize(int newSize) {
        cellSize = newSize;
    }


}
