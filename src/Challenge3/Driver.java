package Challenge3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// Starting class that controls the javafx application
public class Driver extends Application {

  // Application size
  private static final int width = 700;
  private static final int height = 500;

  // Main function
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  // Runs when the application is started
  public void start(Stage primaryStage) throws Exception {

    primaryStage.setTitle("Befunge Interpreter");

    // Sets the MenuScreen as the scene
    IDEScreen menuScreen = new IDEScreen();
    Scene scene = new Scene(menuScreen, width, height, Color.WHITE);
    primaryStage.setScene(scene);

    // Show the scene
    primaryStage.show();
  }
}
