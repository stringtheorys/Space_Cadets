package Challenge2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Driver extends Application {

    private static final int width = 450;
    private static final int height = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("PaintF*ck Interpreter");

        MenuScreen menuScreen = new MenuScreen();
        Scene scene = new Scene(menuScreen, width, height, Color.WHITE);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
