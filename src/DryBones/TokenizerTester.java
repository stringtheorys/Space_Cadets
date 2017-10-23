package DryBones;

import java.util.ArrayList;

public class TokenizerTester {

    public static void main(String[] args) {
        new TokenizerTester();
    }

    public TokenizerTester() {
        //testVariableConsts();
        //testVariableTypes();
        //testVariableNames();
        testComment();
    }

    // Success
    private void testVariableConsts() {

        // Integers
        runCode("5");

        // Floats
        runCode("5.");
        runCode("1.0");

        // Boolean
        runCode("TRUE");
        runCode("FALSE");

        // String
        runCode("\"Const\"");
    }

    // Success
    private void testVariableTypes() {
        runCode("INTEGER");
        runCode("BOOLEAN");
        runCode("STRING");
        runCode("FLOAT");
    }

    // Success
    private void testVariableNames() {
        runCode("a");
        runCode("a ");
        runCode("a1");
        runCode("a1 ");
        runCode("A1");
        //runCode("1a"); // Should fail
        //runCode("_a"); // Should fail
        runCode("var_test1 ");
        runCode("var test"); // Two variables
    }

    private void testOperations() {
        runCode("+ - * / +-*/"); // Integer and float operations
        runCode("( )"); // Parenthes
        runCode("=="); // Equal
        runCode("!="); // Not Equal
        runCode("< >"); // Greater and Less
        runCode("! "); // Not
        runCode("&&"); // And
        runCode("||"); // Or
        runCode("= ; ,");
    }

    private void testKeywords() {
        runCode("IF THEN ELIF ELSE END"); // If statements
        runCode("WHILE DO END"); // While statement

        runCode("INPUT"); // INPUT statement
        runCode("OUTPUT;"); // OUTPUT statement
    }

    private void testComment() {
        runCode("// Comment \n5"); // Single line comment
        runCode("/* This is the start of a comment \n\n this is two lines later \n */5");
    }

    private void runCode(String code) {
        ArrayList<Token> tokens = new Lexer(code).runLexer();
        //printAllTokens(tokens);

        System.out.println();
    }


    private void printAllTokens(ArrayList<Token> tokenList) {

        for (Token token : tokenList)  {
            System.out.println(token);
        }
    }
}
