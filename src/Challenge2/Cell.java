package Challenge2;

import javafx.scene.control.Label;

public class Cell extends Label {

    private static int cellSize = 10;

    private Colour currentColour = Colour.WHITE;

    Cell() {

        setMinSize(cellSize, cellSize);
        setMaxSize(cellSize, cellSize);

        setColour(currentColour);
    }

    void setColour(Colour colour) {
        if (colour != currentColour) {
            setBackground(Colour.backgroundColours[Colour.getColourPosition(colour)]);
        }
    }

    public static void setSize(int newSize) {
        cellSize = newSize;
    }
}
