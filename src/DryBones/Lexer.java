package DryBones;

import java.util.ArrayList;

// Lexer class
public class Lexer {

  private static final char EOF = 0;

  // Really lazy I know
  private static final String alphabet =
      "abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase();
  private static final String digits = "0123456789";

  // Character
  private final char[] validNumbersCharacters = digits.toCharArray();
  private final char[] initalValidVariableCharacters = alphabet.toCharArray();
  private final char[] validVariableCharacters = (alphabet + digits + "_").toCharArray();
  private final char[] validCharactersAfterNumber = ("+-*/ ()" + EOF).toCharArray();

  // Attributes
  private String code; // Code text
  private int position; // Code position
  private char currentChar; // current character in the text
  private int codeLength; // The length of code

  // Constructor
  public Lexer(String newCode) {
    code = newCode;
    codeLength = code.length();

    Log.lexerPrint("Code Length: " + codeLength);

    position = -1;
    advancePosition();
  }

  ArrayList<Token> runLexer() {
    ArrayList<Token> tokens = new ArrayList<>();

    while (position < codeLength) {
      if (currentChar == ' ') {
        advancePosition();
        continue;
      }
      Token token = getNextToken();
      if (token == null) {
        Log.lexerPrint("Null token");
        break;
      } else {
        tokens.add(token);
      }
    }

    return tokens;
  }

  // If error code has occurred, then error message shown
  private void error(String errorMsg) {
    System.err.println(errorMsg);

    // Stop the program
    System.exit(1);
  }

  // Gets the next character after the current position
  private char peek() {
    if (position + 1 < codeLength) {
      return code.charAt(position + 1);
    } else {
      return 0;
    }
  }

  // Gets the next token from the code
  private Token getNextToken() {

    // Searches the possible character combinations
    switch (currentChar) {

      case ('+'):
        advancePosition();
        return new Token(TokenType.PLUS);
      case ('-'):
        advancePosition();
        return new Token(TokenType.MINUS);
      case ('*'):
        advancePosition();
        return new Token(TokenType.MUL);
      case ('/'):
        // Could be a single or multi line comment or divide symbol
        if (peek() == '/') {
          skipSingleLineComment();
          break;
        } else if (peek() == '*') {
          skipMultiLineCommment();
        } else {
          advancePosition();
          return new Token(TokenType.DIV);
        }
      case ('('):
        advancePosition();
        return new Token(TokenType.LPAREN);
      case (')'):
        advancePosition();
        return new Token(TokenType.RPAREN);
      case (';'):
        advancePosition();
        return new Token(TokenType.SEMICOLON);
      case ('='):
        // Could be equals or assigned
        advancePosition();
        if (peek() == '=') {
          advancePosition();
          return new Token(TokenType.EQUAL);
        } else {
          return new Token(TokenType.ASSIGN);
        }
      case ('!'):
        advancePosition();
        // Could be not equals and not boolean
        if (peek() == '=') {
          advancePosition();
          return new Token(TokenType.NOTEQUAL);
        } else {
          return new Token(TokenType.NOT);
        }
      case ('&'):
        // Should be AND or invalid
        if (peek() == '&') {
          advancePosition();
          advancePosition();
          return new Token(TokenType.AND);
        } else {
          error("Invalid Character &");
          return null;
        }
      case ('|'):
        // Should be OR or invalid
        if (peek() == '|') {
          advancePosition();
          advancePosition();
          return new Token(TokenType.OR);
        } else {
          error("Invalid Character |");
          return null;
        }
      case ('>'):
        advancePosition();
        return new Token(TokenType.GREATER);
      case ('<'):
        advancePosition();
        return new Token(TokenType.LESS);
      case (','):
        advancePosition();
        return new Token(TokenType.COMMA);
    }

    // If not one of the normal token types then must be
    // Variable name, variable type, variable consts

    // Check if number or word
    if (isNumber()) {
      return getNumberToken();
    } else {
      return getWordToken();
    }
  }

  // The first character is not a number
  private Token getWordToken() {

    // Check if String constant
    if (currentChar == '\"') {
      Log.lexerPrint("Is String Constant");
      StringBuilder string = new StringBuilder();
      advancePosition();

      // Ignore all characters but the last "
      while (currentChar != '\"') {
        string.append(currentChar);
        advancePosition();
      }
      advancePosition();

      return new Token(TokenType.STRING_CONST, string.toString());
    }

    // Word builder instead of adding strings, more efficient
    StringBuilder wordBuilder = new StringBuilder();

    // Check if the first character is valid, must be a letter
    boolean validFirstCharacter = false;
    for (char validCharacter : initalValidVariableCharacters) {
      if (currentChar == validCharacter) {
        validFirstCharacter = true;
        Log.lexerPrint("Valid First Character");
        break;
      }
    }

    // If valid, continue else stop and exception
    if (validFirstCharacter) {
      wordBuilder.append(currentChar);
      advancePosition();
    } else {
      error("Invalid first character of word: " + currentChar + " code: " + code + " position: "
          + position);
      return null;
    }

    // While the character is valid: letters, numbers, _ then continue
    while (isValidCharacter()) {
      wordBuilder.append(currentChar);
      advancePosition();
    }

    // Check if the word is recognised token type
    String word = wordBuilder.toString();
    switch (word) {
      case "IF":
        return new Token(TokenType.IF);
      case "THEN":
        return new Token(TokenType.THEN);
      case "ELIF":
        return new Token(TokenType.ELIF);
      case "ELSE":
        return new Token(TokenType.ElSE);
      case "END":
        return new Token(TokenType.END);
      case "WHILE":
        return new Token(TokenType.WHILE);
      case "DO":
        return new Token(TokenType.DO);
      case "TRUE":
      case "FALSE":
        return new Token(TokenType.BOOLEAN_CONST, word);
      case "INTEGER":
        return new Token(TokenType.INTEGER);
      case "STRING":
        return new Token(TokenType.STRING);
      case "BOOLEAN":
        return new Token(TokenType.BOOLEAN);
      case "FLOAT":
        return new Token(TokenType.FLOAT);
      case "INPUT":
        return new Token(TokenType.INPUT);
      case "OUTPUT":
        return new Token(TokenType.OUTPUT);
      default: // If unrecognised then must be a variable name
        return new Token(TokenType.VARIABLE, word);
    }
  }

  // Gets the number token
  private Token getNumberToken() {

    StringBuilder number = new StringBuilder();
    boolean isFloat = false; // If a dot is found in the number

    // Loop while the character is a letter
    do {
      number.append(currentChar);
      advancePosition();

      if (currentChar == '.') {
        if (isFloat) { // Two dots cant exist in the number
          error("Unknown number");
          return null;
        } else {
          Log.lexerPrint("Is Float");
          number.append(currentChar);
          advancePosition();
          isFloat = true;
        }
      }
    } while (isNumber());

    // Check that is following character is valid +-*/ ()
    for (char validCharacter : validCharactersAfterNumber) {
      if (currentChar == validCharacter) {
        if (isFloat) { // If the number contains a dot then must be a float else integer
          return new Token(TokenType.FLOAT_CONST, number.toString());
        } else {
          return new Token(TokenType.INTEGER_CONST, number.toString());
        }
      }
    }

    // Invalid character after number
    error("Invalid character after number");
    return null;
  }

  // Check if the current character is a number
  private boolean isNumber() {
    for (char number : validNumbersCharacters) {
      if (number == currentChar) {
        return true;
      }
    }
    return false;
  }

  // Check if the current character is a valid variable characters
  private boolean isValidCharacter() {
    for (char validCharacter : validVariableCharacters) {
      if (currentChar == validCharacter) {
        Log.lexerPrint("Is Valid Character");
        return true;
      }
    }
    return false;
  }

  // Advance the current position, updating the position and current character
  private void advancePosition() {
    position++;
    if (position < codeLength) {
      currentChar = code.charAt(position);
      Log.lexerPrint("AP Character :" + currentChar + " at position: " + position);
    } else {
      currentChar = EOF;
      Log.lexerPrint("EOF");
    }
  }

  // Skip a single line comment
  private void skipSingleLineComment() {
    while (currentChar != '\n') {
      advancePosition();
    }
    advancePosition();
  }

  // Skip a multi line comment
  private void skipMultiLineCommment() {
    while (currentChar != '*' && peek() != '/') {
      advancePosition();
    }
    advancePosition();
  }

  // Check if at the end of the code
  private boolean isEndOfCode() {
    return position == codeLength;
  }
}
