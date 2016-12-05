package uk.co.alt236.stubber.util;

public final class Log {

  private static final String INDENT_CLASS = "\t";
  private static final String INDENT_INNER_CLASS = "\t\t";
  private static final String INDENT_FIELD = "\t\t";
  private static final String INDENT_ANNOT = "\t\t";

  private Log() {
    // NOOP
  }

  public static void err(final String message) {
    System.err.println(message);
  }

  public static void errField(final String message) {
    System.err.println(INDENT_FIELD + message);
  }

  public static void out(final String message) {
    System.out.println(message);
  }

  public static void outAnnotation(final String message) {
    System.out.println(INDENT_ANNOT + message);
  }

  public static void outClass(final String message) {
    System.out.println(INDENT_CLASS + message);
  }

  public static void outField(final String message) {
    System.out.println(INDENT_FIELD + message);
  }

  public static void outInnerClass(final String message) {
    System.out.println(INDENT_INNER_CLASS + message);
  }
}
