package uk.co.alt236.stubber.exporters.v2.template.sections;

import uk.co.alt236.stubber.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*package*/ final class ModifierFormatter {

  private ModifierFormatter() {
    // NOOP
  }

  public static List<String> getModifiers(final Field field) {
    return getModifiers(field.getModifiers());
  }

  public static List<String> getModifiers(final Class<?> clazz) {
    return getModifiers(clazz.getModifiers());
  }

  public static List<String> getModifiers(final Method method) {
    return getModifiers(method.getModifiers());
  }

  public static List<String> getModifiers(final int modifiers) {
    final List<String> retVal = new ArrayList<>();

    final ReflectionUtils.Exposure exposure = ReflectionUtils.getExposure(modifiers);
    if (exposure != ReflectionUtils.Exposure.DEFAULT) {
      retVal.add(exposure.name().toLowerCase(Locale.US));
    }

    if (Modifier.isFinal(modifiers)) {
      retVal.add("final");
    }

    if (Modifier.isAbstract(modifiers)) {
      retVal.add("abstract");
    }

    if (Modifier.isStatic(modifiers)) {
      retVal.add("static");
    }

    if (Modifier.isSynchronized(modifiers)) {
      retVal.add("synchronized");
    }

    if (Modifier.isTransient(modifiers)) {
      retVal.add("transient");
    }

    if (Modifier.isVolatile(modifiers)) {
      retVal.add("volatile");
    }

    return retVal;
  }
}
