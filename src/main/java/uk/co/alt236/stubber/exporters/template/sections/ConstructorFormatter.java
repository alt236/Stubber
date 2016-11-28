package uk.co.alt236.stubber.exporters.template.sections;

import uk.co.alt236.stubber.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.List;

public class ConstructorFormatter {

  public String getConstructors(final Class<?> clazz) {
    final StringBuilder sb = new StringBuilder();

    boolean wroteSomething = false;
    for (final Constructor<?> constructor : clazz.getConstructors()) {
      final List<String> modifiers =
          ModifierFormatter.getModifiers(constructor.getModifiers());
      wroteSomething = true;

      sb.append('\t');
      for (final String mod : modifiers) {
        sb.append(mod);
        sb.append(' ');
      }

      sb.append(constructor.getName());

      sb.append('(');

      if (constructor.getParameterTypes().length > 0) {
        for (int i = 0; i < constructor.getParameterTypes().length; i++) {
          if (i > 0) {
            sb.append(", ");
          }
          sb.append(ReflectionUtils.getSaneType(constructor.getParameterTypes()[i]));
          sb.append(' ');
          sb.append("param");
          sb.append(i);
        }
      }

      sb.append(')');

      sb.append('{');
      sb.append('}');
      sb.append('\n');
      sb.append('\n');
    }

    if (!wroteSomething) {
      sb.append("\t// NO VISIBLE CONSTRUCTORS!");
    }
    return sb.toString();
  }
}
