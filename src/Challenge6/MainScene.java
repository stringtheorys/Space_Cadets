package Challenge6;

import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.opencv.core.Core;

class MainScene extends VBox {

  private final CameraCapture cameraCaptureThread;

  public MainScene() {

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    ComboBox<String> options = new ComboBox<>();
    options.getItems().addAll(
        "Normal Color",
        "Gray Scale",
        "Sobel X",
        "Sobel Y",
        "Sobel Both",
        "Circle",
        "All Circles",
        "Coloured Circles"
    );
    options.setValue("Normal Color");
    options.valueProperty().addListener((observable, oldValue, newValue) ->
      optionChange(newValue)
    );

    ImageView output = new ImageView();
    output.setFitHeight(400);
    output.setFitWidth(600);
    output.setPreserveRatio(true);

    getChildren().addAll(output, options);

    cameraCaptureThread = new CameraCapture(output);
    cameraCaptureThread.start();
  }

  public void stopThread() {
    System.out.println("Stopping thread");
    cameraCaptureThread.stopCamera();
  }

  private void optionChange(String newvalue) {
    cameraCaptureThread.changeOption(newvalue);
  }
}
