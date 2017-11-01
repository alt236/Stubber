package uk.co.alt236.stubber.util.reflection.otsv;

import uk.co.alt236.stubber.util.reflection.ValueSanitizer;

import java.lang.reflect.Field;

/*package*/ class PrimitiveValue implements ValueGetter {

  private static String getChar(final Object object) {
    String character = String.valueOf(object);

    switch (character) {
      case "'":
        character = "\\" + character;
    }

    return "'" + character + "'";
  }

  @Override
  public ObjectToStringValue.Result getValue(final Field field) throws IllegalAccessException {
    final Class<?> clazz = field.getType();
    final Object object = field.get(null);

    return getValueInternal(clazz, object);
  }

  @Override
  public ObjectToStringValue.Result getValue(final Object object) {
    return getValueInternal(object.getClass(), object);
  }

  private ObjectToStringValue.Result getValueInternal(final Class<?> clazz,
                                                      final Object object) {
    final String value;
    boolean error = false;

    if (clazz == int.class) {
      value = String.valueOf(object);
    } else if (clazz == double.class) {
      value = String.valueOf(object);
    } else if (clazz == boolean.class) {
      value = String.valueOf(object);
    } else if (clazz == float.class) {
      value = String.valueOf(object);
    } else if (clazz == short.class) {
      value = String.valueOf(object);
    } else if (clazz == byte.class) {
      value = String.valueOf(object);
    } else if (clazz == long.class) {
      value = String.valueOf(object);
    } else if (clazz == char.class) {
      value = getChar(object);
    } else {
      error = true;
      value = null;
    }

    final String suffixedValue = ValueSanitizer.sanitize(clazz, value);
    return new ObjectToStringValue.Result(clazz, error, suffixedValue);
  }
}
