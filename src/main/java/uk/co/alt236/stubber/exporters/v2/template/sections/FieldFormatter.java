package uk.co.alt236.stubber.exporters.v2.template.sections;

import uk.co.alt236.stubber.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class FieldFormatter {

  public String getFieldDefinition(final Class<?> clazz) {
    final StringBuilder sb = new StringBuilder();

    boolean wroteSomething = false;
    for (final Field field : clazz.getFields()) {
      final ReflectionUtils.Exposure exposure = ReflectionUtils.getExposure(field);
      if (exposure == ReflectionUtils.Exposure.PUBLIC) {
        //We only care for static final values
        if (isStaticFinal(field)) {
          wroteSomething = true;
          sb.append('\t');

          final List<String> modifiers = ModifierFormatter.getModifiers(field);

          for (final String mod : modifiers) {
            sb.append(mod);
            sb.append(' ');
          }

          final Class<?> fieldClass = field.getType();

          sb.append(ReflectionUtils.getSaneType(field));
          sb.append(' ');
          sb.append(field.getName());

          final boolean isAccessible = field.isAccessible();

          if (!isAccessible) {
            field.setAccessible(true);
          }

          try {
            final String value;
            if (fieldClass.isEnum()) {
              final Object tmp = field.get(null);

              if (tmp == null) {
                value = "null";
              } else {
                value = fieldClass.getCanonicalName() + "." + String.valueOf(tmp);
              }
            } else {
              value = getValue(field);
            }

            System.out.println("\t\tField: '" + field + "'= '" + value + "'");

            sb.append(" = ");
            sb.append(value);
          } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
          }

          sb.append(';');
          sb.append('\n');
        }
      }
    }

    if (!wroteSomething) {
      sb.append("\t// NO VISIBLE FIELDS!");
    }

    return sb.toString();
  }

  private String getValue(final Field f) throws IllegalAccessException {
    final Class<?> fieldClass = f.getType();

    final String retVal;

    if (fieldClass == int.class) {
      retVal = String.valueOf(f.getInt(null));
    } else if (fieldClass == double.class) {
      retVal = String.valueOf(f.getDouble(null));
    } else if (fieldClass == boolean.class) {
      retVal = String.valueOf(f.getBoolean(null));
    } else if (fieldClass == float.class) {
      retVal = String.valueOf(f.getFloat(null));
    } else if (fieldClass == String.class) {
      final String tmp = (String) f.get(null);
      retVal = tmp == null ? "null" : "\"" + f.get(null).toString() + "\"";
    } else if (fieldClass == short.class) {
      retVal = String.valueOf(f.getShort(null));
    } else if (fieldClass == byte.class) {
      retVal = String.valueOf(f.getByte(null));
    } else if (fieldClass == long.class) {
      retVal = String.valueOf(f.getLong(null));
    } else if (fieldClass == char.class) {
      retVal = String.valueOf(f.getChar(null));
    } else {
      retVal = "null; // ** FAILED TO DEDUCE RETURN VALUE **";
      System.err.println(
          "\t\tField: No idea what to set as value for '" + f + "' with value '" + f
              .get(null) + "'");
    }

    return retVal;
  }

  private boolean isStaticFinal(final Field field) {
    final int mod = field.getModifiers();

    return Modifier.isStatic(mod) && Modifier.isFinal(mod);
  }
}
