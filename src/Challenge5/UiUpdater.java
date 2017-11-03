package Challenge5;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class UiUpdater extends Thread {

    private double oldX;
    private double oldY;
    private boolean running;
    private int speed;
    private VBox canvas;

    private int t = 0;
    private int R = 0;
    private int r = 0;
    private int p = 0;

    public UiUpdater(VBox newCanvas) {
        canvas = newCanvas;
        running = true;
    }

    public void setSettings(int new_R, int new_r, int new_p) {
        R = new_R;
        r = new_r;
        p = new_p;
    }

    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    @Override
    public void run() {
        super.run();

        try {
            while (running) {
                double nextX = getXPosition();
                double nextY = getYPosition();
                t++;

                Line line = new Line(oldX, oldY, nextX, nextY);
                oldX = nextX;
                oldY = nextY;

                Platform.runLater(() -> canvas.getChildren().add(line));

                sleep(speed);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private double getXPosition() {
        return (R+r) * Math.cos(t) + p*Math.cos((R+r)*t/r);
    }

    private double getYPosition() {
        return (R+r) * Math.sin(t) + p*Math.sin((R+r)*t/r);
    }
}
