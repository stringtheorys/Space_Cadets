package Challenge2;

import javafx.scene.control.Label;

class Cell extends Label {

    private static int cellSize = 20;

    private Colour currentColour;

    Cell() {

        setMinSize(cellSize, cellSize);
        setMaxSize(cellSize, cellSize);

        setColour(Colour.WHITE);
    }

    void setColour(Colour colour) {
        if (colour != currentColour) {
            setBackground(Colour.backgroundColours[Colour.getColourPosition(colour)]);
            currentColour = colour;
        }
    }

    static void setSize(int newSize) {
        cellSize = newSize;
    }
}
