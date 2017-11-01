package uk.co.alt236.stubber.util.reflection;

public class DefaultReturnValues {

  public static String forClass(final Class<?> fieldClass) {
    final String retVal;
    if (fieldClass == int.class) {
      retVal = "0";
    } else if (fieldClass == double.class) {
      retVal = "0.0";
    } else if (fieldClass == boolean.class) {
      retVal = "false";
    } else if (fieldClass == float.class) {
      retVal = "0.0";
    } else if (fieldClass == String.class) {
      retVal = "null";
    } else if (fieldClass == short.class) {
      retVal = "0";
    } else if (fieldClass == byte.class) {
      retVal = "0";
    } else if (fieldClass == long.class) {
      retVal = "0";
    } else if (fieldClass == char.class) {
      retVal = "0";
    } else {
      retVal = "null";
    }

    return ValueSanitizer.sanitize(fieldClass, retVal);
  }
}
