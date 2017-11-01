package uk.co.alt236.stubber.util.reflection;

import java.util.Locale;

public final class ValueSanitizer {

  private static final String POSITIVE_INFINITY_FORMULA = "1.0%s / 0.0%s";
  private static final String NEGATIVE_INFINITY_FORMULA = "-" + POSITIVE_INFINITY_FORMULA;
  private static final String NAN_FORMULA = "0.0%s / 0.0%s";

  public static String sanitize(final Class clazz,
                                final String value) {
    final String retVal;

    if (clazz == double.class) {
      retVal = sanitizeFloatingPoint(value, "D");
    } else if (clazz == float.class) {
      retVal = sanitizeFloatingPoint(value, "F");
    } else if (clazz == long.class) {
      retVal = value + "L";
    } else {
      retVal = value;
    }

    return retVal;
  }

  private static String sanitizeFloatingPoint(final String value,
                                              final String suffix) {
    final String retVal;
    switch (value) {
      case "Infinity":
        retVal = String.format(Locale.US, POSITIVE_INFINITY_FORMULA, suffix, suffix);
        break;
      case "-Infinity":
        retVal = String.format(Locale.US, NEGATIVE_INFINITY_FORMULA, suffix, suffix);
        break;
      case "NaN":
        retVal = String.format(Locale.US, NAN_FORMULA, suffix, suffix);
        break;
      default:
        retVal = value + suffix;
    }
    return retVal;
  }
}