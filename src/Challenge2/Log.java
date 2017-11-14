package Challenge2;

// Quick class to control print statements
// This allows for prints statements to remain in code and not coded out
// But actived quickly and easily
class Log {

  static void print(String line) {
    boolean printing = false;
    if (printing) {
      System.out.println(line);
    }
  }

  static void defPrint(String line) {
    System.out.println(line);
  }
}
