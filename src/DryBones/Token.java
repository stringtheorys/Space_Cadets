package DryBones;

public class Token {

  private TokenType type;
  private String data;

  public Token(TokenType newType, String newData) {
    type = newType;
    data = newData;

    Log.newTokenPrint("New " + toString());
  }

  public Token(TokenType newType) {
    type = newType;

    Log.newTokenPrint("New " + toString());
  }

  public TokenType getType() {
    return type;
  }

  public String getData() {
    return data;
  }

  @Override
  public String toString() {
    if (data == null) {
      return "Token (" + type + ")";
    } else {
      return "Token(" + type + ", " + data + ")";
    }
  }
}
