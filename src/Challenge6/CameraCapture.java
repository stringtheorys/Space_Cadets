package Challenge6;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class CameraCapture extends Thread {

  private int width = 640;
  private int height = 480;
  private int type = 0;

  private VideoCapture capture;
  private ImageView output;
  private Mat capturedImageMatrix;
  private boolean running = true;

  private Mat sobelX = new Mat(width, height, type);
  private Mat sobelY = new Mat(width, height, type);
  private Mat sobelResult = new Mat(width, height, type);

  private int outputType = 0;

  public CameraCapture(ImageView newOutput) {
    output = newOutput;

  }

  @Override
  public void run() {
    super.run();

    capture = new VideoCapture(0);
    Mat captureMatrix = new Mat();

    if (capture.isOpened()) {
      while (running && capture.read(captureMatrix)) {
        //System.out.println("New Capture");

        // Convert to grayscale
        Imgproc.cvtColor(captureMatrix, captureMatrix, Imgproc.COLOR_RGB2GRAY);

        Imgproc.Sobel(captureMatrix, sobelX, type, 1, 0);
        Imgproc.Sobel(captureMatrix, sobelY, type, 0, 1);

        Core.addWeighted(sobelX, 0.5, sobelY, 0.5, 1, sobelResult);

        //BufferedImage bufferedImage = new BufferedImage(captureMatrix.width(), captureMatrix.height(), BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        // Sets the image data
        // Set as option
        sobelResult.get(0, 0, data);

        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        Platform.runLater(() -> output.setImage(image));


        try {
          Thread.sleep(100);
        } catch(InterruptedException ex) {
          ex.printStackTrace();
        }
      }
      capture.release();
      System.out.println("Cant capture");
    } else {
      System.out.println("Closed");
    }
  }

  public void stopCamera() {
    running = false;
    capture.release();
    System.out.println("Released Video");
  }
}
