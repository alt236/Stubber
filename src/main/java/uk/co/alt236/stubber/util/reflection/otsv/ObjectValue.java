package uk.co.alt236.stubber.util.reflection.otsv;

import org.apache.commons.lang3.StringEscapeUtils;

import java.lang.reflect.Field;

/*package*/ class ObjectValue implements ValueGetter {

  private static String quote(final String string) {
    return "\"" + string + "\"";
  }

  public ObjectToStringValue.Result getValue(final Field field) throws IllegalAccessException {
    final Class<?> fieldClass = field.getType();
    final Object object = field.get(null);

    return getValueInternal(fieldClass, object);
  }

  @Override
  public ObjectToStringValue.Result getValue(final Object object) {
    return getValueInternal(object.getClass(), object);
  }

  private ObjectToStringValue.Result getValueInternal(final Class<?> clazz,
                                                      final Object object) {
    final String value;
    final boolean error;

    if (clazz == String.class) {
      error = false;
      final String string = (String) object;
      value = string == null ? null : quote(StringEscapeUtils.escapeJava(string));
    } else {
      error = true;
      value = null;
    }

    return new ObjectToStringValue.Result(clazz, error, value);
  }
}


