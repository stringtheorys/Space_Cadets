package Challenge6;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.Random;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

class CameraCapture extends Thread {

  private final int width = 640;
  private final int height = 480;
  private final int type = 0;

  private VideoCapture capture;
  private final ImageView output;
  private boolean running = true;

  private Mat outputMatrix = new Mat(width, height, type);
  private Mat captureMatrix = new Mat(width, height, type);

  private String option = "Normal Color";

  public CameraCapture(ImageView newOutput) {
    output = newOutput;
  }

  @Override
  public void run() {
    super.run();

    capture = new VideoCapture(0);

    if (capture.isOpened()) {
      while (running && capture.read(captureMatrix)) {

        BufferedImage bufferedImage;
        if (option.equals("Normal Color")) {
          bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
          outputMatrix = captureMatrix.clone();

        } else if (option.equals("Gray Scale")) {
          bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
          Imgproc.cvtColor(captureMatrix, outputMatrix, Imgproc.COLOR_RGB2GRAY);

        } else if (option.equals("Sobel X")) {
          bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
          Imgproc.cvtColor(captureMatrix, captureMatrix, Imgproc.COLOR_RGB2GRAY);
          Imgproc.Sobel(captureMatrix, outputMatrix, type, 1, 0);

        } else if (option.equals("Sobel Y")) {
          bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
          Imgproc.cvtColor(captureMatrix, captureMatrix, Imgproc.COLOR_RGB2GRAY);
          Imgproc.Sobel(captureMatrix, outputMatrix, type, 0, 1);

        } else if (option.equals("Sobel Both")) {
          bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
          Imgproc.cvtColor(captureMatrix, captureMatrix, Imgproc.COLOR_RGB2GRAY);
          final Mat sobelX = new Mat(width, height, type);
          final Mat sobelY = new Mat(width, height, type);
          Imgproc.Sobel(captureMatrix, sobelX, type, 1, 0);
          Imgproc.Sobel(captureMatrix, sobelY, type, 0, 1);
          Core.addWeighted(sobelX, 0.5, sobelY, 0.5, 1, outputMatrix);

        } else if (option.equals("Circle")) {
          bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
          Imgproc.cvtColor(captureMatrix, outputMatrix, Imgproc.COLOR_RGB2GRAY);
          Mat circles = new Mat();
          Imgproc.HoughCircles(outputMatrix, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 50, 0,200);
          if (circles.cols() > 0) {
            double[] circle = circles.get(0, 0);

            Point p = new Point(circle[0], circle[1]);
            double r = circle[2];

            Imgproc.circle(outputMatrix, p, (int) r, new Scalar(0,0,255), radius(r));
          }
        } else if (option.equals("All Circles")) {
          bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
          Imgproc.cvtColor(captureMatrix, outputMatrix, Imgproc.COLOR_RGB2GRAY);
          Mat circles = new Mat();
          Imgproc.HoughCircles(outputMatrix, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 50, 0,200);
          for (int x = 0; x < circles.cols(); x++) {

            double[] circle = circles.get(0, x);

            Point p = new Point(circle[0], circle[1]);
            double r = circle[2];

            Imgproc.circle(outputMatrix, p, (int) r, new Scalar(0,0,255), radius(r));
          }
        } else if (option.equals("Coloured Circles")) {
          bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
          Imgproc.cvtColor(captureMatrix, outputMatrix, Imgproc.COLOR_RGB2GRAY);
          Mat circles = new Mat();
          Imgproc.HoughCircles(outputMatrix, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 50, 0,200);
          for (int x = 0; x < circles.cols(); x++) {

            double[] circle = circles.get(0, x);

            Point p = new Point(circle[0], circle[1]);
            double r = circle[2];

            Imgproc.circle(outputMatrix, p, (int) r, circleColour(), radius(r));
          }
        } else {
          System.err.println("Unknown option:" + option);
          break;
        }

        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        outputMatrix.get(0, 0, data);

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

  public void changeOption(String newOption) {
    option = newOption;
  }

  private Scalar circleColour() {
    Random r = new Random();
    return new Scalar(r.nextInt(255), r.nextInt(255), r.nextInt(255));
  }

  private int radius(double r) {
    return (int) (r * 0.1);
  }

}
