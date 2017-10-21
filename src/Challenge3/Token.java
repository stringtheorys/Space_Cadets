package Challenge3;

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

    @Override
    public String toString() {
        if (data == null) {
            return "Token (" + type + ")";
        } else {
            return "Token(" + type + ", " + data + ")";
        }
    }
}
