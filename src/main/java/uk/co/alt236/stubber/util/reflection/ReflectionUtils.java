package uk.co.alt236.stubber.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectionUtils {

  private static final String PACKAGE_SEPARATOR = ".";

  private ReflectionUtils() {
  }

  public static ClassType getClassType(final Class<?> clazz) {
    if (clazz.isEnum()) {
      return ClassType.ENUM;
    } else if (clazz.isAnnotation()) {
      return ClassType.ANNOTATION;
    } else if (Modifier.isInterface(clazz.getModifiers())) {
      return ClassType.INTERFACE;
    } else {
      return ClassType.CLASS;
    }
  }

  public static Exposure getExposure(final int modifiers) {
    if (Modifier.isPrivate(modifiers)) {
      return Exposure.PRIVATE;
    } else if (Modifier.isPublic(modifiers)) {
      return Exposure.PUBLIC;
    } else if (Modifier.isProtected(modifiers)) {
      return Exposure.PROTECTED;
    } else {
      return Exposure.DEFAULT;
    }
  }

  public static Exposure getExposure(final Member member) {
    return getExposure(member.getModifiers());
  }

  public static String getPackageName(final Class<?> clazz) {
    if (clazz == null) {
      throw new IllegalStateException("Class is null!");
    }

    final String className = clazz.getName();

    if (className == null || className.length() < 1) {
      throw new IllegalStateException("Class name is empty/null!");
    }

    final int index = className.lastIndexOf(PACKAGE_SEPARATOR);
    if (index != -1) {
      return className.substring(0, index);
    } else {
      throw new IllegalStateException("Could not find a '.' !");
    }
  }

  public static Object getReturnValue(final Object object,
                                      final Method method,
                                      final Object[] params) {
    try {
      return method.invoke(object, params);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      System.err.println("Error when invoking method='" + method + "' of '" + object + "'");
      throw e;
    }

    return null;
  }

  public static Object getReturnValue(final Class<?> clazz,
                                      final Method method,
                                      final Object[] params) {
    try {
      return method.invoke(clazz, params);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      System.err.println("Error when invoking method='" + method + "' of '" + clazz + "'");
      throw e;
    }

    return null;
  }

  public static String getSaneType(final Class<?> clazz) {

    final String retVal;
    if (clazz.isArray()) {
      retVal = clazz.getComponentType().getCanonicalName() + "[]";
    } else {
      retVal = clazz.getName();
    }

    return retVal.replaceAll("\\$", "\\.");
  }

  public static String getSaneType(final Field field) {
    return getSaneType(field.getType());
  }

  public enum ClassType {
    CLASS("class"),
    ENUM("enum"),
    INTERFACE("interface"),
    ANNOTATION("@interface");

    private final String syntacticalName;

    ClassType(final String name) {
      syntacticalName = name;
    }

    public String getSyntacticalName() {
      return syntacticalName;
    }
  }

  public enum Exposure {
    DEFAULT,
    PRIVATE,
    PROTECTED,
    PUBLIC
  }
}
