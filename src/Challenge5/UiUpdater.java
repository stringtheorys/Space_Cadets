package Challenge5;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class UiUpdater extends Thread {

  private double oldX;
  private double oldY;
  private boolean running;
  private VBox canvas;

  private int t = 0;
  private double R = 0;
  private double r = 0;
  private double p = 0;
  private double speed;

  public UiUpdater(VBox newCanvas) {
    canvas = newCanvas;
    running = false;
  }

  public void setSettings(double new_R, double new_r, double new_p, double new_speed) {
    R = new_R;
    r = new_r;
    p = new_p;
    speed = new_speed;
  }

  @Override
  public void run() {
    super.run();

    running = true;

    try {
      while (running) {
        double nextX = getXPosition();
        double nextY = getYPosition();
        t++;

        Line line = new Line(oldX, oldY, nextX, nextY);
        line.setStroke(Color.RED);
        line.setStrokeWidth(2);

        oldX = nextX;
        oldY = nextY;

        Platform.runLater(() -> canvas.getChildren().add(line));

        sleep((int) speed);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void pause() {
    running = false;
  }

  private double getXPosition() {
    return (R + r) * Math.cos(t) + p * Math.cos((R + r) * t / r);
  }

  private double getYPosition() {
    return (R + r) * Math.sin(t) + p * Math.sin((R + r) * t / r);
  }
}
