package uk.co.alt236.stubber.exporters.sections;

import uk.co.alt236.stubber.util.Log;
import uk.co.alt236.stubber.util.reflection.ReflectionUtils;
import uk.co.alt236.stubber.util.reflection.otsv.ObjectToStringValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/*package*/ class AnnotationFormatter {

  private static final String[] ANNOTATION_METHOD_BLACKLIST_ARRAY = {
      "hashCode",
      "toString",
      "annotationType"
  };

  private static final Set<String> ANNOTATION_METHOD_BLACKLIST
      = new HashSet<>(Arrays.asList(ANNOTATION_METHOD_BLACKLIST_ARRAY));

  private static final String FORMAT_ANNOTATION = "@%s";
  private static final String FORMAT_ANNOTATION_PARAMS = "@%s(%s)";

  private final ObjectToStringValue objectToStringValue;

  public AnnotationFormatter() {
    this.objectToStringValue = new ObjectToStringValue();
  }

  private String createAnnotation(final String name,
                                  final Map<String, String> values) {

    final String retVal;

    if (values.size() == 0) {
      retVal = String.format(Locale.US, FORMAT_ANNOTATION, name);
    } else {
      final StringBuilder sb = new StringBuilder();
      for (final String key : values.keySet()) {
        if (sb.length() > 0) {
          sb.append(", ");
        }

        sb.append(key);
        sb.append(" = ");
        sb.append(values.get(key));
      }

      retVal = String.format(Locale.US, FORMAT_ANNOTATION_PARAMS, name, sb.toString());
    }

    Log.outAnnotation("Annotation: '" + retVal + "'");
    return retVal;
  }

  public List<String> getAnnotations(final Annotation[] annotations) {
    final List<String> retVal = new ArrayList<>();

    for (final Annotation annotation : annotations) {
      final Map<String, String> values = getValues(annotation);

      final Class<? extends Annotation> type = annotation.annotationType();
      final String typeAsString = type.getCanonicalName();

      retVal.add(createAnnotation(typeAsString, values));
    }

    return retVal;
  }

  private String getReturnValue(final Object clazz, final Method method) {
    final Object value = ReflectionUtils.getReturnValue(clazz, method, null);
    return objectToStringValue.getValue(value).getValue();
  }

  private Map<String, String> getValues(final Annotation annotation) {
    final Map<String, String> retVal = new TreeMap<>();

    final Class<?> clazz = annotation.getClass();
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.getParameterCount() == 0
          && !method.getReturnType().equals(Void.TYPE)
          && !ANNOTATION_METHOD_BLACKLIST.contains(method.getName())) {
        retVal.put(method.getName(), getReturnValue(annotation, method));
      }
    }

    return retVal;
  }
}
