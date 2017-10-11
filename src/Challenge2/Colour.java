package Challenge2;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.Random;

public enum Colour {

    BLACK,
    WHITE,
    RED,
    GREEN,
    BLUE;

    static Background[] backgroundColours = new Background[] {
            new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)),
            new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)),
            new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)),
            new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)),
            new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY))
    };

    static Colour[] nextColour = new Colour[] {
            WHITE, RED, GREEN, BLUE, BLACK
    };

    static Random rnd = new Random();

    static Background getRndBackground() {
        return backgroundColours[rnd.nextInt(backgroundColours.length)];
    }

    static int getColourPosition(Colour colour) {
        if (colour == BLACK) {
            return 0;
        } else if (colour == WHITE) {
            return 1;
        } else if (colour == RED) {
            return 2;
        } else if (colour == GREEN) {
            return 3;
        } else if (colour == BLUE) {
            return 4;
        } else {
            return -1;
        }
    }
}
