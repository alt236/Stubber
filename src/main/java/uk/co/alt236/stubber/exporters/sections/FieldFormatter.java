package uk.co.alt236.stubber.exporters.sections;

import uk.co.alt236.stubber.exporters.CommonFilter;
import uk.co.alt236.stubber.util.Log;
import uk.co.alt236.stubber.util.reflection.ReflectionUtils;
import uk.co.alt236.stubber.util.reflection.otsv.ObjectToStringValue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

/*package*/ class FieldFormatter implements Formatter<Field[]> {

  private final ObjectToStringValue objectToStringValue;

  public FieldFormatter() {
    this.objectToStringValue = new ObjectToStringValue();
  }

  private List<Field> filter(final List<Field> list) {
    return list.stream()
        .filter(field -> !field.isEnumConstant())
        .collect(Collectors.toList());
  }

  @Override
  public String format(final Field[] fields) {
    final StringBuilder sb = new StringBuilder();

    boolean wroteSomething = false;
    for (final Field field : filter(CommonFilter.filter(fields))) {
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

          sb.append(ReflectionUtils.getSaneType(field));
          sb.append(' ');
          sb.append(field.getName());

          final boolean isAccessible = field.isAccessible();

          if (!isAccessible) {
            field.setAccessible(true);
          }

          try {
            final ObjectToStringValue.Result result = objectToStringValue.getValue(field);
            sb.append(" = ");
            sb.append(result.getValue());

            if (result.isError()) {
              sb.append("; // ** FAILED TO DEDUCE RETURN VALUE **");
            }

            if (result.isError()) {
              Log.errField(
                  "Field: No idea what to set as value for '" + field + "' with value '" + field
                      .get(null) + "', fieldClass=" + result.getClazz());
            }
            Log.outField("Field: '" + field + "'= '" + result.getValue() + "'");
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

  private boolean isStaticFinal(final Field field) {
    final int mod = field.getModifiers();

    return Modifier.isStatic(mod) && Modifier.isFinal(mod);
  }
}
