package uk.co.alt236.stubber.util.reflection.otsv;

import java.lang.reflect.Field;

/*package*/ interface ValueGetter {

  ObjectToStringValue.Result getValue(Field field) throws IllegalAccessException;

  ObjectToStringValue.Result getValue(Object object);
}
