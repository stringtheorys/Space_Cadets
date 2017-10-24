package Challenge3;

import java.util.Random;
import java.util.Stack;

public class Interpreter extends Thread {

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    interface InterpreterInterface {
        void interpreterFinishedRunning();
        void interpreterHasStopped();
        void printInteger(int num);
        void printChar(char character);
        void error(String errorMsg);
        void unhighlight(int x, int y);
        void highlight(int x, int y);
    }

    private final char[] digits = "0123456789".toCharArray();
    private final int gridSize = 20;

    private final int uiUpdateDelay = 200; // Milliseconds

    private char[][] codeGrid = new char[gridSize][gridSize];
    private int pc_x;
    private int pc_y;
    private Direction pc_direction = Direction.RIGHT;
    private boolean stringMode = false;
    private boolean running = false;
    private int loopCounter = 0;

    private Stack<Integer> dataStack = new Stack<>();

    private IDEScreen ideScreen;
    private InterpreterInterface  interpreterInterface;

    public Interpreter(IDEScreen newIDEScreen, InterpreterInterface newInterpreterInterface, char[][] code) {
        ideScreen = newIDEScreen;
        interpreterInterface = newInterpreterInterface;
        codeGrid = code;
    }

    public void runSingle() {
        start();

        runInterpreter(1);
    }

    public void runMulti() {
        start();

        runInterpreter(5);
    }

    public void runAll() {
        start();

        runInterpreter(1000);
    }

    public void runInterpreter(int maxLoopCounter) {
        while (running && loopCounter < maxLoopCounter) {
            loopCounter++;
            interpretChar();

            try {
                sleep(uiUpdateDelay);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (running) {
            interpreterInterface.interpreterFinishedRunning();
        } else {
            interpreterInterface.interpreterHasStopped();
        }
    }

    private void interpretChar() {

        if (codeGrid[pc_x][pc_y] == '\"') {
            stringMode = !stringMode;
        } else if (stringMode) {
            stackPush((int) codeGrid[pc_x][pc_y]);
        } else switch (codeGrid[pc_x][pc_y]) {

            case '+':
                if (dataStack.size() >= 2) {
                    int a = stackPop();
                    int b = stackPop();
                    stackPush(a + b);
                } else {
                    error("Addition failed, data stack too small");
                }
                break;

            case '-':
                if (dataStack.size() >= 2) {
                    int a = stackPop();
                    int b = stackPop();
                    stackPush(b - a);
                } else {
                    error("Subtraction failed, data stack too small");
                }
                break;

            case '*':
                if (dataStack.size() >= 2) {
                    int a = stackPop();
                    int b = stackPop();
                    stackPush(b * a);
                } else {
                    error("Multiplication failed, data stack too small");
                }
                break;

            case '/':
                if (dataStack.size() >= 2) {
                    int a = stackPop();
                    int b = stackPop();
                    if (a == 0) {
                        // TODO
                        //a = getUserInteger();
                    }
                    stackPush((int) Math.floor(b / a));
                } else {
                    dataStackError("Division");
                }
                break;

            case '%':
                if (dataStack.size() >= 2) {
                    int a = stackPop();
                    int b = stackPop();
                    stackPush(b % a);
                } else {
                    dataStackError("Modulo");
                }
                break;

            case '!':
                if (dataStack.size() >= 1) {
                    int a = stackPop();
                    if (a == 0) {
                        stackPush(1);
                    } else {
                        stackPush(0);
                    }
                } else {
                    dataStackError("Not");
                }
                break;

            case '`':
                if (dataStack.size() >= 2) {
                    int a = stackPop();
                    int b = stackPop();
                    if (b > a) {
                        stackPush(1);
                    } else {
                        stackPush(0);
                    }
                }
                break;

            case '>':
                pc_direction = Direction.RIGHT;
                break;

            case '<':
                pc_direction = Direction.LEFT;
                break;

            case '^':
                pc_direction = Direction.UP;
                break;

            case 'v':
                pc_direction = Direction.DOWN;
                break;

            case '?':
                int rndNum = new Random().nextInt(4);
                if (rndNum == 0) {
                    pc_direction = Direction.UP;
                } else if (rndNum == 1) {
                    pc_direction = Direction.DOWN;
                } else if (rndNum == 2) {
                    pc_direction = Direction.LEFT;
                } else {
                    pc_direction = Direction.UP;
                }
                break;

            case '_':
                if (dataStack.size() >= 1) {
                    int a = stackPop();
                    if (a == 0) {
                        pc_direction = Direction.RIGHT;
                    } else {
                        pc_direction = Direction.LEFT;
                    }
                } else {
                    dataStackError("Horizontal IF");
                }
                break;

            case '|':
                if (dataStack.size() >= 1) {
                    int a = stackPop();
                    if (a == 0) {
                        pc_direction = Direction.DOWN;
                    } else {
                        pc_direction = Direction.UP;
                    }
                } else {
                    dataStackError("Vertical IF");
                }
                break;

            case ':':
                if (dataStack.size() >= 1) {
                    int a = dataStack.peek();
                    stackPush(a);
                } else {
                    dataStackError("Duplicate");
                }
                break;

            case '\\':
                if (dataStack.size() >= 2) {
                    int a = stackPop();
                    int b = stackPop();
                    stackPush(b);
                    stackPush(a);
                } else {
                    dataStackError("Swap");
                }
                break;

            case '$':
                if (dataStack.size() >= 1) {
                    stackPop();
                } else {
                    dataStackError("Discard");
                }
                break;

            case '.':
                if (dataStack.size() >= 1) {
                    interpreterInterface.printInteger(stackPop());
                } else {
                    dataStackError("Output integer");
                }
                break;
            case ',':
                if (dataStack.size() >= 1) {
                    char a = (char) (int) stackPop();
                    interpreterInterface.printChar(a);
                } else {
                    dataStackError("Output ASCII");
                }
                break;

            case '#':
                movePC();
                break;

            case 'g':
                if (dataStack.size() >= 2) {
                    int a = stackPop();
                    int b = stackPop();

                    try {
                        stackPush((int) codeGrid[a][b]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        error("Get, failed out of bounds position");
                    }
                } else {
                    dataStackError("Get");
                }
                break;

            case 'p':
                if (dataStack.size() >= 3) {
                    int a = stackPop();
                    int b = stackPop();
                    int c = stackPop();

                    try {
                        codeGrid[a][b] = (char) c;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        error("Put, failed out of bounds position");
                    }
                } else {
                    dataStackError("Put");
                }
                break;

            case '&':
                stackPush(ideScreen.getIntInput());
                break;

            case '~':
                stackPush(ideScreen.getCharInput());
                break;

            case '@':
                running = false;
                interpreterInterface.interpreterHasStopped();
                break;

            default:
                for (char digit : digits) {
                    if (codeGrid[pc_x][pc_y] == digit) {
                        stackPush((int) codeGrid[pc_x][pc_y]);
                        return;
                    }
                }
                error("Character not recognised");
        }
    }

    private void stackPush(int element) {
        ideScreen.pushElement(element);
        dataStack.push(element);
    }

    private int stackPop() {
        ideScreen.popElement();
        return dataStack.pop();
    }

    private void dataStackError(String errorMsg) {
        error(errorMsg + " failed, data stack is too small");
    }

    private void error(String errorMsg) {
        interpreterInterface.error(errorMsg);
    }
    
    private void movePC() {
        interpreterInterface.unhighlight(pc_x, pc_y);
        if (pc_direction == Direction.UP) {
            if (pc_x > 0) {
                pc_x -= 1;
            } else {
                error("Program Counter can't move up");
            }
        } else if (pc_direction == Direction.DOWN) {
            if (pc_x < gridSize-1) {
                pc_x += 1;
            } else {
                error("Program Counter can't move down");
            }
        } else if (pc_direction == Direction.LEFT) {
            if (pc_y > 0) {
                pc_y -= 1;
            } else {
                error("Program Counter can't move left");
            }
        } else if (pc_direction == Direction.RIGHT){
            if (pc_y < gridSize-1) {
                pc_y += 1;
            } else {
                error("Program Counter can't move right");
            }
        }
        interpreterInterface.highlight(pc_x, pc_y);
    }

    public void stopInterpreter() {
        running = false;
    }
}
