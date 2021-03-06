package uk.co.alt236.stubber.exporters.sections;

import uk.co.alt236.stubber.exporters.CommonFilter;
import uk.co.alt236.stubber.util.reflection.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/*package*/ class ConstructorFormatter implements Formatter<Constructor[]> {

  private static final String INDENT = "\t";
  private final SuperConstructorCallWriter superConstructorCallWriter;

  public ConstructorFormatter() {
    superConstructorCallWriter = new SuperConstructorCallWriter(INDENT);
  }

  private List<Constructor> filter(final Constructor[] constructors) {
    final List<Constructor> retVal = new ArrayList<>();

    retVal.addAll(CommonFilter.filter(constructors));
    return retVal;
  }

  @Override
  public String format(final Class declaringClass,
                       final Constructor[] constructors) {
    final StringBuilder sb = new StringBuilder();

    final boolean wroteSomething;

    if (!declaringClass.isEnum() && constructors.length > 0) {
      wroteSomething = writeClassConstructors(sb, constructors);
    } else {
      wroteSomething = writeRequiredSuperConstructorCall(sb, declaringClass);
    }

    if (!wroteSomething) {
      sb.append("\t// NO VISIBLE CONSTRUCTORS!");
    }
    return sb.toString();
  }

  private boolean writeClassConstructors(final StringBuilder sb,
                                         final Constructor[] constructors) {
    boolean wroteSomething = false;
    for (final Constructor<?> constructor : filter(constructors)) {
      wroteSomething = true;

      final List<String> modifiers = ModifierFormatter.getModifiers(constructor);
      final Class declaringClass = constructor.getDeclaringClass();
      sb.append(INDENT);
      for (final String mod : modifiers) {
        sb.append(mod);
        sb.append(' ');
      }

      sb.append(declaringClass.getSimpleName());

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

      sb.append(") {");
      superConstructorCallWriter.writeSuperConstructorCall(sb, declaringClass);
      sb.append('}');
      sb.append('\n');
      sb.append('\n');
    }

    return wroteSomething;
  }

  private boolean writeRequiredSuperConstructorCall(final StringBuilder sb,
                                                    final Class declaringClass) {

    boolean wroteSomething = false;
    if (superConstructorCallWriter.requiresSuperCall(declaringClass)) {
      wroteSomething = true;
      sb.append(INDENT);
      sb.append("public");
      sb.append(' ');
      sb.append(declaringClass.getSimpleName());
      sb.append("() {");
      superConstructorCallWriter.writeSuperConstructorCall(sb, declaringClass.getSuperclass());
      sb.append('}');
      sb.append('\n');
      sb.append('\n');
    }
    return wroteSomething;
  }
}
