package uk.co.alt236.stubber.exporters.sections;

import uk.co.alt236.stubber.util.reflection.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FormatterFactory {

  public FormatterFactory() {

  }

  public Formatter<Constructor[]> getConstructorFormatter() {
    return new ConstructorFormatter();
  }

  public EnumFormatter getEnumFormatter(Class<?> clazz) {
    return new EnumFormatter(clazz);
  }

  public Formatter<Field[]> getFieldFormatter(Class<?> clazz) {
    return new FieldFormatter();
  }

  public Formatter<Class<?>> getFormatter(Class<?> clazz) {
    return new ClassFormatter();
  }

  public Formatter<Method[]> getMethodFormatter(Class<?> clazz,
                                                boolean blowOnReturn) {
    return new MethodFormatter(ReflectionUtils.getClassType(clazz), blowOnReturn);
  }
}
