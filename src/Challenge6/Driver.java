package Challenge6;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Driver extends Application {

  private MainScene mainScene;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    mainScene = new MainScene();
    Scene scene = new Scene(mainScene, 500, 500);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop();

    System.out.println("Stopping");
    mainScene.stopThread();
  }
}
