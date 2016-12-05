package uk.co.alt236.stubber.exporters.sections;

import uk.co.alt236.stubber.exporters.CommonFilter;
import uk.co.alt236.stubber.util.reflection.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/*package*/ class ConstructorFormatter implements Formatter<Constructor[]> {

  private List<Constructor> filter(final Constructor[] constructors) {
    final List<Constructor> retVal = new ArrayList<>();

    retVal.addAll(CommonFilter.filter(constructors));
    return retVal;
  }

  public String format(final Constructor[] constructors) {
    final StringBuilder sb = new StringBuilder();

    boolean wroteSomething = false;
    for (final Constructor<?> constructor : filter(constructors)) {
      final List<String> modifiers =
          ModifierFormatter.getModifiers(constructor);
      wroteSomething = true;

      sb.append('\t');
      for (final String mod : modifiers) {
        sb.append(mod);
        sb.append(' ');
      }

      sb.append(constructor.getDeclaringClass().getSimpleName());

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
