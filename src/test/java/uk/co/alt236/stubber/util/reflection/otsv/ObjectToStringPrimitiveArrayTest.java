package uk.co.alt236.stubber.util.reflection.otsv;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class ObjectToStringPrimitiveArrayTest {

  private static final Class<?> clazz = ObjectToStringValueTestData.class;
  private OtsvWrapper otsvWrapper;

  @Before
  public void setUp() {
    otsvWrapper = new OtsvWrapper(
        new ObjectToStringValue(),
        clazz);
  }

  @Test
  public void testBooleanArray() {
    final Class<?> expectedClass = boolean[].class;
    final String key1 = "BOOLEAN_ARRAY";
    final String value1 = "[true, false, true]";

    final String key2 = "BOOLEAN_ARRAY_NULL";
    final String value2 = null;

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testDoubleArray() {
    final Class<?> expectedClass = double[].class;
    final String key1 = "DOUBLE_ARRAY";
    final String value1 = "[1.0, 2.0, 3.0]";

    final String key2 = "DOUBLE_ARRAY_NULL";
    final String value2 = null;

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testFloatArray() {
    final Class<?> expectedClass = float[].class;
    final String key1 = "FLOAT_ARRAY";
    final String value1 = "[1.0, 2.0, 3.0]";

    final String key2 = "FLOAT_ARRAY_NULL";
    final String value2 = null;

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testIntArray() {
    final Class<?> expectedClass = int[].class;
    final String key1 = "INT_ARRAY";
    final String value1 = "[1, 2, 3]";

    final String key2 = "INT_ARRAY_NULL";
    final String value2 = null;

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testLongArray() {
    final Class<?> expectedClass = long[].class;
    final String key1 = "LONG_ARRAY";
    final String value1 = "[1, 2, 3]";

    final String key2 = "LONG_ARRAY_NULL";
    final String value2 = null;

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
