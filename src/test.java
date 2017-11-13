public class test {

  public static void main(String[] args) {
    String props = System.getProperty("java.library.path");
    for (String prop : props.split(";")) {
      System.out.println(prop);
    }
  }

}
