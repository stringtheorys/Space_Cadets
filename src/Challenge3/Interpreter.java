package Challenge3;

import java.util.ArrayList;

public class Interpreter {

    private Token[] tokens;
    private int position = 0;
    private Token currentToken;

    public Interpreter(ArrayList<Token> newTokens) {
        tokens = (Token[]) newTokens.toArray();
    }

    public void runInterpreter() {

    }
}
