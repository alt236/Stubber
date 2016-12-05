package uk.co.alt236.stubber.util.reflection.otsv;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class ObjectToStringObjectTest {

  private static final Class<?> clazz = ObjectToStringValueTestData.class;
  private OtsvWrapper otsvWrapper;

  @Before
  public void setUp() {
    otsvWrapper = new OtsvWrapper(
        new ObjectToStringValue(),
        clazz);
  }

  @Test
  public void testString() {
    final Class<?> expectedClass = String.class;
    final String key1 = "STRING";
    final String value1 = "\"FOO\"";

    final String key2 = "STRING_NULL";
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
