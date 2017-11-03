package Challenge5;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainScene extends VBox {

    private UiUpdater uiThread;
    private boolean running;

    public MainScene() {
        initGui();
    }

    private void initGui() {
        VBox box = new VBox();
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
        TextField R_textfield = new TextField();
        R_box.getChildren().addAll(R_label, R_textfield);

        HBox r_box = new HBox();
        Label r_label = new Label("r :");
        TextField r_textfield = new TextField();
        R_box.getChildren().addAll(r_label, r_textfield);

        HBox p_box = new HBox();
        Label p_label = new Label("r :");
        TextField p_textfield = new TextField();
        R_box.getChildren().addAll(p_label, p_textfield);

        getChildren().addAll(box, buttons, R_box, r_box, p_box);
    }

    private void runPause() {

    }
    private void stop() {

    }

}
