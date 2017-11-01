package uk.co.alt236.stubber.exporters.sections;

import uk.co.alt236.stubber.util.reflection.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*package*/ final class ModifierFormatter {

  private static final int ALLOWED_CLASS_MODS = Modifier.classModifiers();
  // Interfaces are technically abstract, but it is not syntactically valid
  private static final int
      ALLOWED_INTERFACE_MODS =
      Modifier.interfaceModifiers() & ~Modifier.ABSTRACT;

  private static final String STATIC = "static";
  private static final String FINAL = "final";
  private static final String ABSTRACT = "abstract";
  private static final String SYNCHRONIZED = "synchronized";
  private static final String TRANSIENT = "transient";
  private static final String VOLATILE = "volatile";

  private ModifierFormatter() {
    // NOOP
  }

  public static int clearBits(final int bitmask,
                              final int... bits) {
    int retVal = bitmask;

    for (final int bit : bits) {
      retVal = retVal & ~bit;
    }

    return retVal;
  }

  public static List<String> getModifiers(final Class<?> clazz) {
    final List<String> retVal;

    final int thisClassModifiers = clazz.getModifiers();

    if (clazz.isInterface()) {
      final int bitmask = clearBits(
          ALLOWED_INTERFACE_MODS,
          Modifier.FINAL,
          Modifier.ABSTRACT,
          Modifier.STATIC);
      retVal = getModifiers(thisClassModifiers & bitmask);
    } else if (clazz.isEnum()) {
      final int bitmask = clearBits(
          ALLOWED_CLASS_MODS,
          Modifier.FINAL,
          Modifier.ABSTRACT,
          Modifier.STATIC);

      retVal = getModifiers(thisClassModifiers & bitmask);
    } else {
      // Normal class
      retVal = getModifiers(thisClassModifiers & ALLOWED_CLASS_MODS);
    }

    return retVal;
  }

  public static List<String> getModifiers(final Field field) {
    return getModifiers(field.getModifiers() & Modifier.fieldModifiers());
  }

  public static List<String> getModifiers(final Method method) {
    return getModifiers(method.getModifiers() & Modifier.methodModifiers());
  }

  public static List<String> getModifiers(final Constructor<?> constructor) {
    return getModifiers(constructor.getModifiers() & Modifier.constructorModifiers());
  }

  private static List<String> getModifiers(final int modifiers) {
    final List<String> retVal = new ArrayList<>();

    final ReflectionUtils.Exposure exposure = ReflectionUtils.getExposure(modifiers);
    if (exposure == ReflectionUtils.Exposure.DEFAULT) {
      retVal.add("/* package */");
    } else {
      retVal.add(exposure.name().toLowerCase(Locale.US));
    }

    if (Modifier.isStatic(modifiers)) {
      retVal.add(STATIC);
    }

    if (Modifier.isFinal(modifiers)) {
      retVal.add(FINAL);
    }

    if (Modifier.isAbstract(modifiers)) {
      retVal.add(ABSTRACT);
    }

    if (Modifier.isSynchronized(modifiers)) {
      retVal.add(SYNCHRONIZED);
    }

    if (Modifier.isTransient(modifiers)) {
      retVal.add(TRANSIENT);
    }

    if (Modifier.isVolatile(modifiers)) {
      retVal.add(VOLATILE);
    }

    return retVal;
  }
}
