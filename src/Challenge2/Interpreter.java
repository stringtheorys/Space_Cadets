package Challenge2;

import java.util.Stack;

// Interpreter for Paintf*ck and runs as a thread
public class Interpreter extends Thread {

    /*
    Commands

    n - Move data pointer north
    s - Move data pointer south
    w - Move data pointer west
    e - Move data pointer east
    * - Flip data pointer cell Black -> White or White -> Black
    [ - While data is not 0
    ] - End of While loop
    % - Jump to next colour
     */

  // Interface for the interpreter to communicate with the menuScreen
  public interface InterpreterInterface {

    void addOutputGrid(boolean[][] colourGrid);

    void programEnd();

    void maxLoops();
  }

  // Attributes
  private int maxLoops = 10000; // Number of times the program counter ticks over
  private int uiUpdateTime = 100; // Number of times till the Ui is updated

  private int gridSize; // The size of the colour grid
  private int programCounter = 0; // The position in the code
  private int updateTime = 0; // The number of times till ui update
  private int loopNumber = 0; // The number of time the code will loop
  private Stack<Integer> loopPositions = new Stack<>(); // The loop positions
  private int x_pointer = 0; // The pointer in the horizontal direction
  private int y_pointer = 0; // The pointer in the vertical direction
  private boolean[][] colourGrid; // The two dimensional array of grid elements

  private String code; // The code to run

  private InterpreterInterface interpreterInterface; // The interface to communicate with the menu screen

  Interpreter(InterpreterInterface newInterpreterInterface, int newGridSize, String newCode) {
    interpreterInterface = newInterpreterInterface;
    gridSize = newGridSize;
    code = newCode;

    colourGrid = new boolean[gridSize][gridSize];
  }

  @Override
  public void run() {

    Log.defPrint("I : Interpreter starting");
    // Interpreters the code while the program counter is not at the end of the code and the loopNumber is less than the maxLoops
    // So to prevent an infinite loop
    // This could be done with a hashset but is memory expensive
    while (programCounter < code.length() && loopNumber < maxLoops) {
      // Interpreter the next character in the code
      interpretNextChar();

      // Increment variables
      loopNumber++;
      programCounter++;
      updateTime++;

      if (updateTime == uiUpdateTime) {
        interpreterInterface.addOutputGrid(colourGrid);
        updateTime = 0;
      }
    }

    // Program or loop end
    Log.print("I : UpdateUI");
    interpreterInterface.addOutputGrid(colourGrid);

    // Possible reasons that the while loops stops
    if (programCounter == code.length()) {
      Log.defPrint("I : Program end");
      interpreterInterface.programEnd();
    } else if (loopNumber == maxLoops) {
      Log.defPrint("I : Max loops");
      interpreterInterface.maxLoops();
    }
  }

  // Interpert's a character
  private void interpretNextChar() {
    Log.print("I : Program code :" + code.charAt(programCounter));
    switch (code.charAt(programCounter)) {
      case ('s'):
        y_pointer = (y_pointer + 1) % gridSize;
        break;

      case ('w'):
        x_pointer--;
        if (x_pointer < 0) {
          x_pointer = gridSize - 1;
        }
        break;

      case ('e'):
        x_pointer = (x_pointer + 1) % gridSize;
        break;

      case ('['):
        loopPositions.push(programCounter);
        break;

      case (']'):
        // If the position is black then reset the loop else continue
        if (colourGrid[x_pointer][y_pointer]) {
          programCounter = loopPositions.peek();
        } else {
          loopPositions.pop();
        }
        break;

      case ('*'):
        colourGrid[x_pointer][y_pointer] = !colourGrid[x_pointer][y_pointer];
        break;
    }
  }
}

