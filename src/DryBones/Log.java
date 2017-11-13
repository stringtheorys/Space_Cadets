package DryBones;

public class Log {

  public static boolean lexerDebug = false;
  public static boolean newTokenDebug = true;

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
