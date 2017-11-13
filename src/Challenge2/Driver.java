package Challenge2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// Starting class that controls the javafx application
public class Driver extends Application {

  // Application size
  private static final int width = 450;
  private static final int height = 600;

  // Main function
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  // Runs when the application is started
  public void start(Stage primaryStage) throws Exception {

    primaryStage.setTitle("PaintF*ck Interpreter");

    // Sets the MenuScreen as the scene
    MenuScreen menuScreen = new MenuScreen();
    Scene scene = new Scene(menuScreen, width, height, Color.WHITE);
    primaryStage.setScene(scene);

    // Show the scene
    primaryStage.show();
  }
}
