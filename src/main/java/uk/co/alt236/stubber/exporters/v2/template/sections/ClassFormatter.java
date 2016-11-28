package uk.co.alt236.stubber.exporters.v2.template.sections;

import uk.co.alt236.stubber.util.ReflectionUtils;

import java.util.List;
import java.util.Locale;

public class ClassFormatter {

  public String getClassDefinition(final Class<?> clazz) {
    final ReflectionUtils.ClassType type = ReflectionUtils.getClassType(clazz);
    final Class<?> superClass = clazz.getSuperclass();
    final Class<?>[] interfaces = clazz.getInterfaces();
    final List<String> modifiers = ModifierFormatter.getModifiers(clazz);
    final StringBuilder sb = new StringBuilder();

    for (final String mod : modifiers) {
      sb.append(mod);
      sb.append(' ');
    }

    sb.append(type.name().toLowerCase(Locale.US));
    sb.append(' ');
    sb.append(clazz.getSimpleName());

    if (superClass != null) {
      sb.append(" extends ");
      sb.append(superClass.getCanonicalName());
    }

    if (interfaces != null && interfaces.length > 1) {
      sb.append(" implements ");
      boolean firstRun = true;

      for (final Class<?> iFace : interfaces) {
        if (!firstRun) {
          sb.append(", ");
          firstRun = false;
        }

        sb.append(iFace.getCanonicalName());
      }
    }

    return sb.toString().trim();
  }
}
