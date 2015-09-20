package uk.co.alt236.stubber.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public final class ReflectionUtils {

  private static final String PACKAGE_SEPARATOR = ".";

  private ReflectionUtils() {
  }

  public static ClassType getClassType(final Class<?> clazz) {
    if (Modifier.isInterface(clazz.getModifiers())) {
      return ClassType.INTERFACE;
    } else {
      return ClassType.CLASS;
    }
  }

  public static Exposure getExposure(final Class<?> clazz) {
    return getExposure(clazz.getModifiers());
  }

  private static Exposure getExposure(final int modifiers) {
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

  public static String getSaneType(final Class<?> clazz) {
    if (clazz.isArray()) {
      return clazz.getCanonicalName();
    } else {
      return clazz.getCanonicalName();
    }
  }

  public static String getSaneType(final Field field) {
    return getSaneType(field.getType());
  }

  public enum ClassType {
    CLASS,
    INTERFACE
  }

  public enum Exposure {
    DEFAULT,
    PRIVATE,
    PROTECTED,
    PUBLIC
  }
}
