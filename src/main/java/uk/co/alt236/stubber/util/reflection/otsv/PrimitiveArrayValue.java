package uk.co.alt236.stubber.util.reflection.otsv;

import java.lang.reflect.Field;
import java.util.Arrays;

/*package*/ class PrimitiveArrayValue implements ValueGetter {

  @Override
  public ObjectToStringValue.Result getValue(final Field field) throws IllegalAccessException {
    final Class<?> objectClass = field.getType();
    final Object object = field.get(null);

    return getValueInteral(objectClass, object);
  }

  @Override
  public ObjectToStringValue.Result getValue(final Object object) {
    return getValueInteral(object.getClass(), object);
  }

  private ObjectToStringValue.Result getValueInteral(final Class<?> objectClass,
                                                     final Object object) {
    final Class<?> contentsClass = objectClass.getComponentType();
    final String value;
    boolean error = false;

    if (object == null) {
      value = null;
    } else {
      if (contentsClass == int.class) {
        value = Arrays.toString((int[]) object);
      } else if (contentsClass == double.class) {
        value = Arrays.toString((double[]) object);
      } else if (contentsClass == boolean.class) {
        value = Arrays.toString((boolean[]) object);
      } else if (contentsClass == float.class) {
        value = Arrays.toString((float[]) object);
      } else if (contentsClass == short.class) {
        value = Arrays.toString((short[]) object);
      } else if (contentsClass == byte.class) {
        value = Arrays.toString((byte[]) object);
      } else if (contentsClass == long.class) {
        value = Arrays.toString((long[]) object);
      } else if (contentsClass == char.class) {
        value = Arrays.toString((char[]) object);
      } else {
        error = true;
        value = null;
      }
    }

    return new ObjectToStringValue.Result(objectClass, error, value);
  }
}
