package uk.co.alt236.stubber.util.reflection.otsv;

/*package*/ final class Helper {

  public static ObjectToStringValue.Result getNullResult(final Class<?> clazz) {
    return new ObjectToStringValue.Result(clazz, false, null);
  }
}
