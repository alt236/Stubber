package uk.co.alt236.stubber.util.reflection.otsv;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class ObjectToStringPrimitiveValueTest {

  private static final Class<?> clazz = ObjectToStringValueTestData.class;
  private OtsvWrapper otsvWrapper;

  @Before
  public void setUp() {
    otsvWrapper = new OtsvWrapper(
        new ObjectToStringValue(),
        clazz);
  }

  @Test
  public void testBoolean() {
    final Class<?> expectedClass = boolean.class;
    final String key1 = "BOOLEAN_MAX";
    final String value1 = String.valueOf(Boolean.TRUE);

    final String key2 = "BOOLEAN_MIN";
    final String value2 = String.valueOf(Boolean.FALSE);

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testByte() {
    final Class<?> expectedClass = byte.class;
    final String key1 = "BYTE_MAX";
    final String value1 = String.valueOf(Byte.MAX_VALUE);

    final String key2 = "BYTE_MIN";
    final String value2 = String.valueOf(Byte.MIN_VALUE);

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testChar() {
    final Class<?> expectedClass = char.class;
    final String key1 = "CHAR_MAX";
    final String value1 = "'" + String.valueOf(Character.MAX_VALUE) + "'";

    final String key2 = "CHAR_MIN";
    final String value2 = "'" + String.valueOf(Character.MIN_VALUE) + "'";

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testDouble() {
    final Class<?> expectedClass = double.class;
    final String key1 = "DOUBLE_MAX";
    final String value1 = String.valueOf(Double.MAX_VALUE) + "d";

    final String key2 = "DOUBLE_MIN";
    final String value2 = String.valueOf(Double.MIN_VALUE) + "d";

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testFloat() {
    final Class<?> expectedClass = float.class;
    final String key1 = "FLOAT_MAX";
    final String value1 = String.valueOf(Float.MAX_VALUE) + "f";

    final String key2 = "FLOAT_MIN";
    final String value2 = String.valueOf(Float.MIN_VALUE) + "f";

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testInt() {
    final Class<?> expectedClass = int.class;
    final String key1 = "INT_MAX";
    final String value1 = String.valueOf(Integer.MAX_VALUE);

    final String key2 = "INT_MIN";
    final String value2 = String.valueOf(Integer.MIN_VALUE);

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testLong() {
    final Class<?> expectedClass = long.class;
    final String key1 = "LONG_MAX";
    final String value1 = String.valueOf(Long.MAX_VALUE) + "l";

    final String key2 = "LONG_MIN";
    final String value2 = String.valueOf(Long.MIN_VALUE) + "l";

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testShort() {
    final Class<?> expectedClass = short.class;
    final String key1 = "SHORT_MAX";
    final String value1 = String.valueOf(Short.MAX_VALUE);

    final String key2 = "SHORT_MIN";
    final String value2 = String.valueOf(Short.MIN_VALUE);

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  private void validate(final String key,
                        final String expectedValue,
                        final Class<?> expectedClass) {

    final Field field = otsvWrapper.getField(key);
    final ObjectToStringValue.Result result = otsvWrapper.getValue(field);
    assertEquals(expectedValue, result.getValue());
    assertEquals(false, result.isError());
    assertEquals(expectedClass, result.getClazz());
  }
}