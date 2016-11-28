package uk.co.alt236.stubber.exporters.v2.template.sections;

import uk.co.alt236.stubber.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class MethodFormatter {

  private final boolean blowOnReturn;

  public MethodFormatter(final boolean blowOnReturn) {
    this.blowOnReturn = blowOnReturn;
  }

  private String getMethodReturnStatement(final Class<?> fieldClass) {
    final String value;
    final String methodResult;

    if (blowOnReturn) {
      methodResult = "throw new UnsupportedOperationException(\"Stub!\");";
    } else {
      if (fieldClass.equals(Void.TYPE)) {
        methodResult = "";
      } else {
        if (fieldClass == int.class) {
          value = "0";
        } else if (fieldClass == double.class) {
          value = "0.0";
        } else if (fieldClass == boolean.class) {
          value = "false";
        } else if (fieldClass == float.class) {
          value = "0.0";
        } else if (fieldClass == String.class) {
          value = "null";
        } else if (fieldClass == short.class) {
          value = "0";
        } else if (fieldClass == byte.class) {
          value = "0";
        } else if (fieldClass == long.class) {
          value = "0";
        } else if (fieldClass == char.class) {
          value = "0";
        } else {
          value = "null";
        }
        methodResult = "return " + value + ";";
      }
    }
    return methodResult;
  }

  public String getMethods(final Class<?> clazz) {
    final StringBuilder sb = new StringBuilder();
    final boolean
        isInterface =
        ReflectionUtils.getClassType(clazz) == ReflectionUtils.ClassType.INTERFACE;

    boolean wroteSomething = false;
    for (final Method method : clazz.getMethods()) {
      final ReflectionUtils.Exposure exposure = ReflectionUtils.getExposure(method);
      if (exposure == ReflectionUtils.Exposure.PUBLIC) {
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

        if (isInterface || isAbstract) {
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
    }

    if (!wroteSomething) {
      sb.append("\t// NO VISIBLE METHODS!");
    }
    return sb.toString();
  }
}
