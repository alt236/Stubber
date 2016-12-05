package uk.co.alt236.stubber.util.reflection.otsv;

import java.lang.reflect.Field;

/*package*/ class OtsvWrapper {

  private final Class<?> clazz;
  private final ObjectToStringValue objectToStringValue;

  public OtsvWrapper(final ObjectToStringValue objectToStringValue,
                     final Class<?> clazz) {
    this.clazz = clazz;
    this.objectToStringValue = objectToStringValue;
  }

  public Field getField(final String name) {
    try {
      return clazz.getField(name);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public ObjectToStringValue.Result getValue(final Field field) {
    try {
      return objectToStringValue.getValue(field);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

}
