package DryBones;

class Log {

  private static final boolean lexerDebug = false;
  private static final boolean newTokenDebug = true;

  public static void lexerPrint(String text) {
    if (lexerDebug) {
      System.out.println(text);
    }
  }

  public static void newTokenPrint(String text) {
    if (newTokenDebug) {
      System.out.println(text);
    }
  }
}
