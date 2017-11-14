package Challenge3;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

class Block extends TextField {

  public Block() {

    // Limits the text size
    textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.length() > 1) {
        setText(getText().substring(0, 1));
      }
    });

    int size = 20;
    setMinWidth(size);
    setMaxWidth(size);
    setMinHeight(size);
    setMaxHeight(size);
  }

  public char getInput() {
    if (getText().equals("")) {
      return ' ';
    } else {
      return getText().charAt(0);
    }
  }

  public void highlight() {
    setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
  }

  public void unhighlight() {
    setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
  }

}
