package Challenge6;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.opencv.core.Core;

public class MainScene extends VBox {

  private ImageView output;
  private CameraCapture cameraCaptureThread;

  public MainScene() {

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    output = new ImageView();
    output.setFitHeight(400);
    output.setFitWidth(600);
    output.setPreserveRatio(true);

    Label test = new Label("Please work");
    getChildren().add(test);

    getChildren().add(output);

    cameraCaptureThread = new CameraCapture(output);
    cameraCaptureThread.start();
  }

  public void stopThread() {
    System.out.println("Stopping thread");
    cameraCaptureThread.stopCamera();
  }



}
