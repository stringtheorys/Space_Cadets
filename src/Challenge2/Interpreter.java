package Challenge2;

import javafx.application.Platform;

import java.util.Stack;

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

    public interface InterpreterInterface {

        void updateUI(Colour[][] grid);
        void programEnd();
    }

    private final static int waitTime = 500; // Milliseconds
    private final static int maxLoops = 10000;
    private final static int maxUpdateTime = maxLoops-1;

    private static boolean running = true;

    private InterpreterInterface interpreterInterface;

    private int gridSize;
    private int x;
    private int y;
    private Colour[][] grid;

    private int programCounter;
    private int updateTime = 0;


    private Stack<Integer> loopPosition;

    private String code;

    Interpreter(InterpreterInterface newInterpreterInterface, int newGridSize, String newCode) {
        interpreterInterface = newInterpreterInterface;
        gridSize = newGridSize;
        code = newCode;

        grid = new Colour[gridSize][gridSize];
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                grid[x][y] = Colour.WHITE;
            }
        }

        x = 0;
        y = 0;
        programCounter = 0;
        updateTime = 0;
        loopPosition = new Stack<>();
    }

    @Override
    public void run() {
        System.out.println("I : Program starting");
        int currentLoops = 0;
        while (programCounter < code.length() && running && currentLoops < maxLoops) {
            System.out.println("PC :" + programCounter + " X :" + x + " Y :" + y);
            interpretNextChar();
            currentLoops++;
        }

        if (currentLoops == maxLoops) {
            System.out.println("Max loops");
        } else if (running) {
            System.out.println("I : UpdateUI");
            Platform.runLater(() -> interpreterInterface.updateUI(grid));
            System.out.println("I : Program End");
            interpreterInterface.programEnd();
        } else {
            System.out.println("I : Program Paused");
        }
    }

    private void interpretNextChar() {
        switch(code.charAt(programCounter)) {
            case('n'):
                y--;
                if (y < 0) {
                    y = gridSize - 1;
                }
                System.out.println("I : Pointer move North");
                break;

            case('s'):
                y = (y+1) % gridSize;
                System.out.println("I : Pointer move South");
                break;

            case('w'):
                x--;
                if (x < 0) {
                    x = gridSize-1;
                }
                System.out.println("I : Pointer move West");
                break;

            case('e'):
                x = (x+1) % gridSize;
                System.out.println("I : Pointer move East");
                break;

            case('['):
                loopPosition.push(programCounter);
                System.out.println("I : While loop start");
                break;

            case(']'):
                if (grid[x][y] == Colour.BLACK) {
                    programCounter = loopPosition.peek();
                    System.out.println("I : While loop end, restart");
                } else {
                    loopPosition.pop();
                    System.out.println("I : While loop end, stop");
                }
                break;

            case('*'):
                System.out.println("I : Flip colour");
                grid[x][y] = (grid[x][y] == Colour.WHITE) ? Colour.BLACK : Colour.WHITE;
                break;

            case('%'):
                System.out.println("I : Rotate Colour");
                grid[x][y] = Colour.rotateColour(grid[x][y]);
                break;
        }

        programCounter++;

        if (updateTime >= maxUpdateTime) {
            System.out.println("I : UpdateUI");

            Platform.runLater(() -> interpreterInterface.updateUI(grid));

            updateTime = 0;
        } else {
            updateTime++;
        }
    }

    void pauseInterpreter() {
        System.out.println("I : Pausing Interpreter");
        running = false;
    }
}
