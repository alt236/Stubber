package uk.co.alt236.stubber.util.reflection.otsv;

import java.lang.reflect.Field;

public class ObjectToStringValue implements ValueGetter {

  private final PrimitiveValue primitiveValue;
  private final ObjectValue objectValue;
  private final PrimitiveArrayValue primitiveArrayValue;
  private final EnumValue enumValue;

  public ObjectToStringValue() {
    enumValue = new EnumValue();
    primitiveValue = new PrimitiveValue();
    objectValue = new ObjectValue();
    primitiveArrayValue = new PrimitiveArrayValue();
  }

  public Result getValue(final Object object) {
    if (object == null) {
      return Helper.getNullResult(null);
    } else {
      final Class<?> clazz = object.getClass();
      final Result retVal;
      if (clazz.isEnum() || Enum[].class.isAssignableFrom(clazz)) {
        retVal = enumValue.getValue(object);
      } else {
        if (clazz.isPrimitive()) {
          retVal = primitiveValue.getValue(object);
        } else if (clazz.isArray()) {
          retVal = primitiveArrayValue.getValue(object);
        } else {
          retVal = objectValue.getValue(object);
        }
      }

      return retVal;
    }
  }

  public Result getValue(final Field f) throws IllegalAccessException {
    final Class<?> clazz = f.getType();

    final Result retVal;
    if (clazz.isEnum() || Enum[].class.isAssignableFrom(clazz)) {
      retVal = enumValue.getValue(f);
    } else {
      if (clazz.isPrimitive()) {
        retVal = primitiveValue.getValue(f);
      } else if (clazz.isArray()) {
        retVal = primitiveArrayValue.getValue(f);
      } else {
        retVal = objectValue.getValue(f);
      }
    }

    return retVal;
  }

  public static class Result {

    private final boolean error;
    private final String value;
    private final Class<?> clazz;

    protected Result(final Class<?> clazz,
                     final boolean error,
                     final String value) {
      this.error = error;
      this.value = value;
      this.clazz = clazz;
    }

    public Class<?> getClazz() {
      return clazz;
    }

    public String getValue() {
      return value;
    }

    public boolean isError() {
      return error;
    }
  }
}
