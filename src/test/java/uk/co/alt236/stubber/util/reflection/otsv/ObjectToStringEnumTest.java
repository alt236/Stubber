package uk.co.alt236.stubber.util.reflection.otsv;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class ObjectToStringEnumTest {

  private static final Class<?> clazz = ObjectToStringValueTestData.class;
  private OtsvWrapper otsvWrapper;

  @Before
  public void setUp() {
    otsvWrapper = new OtsvWrapper(
        new ObjectToStringValue(),
        clazz);
  }

  @Test
  public void testEnum() {
    final Class<?> expectedClass = ObjectToStringValueTestData.TestEnum.class;
    final String key1 = "ENUM";
    final String value1 = ObjectToStringValueTestData.TestEnum.class.getCanonicalName() + ".TEST_2";

    final String key2 = "ENUM_NULL";
    final String value2 = null;

    validate(key1, value1, expectedClass);
    validate(key2, value2, expectedClass);
  }

  @Test
  public void testEnumArrayEmpty() {
    final Class<?> expectedClass = ObjectToStringValueTestData.TestEnum[].class;
    final String key1 = "ENUM_ARRAY_EMPTY";
    final String value1 = "{}";

    validate(key1, value1, expectedClass);
  }

  @Test
  public void testEnumArrayNull() {
    final Class<?> expectedClass = ObjectToStringValueTestData.TestEnum[].class;
    final String key1 = "ENUM_ARRAY_NULL";
    final String value1 = null;

    validate(key1, value1, expectedClass);
  }

  @Test
  public void testEnumArrayValid() {
    final Class<?> expectedClass = ObjectToStringValueTestData.TestEnum[].class;
    final String key1 = "ENUM_ARRAY";
    final String value1 = "{"
                          + ObjectToStringValueTestData.TestEnum.class.getCanonicalName()
                          + ".TEST_1"
                          + ", "
                          + ObjectToStringValueTestData.TestEnum.class.getCanonicalName()
                          + ".TEST_2"
                          + "}";

    validate(key1, value1, expectedClass);
  }

  @Test
  public void testEnumArrayWithNulls() {
    final Class<?> expectedClass = ObjectToStringValueTestData.TestEnum[].class;
    final String key1 = "ENUM_ARRAY_WITH_NULLS";
    final String value1 = "{"
                          + ObjectToStringValueTestData.TestEnum.class.getCanonicalName()
                          + ".TEST_1"
                          + ", null, "
                          + ObjectToStringValueTestData.TestEnum.class.getCanonicalName()
                          + ".TEST_2"
                          + ", null}";

    validate(key1, value1, expectedClass);
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
