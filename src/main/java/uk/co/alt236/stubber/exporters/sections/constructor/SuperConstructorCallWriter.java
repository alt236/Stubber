package uk.co.alt236.stubber.exporters.sections.constructor;

import uk.co.alt236.stubber.util.reflection.DefaultReturnValues;
import uk.co.alt236.stubber.util.reflection.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class SuperConstructorCallWriter {

  private final String indent;

  public SuperConstructorCallWriter(final String indent) {
    this.indent = indent;
  }

  private boolean allArePrimitives(final Class[] parameterTypes) {
    for (final Class clazz : parameterTypes) {
      if (!clazz.isPrimitive()) {
        return false;
      }
    }

    return true;
  }

  private Constructor getSimplestConstructor(final Constructor[] superConstructors) {
    final List<Constructor> constructorList = Arrays.asList(superConstructors);
    constructorList.sort(Comparator.comparingInt(Constructor::getParameterCount));

    for (final Constructor constructor : superConstructors) {
      if (constructor.getParameterCount() == 1) {
        return constructor;
      } else if (allArePrimitives(constructor.getParameterTypes())) {
        return constructor;
      }
    }

    return constructorList.get(0);
  }

  public boolean requiresSuperCall(final Class clazz) {
    final boolean retVal;

    if (clazz.isEnum()) {
      retVal = false;
    } else {
      final Class superClass = clazz.getSuperclass();
      retVal = superClass != null
               && superClass != java.lang.Object.class
               && superClass.getDeclaredConstructors() != null
               && superClass.getDeclaredConstructors().length > 0;
    }

    return retVal;
  }

  public void writeSuperConstructorCall(final StringBuilder sb, final Class clazz) {
    final Class superClass = clazz.getSuperclass();

    if (requiresSuperCall(clazz)) {
      final Constructor[] superConstructors = superClass.getDeclaredConstructors();
      final Constructor superConstructor = getSimplestConstructor(superConstructors);
      sb.append("\n");
      sb.append(indent);
      sb.append(indent);
      sb.append("super(");

      for (int i = 0; i < superConstructor.getParameterTypes().length; i++) {
        final Class parameter = superConstructor.getParameterTypes()[i];
        final String value = DefaultReturnValues.forClass(parameter);

        if (i > 0) {
          sb.append(", ");
        }

        if ("null".equals(value)) {
          final String dataType = ReflectionUtils.getSaneType(parameter);
          sb.append('(');
          sb.append(dataType);
          sb.append(") ");
        }

        sb.append(value);
      }

      sb.append(");");
      sb.append("\n");
      sb.append(indent);
    }
  }
}
