package uk.co.alt236.stubber.util.reflection.otsv;

import java.lang.reflect.Field;

/*package*/ class EnumValue implements ValueGetter {

  private static final String ARRAY_START = "{";
  private static final String ARRAY_END = "}";
  private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;

  private static String constructName(final Object object) {
    if (object == null) {
      return "null";
    } else {
      return object.getClass().getCanonicalName() + "." + String.valueOf(object);
    }
  }

  private ObjectToStringValue.Result getArray(final Class<?> fieldClass,
                                              final Object object) {
    final Object[] array = (Object[]) object;

    final String value;
    if (array.length == 0) {
      value = EMPTY_ARRAY;
    } else {
      StringBuilder sb = new StringBuilder();
      for (final Object item : array) {
        if (sb.length() == 0) {
          sb.append(ARRAY_START);
        } else {
          sb.append(", ");
        }
        sb.append(constructName(item));
      }

      sb.append(ARRAY_END);
      value = sb.toString();
    }

    return new ObjectToStringValue.Result(fieldClass, false, value);
  }

  @Override
  public ObjectToStringValue.Result getValue(final Field field) throws IllegalAccessException {
    final Class<?> fieldClass = field.getType();
    final Object object = field.get(null);

    return getValueInternal(fieldClass, object);
  }

  @Override
  public ObjectToStringValue.Result getValue(final Object object) {
    return getValueInternal(object.getClass(), object);
  }

  public ObjectToStringValue.Result getValueInternal(final Class<?> clazz,
                                                     final Object object) {

    final ObjectToStringValue.Result retVal;

    if (object == null) {
      retVal = new ObjectToStringValue.Result(clazz, false, null);
    } else {
      if (clazz.isArray()) {
        retVal = getArray(clazz, object);
      } else {
        final String value = constructName(object);
        retVal = new ObjectToStringValue.Result(clazz, false, value);
      }
    }

    return retVal;
  }
}


