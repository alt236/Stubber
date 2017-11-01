package uk.co.alt236.stubber.exporters.sections;

import uk.co.alt236.stubber.exporters.CommonFilter;
import uk.co.alt236.stubber.util.reflection.DefaultReturnValues;
import uk.co.alt236.stubber.util.reflection.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/*package*/ class MethodFormatter implements Formatter<Method[]> {

  private final boolean blowOnReturn;
  private final ReflectionUtils.ClassType classType;

  public MethodFormatter(final ReflectionUtils.ClassType classType,
                         final boolean blowOnReturn) {
    this.blowOnReturn = blowOnReturn;
    this.classType = classType;
  }

  @Override
  public String format(final Class declaringClass,
                       final Method[] methods) {
    final StringBuilder sb = new StringBuilder();

    boolean wroteSomething = false;
    for (final Method method : getFilteredMethods(methods)) {
        final List<String> modifiers = ModifierFormatter.getModifiers(method);
        final boolean isAbstract = Modifier.isAbstract(method.getModifiers());

        wroteSomething = true;

        sb.append('\t');
        for (final String mod : modifiers) {
          sb.append(mod);
          sb.append(' ');
        }

        sb.append(ReflectionUtils.getSaneType(method.getReturnType()));
        sb.append(' ');
        sb.append(method.getName());

        sb.append('(');

        if (method.getParameterTypes().length > 0) {
          for (int i = 0; i < method.getParameterTypes().length; i++) {
            if (i > 0) {
              sb.append(", ");
            }
            sb.append(ReflectionUtils.getSaneType(method.getParameterTypes()[i]));
            sb.append(' ');
            sb.append("param");
            sb.append(i);
          }
        }

        sb.append(')');

      if (classType == ReflectionUtils.ClassType.INTERFACE || isAbstract) {
          sb.append(';');
          sb.append('\n');
          sb.append('\n');
        } else {
          sb.append('{');
          final String returnStatement = getMethodReturnStatement(method.getReturnType());

          if (returnStatement.length() > 0) {
            sb.append('\n');
            sb.append("\t\t");
            sb.append(returnStatement);
            sb.append('\n');
            sb.append('\t');
          }

          sb.append('}');
          sb.append('\n');
          sb.append('\n');
        }
    }

    if (!wroteSomething) {
      sb.append("\t// NO VISIBLE METHODS!");
    }
    return sb.toString();
  }

  // TODO: Add support for default methods
  private List<Method> getFilteredMethods(final Method[] methods) {
    final List<Method> retVal = new ArrayList<>();

    for (final Method method : CommonFilter.filter(methods)) {
      final boolean validType = !method.isBridge();
      final boolean add;

      if (classType == ReflectionUtils.ClassType.ENUM) {
        switch (method.getName()) {
          case "values":
            add = false;
            break;
          case "valueOf":
            add = false;
            break;
          default:
            add = true;
            break;
        }
      } else {
        add = true;
      }

      if (validType && add) {
        retVal.add(method);
      }
    }

    return retVal;
  }

  private String getMethodReturnStatement(final Class<?> fieldClass) {
    final String methodResult;

    if (blowOnReturn) {
      methodResult = "throw new UnsupportedOperationException(\"Stub!\");";
    } else {
      if (fieldClass.equals(Void.TYPE)) {
        methodResult = "";
      } else {
        final String value = DefaultReturnValues.forClass(fieldClass);
        methodResult = "return " + value + ";";
      }
    }
    return methodResult;
  }
}
