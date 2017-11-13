package Challenge5;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainScene extends VBox {

  private Slider R_slider, r_slider, p_slider, speed_slider;

  private UiUpdater uiThread;
  private boolean start;
  private boolean running;

  private VBox box;

  public MainScene() {
    initGui();
  }

  private void initGui() {
    box = new VBox();
    box.setMinWidth(300);
    box.setMinHeight(300);

    HBox buttons = new HBox();
    Button runPauseBtn = new Button("Run");
    runPauseBtn.setOnMouseClicked(event -> runPause());
    Button stopBtn = new Button("Stop");
    stopBtn.setOnMouseClicked(event -> stop());
    buttons.getChildren().addAll(runPauseBtn, stopBtn);

    HBox R_box = new HBox();
    Label R_label = new Label("R :");
    R_slider = new Slider();
    R_slider.setMin(-200);
    R_slider.setMax(200);
    R_box.getChildren().addAll(R_label, R_slider);

    HBox r_box = new HBox();
    Label r_label = new Label("r :");
    r_slider = new Slider();
    r_slider.setMin(-200);
    r_slider.setMax(200);
    r_box.getChildren().addAll(r_label, r_slider);

    HBox p_box = new HBox();
    Label p_label = new Label("p :");
    p_slider = new Slider();
    p_slider.setMin(-200);
    p_slider.setMax(200);
    p_box.getChildren().addAll(p_label, p_slider);

    HBox speed_box = new HBox();
    Label speed_label = new Label("Speed :");
    speed_slider = new Slider();
    speed_slider.setMin(5);
    speed_slider.setMax(1000);

    speed_box.getChildren().addAll(speed_label, speed_slider);

    getChildren().addAll(box, buttons, R_box, r_box, p_box, speed_box);
  }

  private void runPause() {
    if (start) {
      if (running) {
        uiThread.pause();
        running = false;
      } else {
        uiThread.start();
        running = true;
      }
    } else {
      uiThread = new UiUpdater(box);

      double R = R_slider.getValue();
      double r = r_slider.getValue();
      double p = p_slider.getValue();
      double speed = speed_slider.getValue();
      System.out.println("R :" + R);
      System.out.println("r :" + r);
      System.out.println("p :" + p);
      System.out.println("Speed :" + speed);
      uiThread.setSettings(R, r, p, speed);

      start = true;
      running = true;

      uiThread.start();
    }
  }

  private void stop() {
    start = false;
  }
}
