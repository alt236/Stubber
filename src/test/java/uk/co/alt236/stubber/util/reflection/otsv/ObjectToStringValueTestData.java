package uk.co.alt236.stubber.util.reflection.otsv;

/*package*/ class ObjectToStringValueTestData {

  public static final int INT_MAX = Integer.MAX_VALUE;
  public static final int INT_MIN = Integer.MIN_VALUE;
  public static final long LONG_MAX = Long.MAX_VALUE;
  public static final long LONG_MIN = Long.MIN_VALUE;
  public static final double DOUBLE_MAX = Double.MAX_VALUE;
  public static final double DOUBLE_MIN = Double.MIN_VALUE;
  public static final float FLOAT_MAX = Float.MAX_VALUE;
  public static final float FLOAT_MIN = Float.MIN_VALUE;
  public static final boolean BOOLEAN_MAX = true;
  public static final boolean BOOLEAN_MIN = false;
  public static final char CHAR_MAX = Character.MAX_VALUE;
  public static final char CHAR_MIN = Character.MIN_VALUE;
  public static final byte BYTE_MAX = Byte.MAX_VALUE;
  public static final byte BYTE_MIN = Byte.MIN_VALUE;
  public static final short SHORT_MAX = Short.MAX_VALUE;
  public static final short SHORT_MIN = Short.MIN_VALUE;

  public static final String STRING = "FOO";
  public static final String STRING_NULL = null;

  // ARRAYS

  public static final int[] INT_ARRAY = {1, 2, 3};
  public static final int[] INT_ARRAY_NULL = null;

  public static final long[] LONG_ARRAY = {1, 2, 3};
  public static final long[] LONG_ARRAY_NULL = null;

  public static final double[] DOUBLE_ARRAY = {1.0d, 2.0d, 3.0d};
  public static final double[] DOUBLE_ARRAY_NULL = null;

  public static final float[] FLOAT_ARRAY = {1.0f, 2.0f, 3.0f};
  public static final float[] FLOAT_ARRAY_NULL = null;

  public static final boolean[] BOOLEAN_ARRAY = {true, false, true};
  public static final boolean[] BOOLEAN_ARRAY_NULL = null;

  // ENUMS
  public static final TestEnum ENUM = TestEnum.TEST_2;
  public static final TestEnum ENUM_NULL = null;

  public static final TestEnum[] ENUM_ARRAY = {TestEnum.TEST_1, TestEnum.TEST_2};
  public static final TestEnum[]
      ENUM_ARRAY_WITH_NULLS =
      {TestEnum.TEST_1, null, TestEnum.TEST_2, null};
  public static final TestEnum[] ENUM_ARRAY_EMPTY = {};
  public static final TestEnum[] ENUM_ARRAY_NULL = null;

  protected enum TestEnum {
    TEST_1,
    TEST_2
  }
}
