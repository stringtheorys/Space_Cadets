package Challenge3;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Interpreter {

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private final char[] digits = "0123456789".toCharArray();
    private final int gridHeight = 30;
    private final int gridWidth = 20;

    private char[][] codeGrid = new char[30][20];
    private int pc_x;
    private int pc_y;
    private Direction pc_direction = Direction.RIGHT;
    private boolean stringMode = false;
    private boolean running = false;

    private Stack<Integer> dataStack = new Stack<>();

    private void error(String text) {
        System.err.println(text);
    }

    private void interpretChar() {

        if (codeGrid[pc_x][pc_y] == '\"') {
            stringMode = !stringMode;
        } else if (stringMode) {
            dataStack.push((int) codeGrid[pc_x][pc_y]);
        } else {
            switch(codeGrid[pc_x][pc_y]) {

                case '+':
                    if (dataStack.size() >= 2) {
                        int a = dataStack.pop();
                        int b = dataStack.pop();
                        dataStack.push(a + b);
                    } else {
                        error("Addition failed, data stack too small");
                    }
                    break;

                case '-':
                    if (dataStack.size() >= 2) {
                        int a = dataStack.pop();
                        int b = dataStack.pop();
                        dataStack.push(b - a);
                    } else {
                        error("Subtraction failed, data stack too small");
                    }
                    break;

                case '*':
                    if (dataStack.size() >= 2) {
                        int a = dataStack.pop();
                        int b = dataStack.pop();
                        dataStack.push(b * a);
                    } else {
                        error("Multiplication failed, data stack too small");
                    }
                    break;

                case '/':
                    if (dataStack.size() >= 2) {
                        int a = dataStack.pop();
                        int b = dataStack.pop();
                        if (a == 0) {
                            a = getUserInteger();
                        }
                        dataStack.push((int) Math.floor(b / a));
                    } else {
                        dataStackError("Division");
                    }
                    break;

                case '%':
                    if (dataStack.size() >= 2) {
                        int a = dataStack.pop();
                        int b = dataStack.pop();
                        dataStack.push(b % a);
                    } else {
                        dataStackError("Modulo");
                    }
                    break;

                case '!':
                    if (dataStack.size() >= 1){
                        int a = dataStack.pop();
                        if (a == 0) {
                            dataStack.push(1);
                        } else {
                            dataStack.push(0);
                        }
                    } else {
                        dataStackError("Not");
                    }
                    break;

                case '`':
                    if (dataStack.size() >= 2) {
                        int a = dataStack.pop();
                        int b = dataStack.pop();
                        if (b > a) {
                            dataStack.push(1);
                        } else {
                            dataStack.push(0);
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
                    if (dataStack.size() >= 1){
                        int a = dataStack.pop();
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
                    if (dataStack.size() >= 1){
                        int a = dataStack.pop();
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
                        dataStack.push(a);
                    } else {
                        dataStackError("Duplicate");
                    }
                    break;

                case '\\':
                    if (dataStack.size() >= 2) {
                        int a = dataStack.pop();
                        int b = dataStack.pop();
                        dataStack.push(b);
                        dataStack.push(a);
                    } else {
                        dataStackError("Swap");
                    }
                    break;

                case '$':
                    if (dataStack.size() >= 1){
                        dataStack.pop();
                    } else {
                        dataStackError("Discard");
                    }
                    break;

                case '.':
                    if (dataStack.size() >= 1){
                        System.out.print(dataStack.pop());
                    } else {
                        dataStackError("Output integer");
                    }
                    break;
                case ',':
                    if (dataStack.size() >= 1){
                        char a = (char) (int) dataStack.pop();
                        System.out.print(a);
                    } else {
                        dataStackError("Output ASCII");
                    }
                    break;

                case '#':
                    movePC();
                    break;

                case 'g':
                    if (dataStack.size() >= 2) {
                        int a = dataStack.pop();
                        int b = dataStack.pop();

                        try {
                            dataStack.push((int) codeGrid[a][b]);
                        } catch(ArrayIndexOutOfBoundsException e) {
                            error("Get, failed out of bounds position");
                        }
                    } else {
                        dataStackError("Get");
                    }
                    break;

                case 'p':
                    if (dataStack.size() >= 3) {
                        int a = dataStack.pop();
                        int b = dataStack.pop();
                        int c = dataStack.pop();

                        try {
                            codeGrid[a][b] = (char) c;
                        } catch(ArrayIndexOutOfBoundsException e) {
                            error("Put, failed out of bounds position");
                        }
                    } else {
                        dataStackError("Put");
                    }
                    break;

                case '&':
                    dataStack.push(getUserInteger());
                    break;

                case '~':
                    dataStack.push((int) getUserCharacter());
                    break;

                case '@':
                    running = false;
                    break;

                default:
                    for (char digit : digits) {
                        if (codeGrid[pc_x][pc_y] == digit) {
                            dataStack.push((int) codeGrid[pc_x][pc_y]);
                            return;
                        }
                    }
                    error("Character not recognised");
            }
        }
    }

    private void dataStackError(String errorMsg) {
        System.err.println(errorMsg + " failed, data stack is too small");
    }

    private int getUserInteger() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private int getUserCharacter() {
        Scanner scanner = new Scanner(System.in);
        String data = scanner.nextLine();
        return data.charAt(0);
    }

    private void movePC() {
        if (pc_direction == Direction.UP) {
            if (pc_x > 0) {
                pc_x -= 1;
            } else {
                error("Program Counter can't move up");
            }
        } else if (pc_direction == Direction.DOWN) {
            if (pc_x < gridHeight) {
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
            if (pc_y < gridWidth) {
                pc_y += 1;
            } else {
                error("Program Counter can't move right");
            }
        }
    }

}
