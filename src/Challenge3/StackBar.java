package Challenge3;

import java.util.Stack;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StackBar extends VBox {

  private final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

  private Stack<Label> elements = new Stack<>();

  public StackBar() {
    Label topLabel = new Label("Current Stack");
    getChildren().add(topLabel);
  }

  public void clearStackBar() {
    getChildren().removeAll(elements);
  }

  public void pushElement(int element) {
    char charVersion = (char) element;
    Label l = new Label();

    for (char alpha : alphabet) {
      if (charVersion == alpha) {
        l.setText(Character.toString(alpha));
        break;
      }
    }
    if (l.getText().equals("")) {
      l.setText(Integer.toString(element));
    }
    elements.push(l);
    getChildren().add(l);
  }

  public void popElement() {
    Label l = elements.pop();
    getChildren().remove(l);
  }
}
