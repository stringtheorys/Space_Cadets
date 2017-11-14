package DryBones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

class Interpreter {

  private final Token[] tokens;
  private final int tokenLength;
  private int position = 0;
  private Token currentToken;

  private final HashMap<String, Variable> variableMap = new HashMap<>();
  private Stack<Integer> loopStack = new Stack<>();
  private Stack<ArrayList<String>> scopeStack = new Stack<>();

  public Interpreter(ArrayList<Token> newTokens) {
    tokens = (Token[]) newTokens.toArray();
    tokenLength = tokens.length;
    currentToken = tokens[0];
  }

  public void runInterpreter() {

    while (position < tokenLength) {

      interpretToken();
    }
  }

  private void error(String text) {
    System.err.println(text);
    System.exit(1);
  }

  private void interpretToken() {

    switch (currentToken.getType()) {
      // Declaring a new variable
      case INTEGER:
      case FLOAT:
      case STRING:
      case BOOLEAN:
        variableDeclaration();
        break;

      // Setting a variable to new value
      case VARIABLE:
        variableAssignment();
        break;

      // Output to console
      case OUTPUT:
        outputStatement();
        break;

      // Input to console
      case INPUT:
        inputStatement();
        break;

      // If Statement, deals with elif and else statements as well
      case IF:
        ifStatement();
        break;

      // While statement
      case WHILE:
        whileStatement();
        break;

      default:


    }
  }

  private void ifStatement() {

  }

  private void inputStatement() {
    Scanner scanner = new Scanner(System.in);
    advanceToken(TokenType.INPUT);

    // Check that the next token is a variable
    if (currentToken.getType() == TokenType.VARIABLE) {
      String variableName = currentToken.getData();
      advanceToken(TokenType.VARIABLE);

      // Check that the next token is a semicolon
      if (currentToken.getType() == TokenType.SEMICOLON) {
        advanceToken(TokenType.SEMICOLON);

        // Check if the variable exists
        Variable variable = doesVariableExist(variableName);
        if (variable != null) {
          String data = scanner.nextLine();

          setVariableData(variable, data);
        } else {
          error("Variable " + variableName + " is not created");
        }
      } else {
        error("Excepted token is semicolon, actual token is " + currentToken.getType());
      }
    } else {
      error("Excepted token is Variable, actual token is " + currentToken.getType());
    }
  }

  private void whileStatement() {
  }

  private void outputStatement() {
    advanceToken(TokenType.OUTPUT);
    if (peek().getType() != TokenType.SEMICOLON) {
      error("Excepted token is semicolon, actual token is " + peek().getType());
    }

    TokenType[] validOutputs = {
        TokenType.INTEGER_CONST, TokenType.FLOAT_CONST, TokenType.STRING_CONST,
        TokenType.BOOLEAN_CONST
    };

    for (TokenType tokenType : validOutputs) {
      if (currentToken.getType() == tokenType) {
        System.out.println(currentToken.getData());
        advanceToken(currentToken.getType());
        return;
      }
    }

    if (currentToken.getType() == TokenType.VARIABLE) {
      Variable variable = doesVariableExist(currentToken.getData());
      if (variable != null) {
        System.out.println(variable.getValue());
      } else {
        error("Variable (" + currentToken.getData() + ") does not exist");
      }
    } else {
      error("Excepted token is a constant or varaible, actual token is " + currentToken.getType());
    }
  }

  // Variable Statement: variableName = variableStatement
  private void variableAssignment() {

    String variableName = currentToken.getData();
    Variable variable = doesVariableExist(variableName);
    if (variable == null) {
      error("Variable (" + variableName + ") has not been created");
    }

    advanceToken(TokenType.VARIABLE);

    if (currentToken.getType() == TokenType.ASSIGN) {
      advanceToken(TokenType.ASSIGN);

      String data = variableStatement(variable.getType());
      setVariableData(variable, data);
    }
  }

  // Variable Declaration: variableType variableName (, variableName) (; | = data;)
  private void variableDeclaration() {

    // Variable Type
    TokenType variableType = currentToken.getType();
    advanceToken(currentToken.getType());

    // Get variable names
    ArrayList<String> variableNames = new ArrayList<>();
    while (currentToken.getType() == TokenType.VARIABLE) {
      variableNames.add(currentToken.getData());
      advanceToken(TokenType.VARIABLE);
    }

    // Check if assigned or put default value
    if (currentToken.getType() == TokenType.ASSIGN) {
      advanceToken(TokenType.ASSIGN);

      // TODO check if the type to set is the same
      String data = variableStatement(variableType);

      for (String variableName : variableNames) {
        Variable newVariable = Variable.newVariable(variableType, variableName, data);
        variableMap.put(variableName, newVariable);
      }

      // If SemiColon then just create all of the variables
    } else if (currentToken.getType() == TokenType.SEMICOLON) {
      for (String variableName : variableNames) {
        Variable newVariable = Variable.newVariable(variableType, variableName);
        variableMap.put(variableName, newVariable);
      }
    }
  }

  // TODO
  private String variableStatement(TokenType variableType) {

    return null;
  }

  private Variable doesVariableExist(String variableName) {
    return variableMap.get(variableName);
  }

  private void setVariableData(Variable variable, String data) {
    // Parse the data
    try {
      switch (variable.getType()) {
        case INTEGER:
          variable.setValue(Integer.parseInt(data));
        case FLOAT:
          variable.setValue(Float.parseFloat(data));
        case STRING:
          variable.setValue(data);
        case BOOLEAN:
          variable.setValue(Boolean.parseBoolean(data));
      }
    } catch (Exception e) {
      error("Variable cast fail");
    }
  }

  private void advanceToken(TokenType tokenType) {
    if (currentToken.getType() == tokenType) {
      position++;
      if (position < tokenLength) {
        currentToken = tokens[position];
      }
    } else {
      error("Unexcepted token: " + currentToken.getType() + " excepted type: " + tokenType);
    }
  }

  private Token peek() {
    if (position + 1 < tokenLength) {
      return tokens[position + 1];
    } else {
      System.out.println("Peek gets null");
      return null;
    }
  }
}
