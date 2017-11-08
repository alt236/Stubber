package uk.co.alt236.stubber.exporters.sections.clazz;

import uk.co.alt236.stubber.exporters.sections.Formatter;
import uk.co.alt236.stubber.exporters.sections.annotation.AnnotationFormatter;
import uk.co.alt236.stubber.exporters.sections.modifier.ModifierFormatter;
import uk.co.alt236.stubber.util.reflection.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;

public class ClassFormatter implements Formatter<Class<?>> {

  private final ClassFormatterInternal classFormatter;
  private final AnnotationFormatter annotationFormatter;

  public ClassFormatter() {
    classFormatter = new ClassFormatterInternal();
    annotationFormatter = new AnnotationFormatter();
  }

  @Override
  public String format(final Class declaringClass,
                       final Class<?> clazz) {
    final ReflectionUtils.ClassType type = ReflectionUtils.getClassType(clazz);
    final Class<?> superClass = clazz.getSuperclass();
    final Class<?>[] interfaceList = clazz.getInterfaces();
    final List<String> modifierList = ModifierFormatter.getModifiers(clazz);
    final List<String>
        annotationList =
        annotationFormatter.getAnnotations(clazz.getDeclaredAnnotations());

    final String annotations = getAnnotations(annotationList);
    final String modifiers = getModifiers(modifierList);
    final String classType = type.getSyntacticalName();
    final String className = clazz.getSimpleName();
    final String superClassName = getSuperClassName(superClass);
    final String interfaces = getInterfaces(interfaceList);

    final boolean isInterface = type == ReflectionUtils.ClassType.INTERFACE;
    return classFormatter.getDefinition(
        isInterface,
        annotations,
        modifiers,
        classType,
        className,
        superClassName,
        interfaces);
  }

  private String getAnnotations(final List<String> annotationList) {
    final StringBuilder sb = new StringBuilder();
    if (annotationList != null) {
      for (final String annotation : annotationList) {
        sb.append(annotation);
        sb.append('\n');
      }
    }
    return sb.toString();
  }

  private String getInterfaces(final Class<?>[] interfaces) {
    final StringBuilder sb = new StringBuilder();

    if (interfaces != null) {
      for (final Class<?> iFace : interfaces) {
        if (!Annotation.class.getName().equals(iFace.getName())) {
          if (sb.length() > 0) {
            sb.append(", ");
          }

          sb.append(iFace.getCanonicalName());
        }
      }
    }
    return sb.toString().trim();
  }

  private String getModifiers(final List<String> modifiers) {
    final StringBuilder sb = new StringBuilder();
    if (modifiers != null) {
      for (final String mod : modifiers) {
        sb.append(mod);
        sb.append(' ');
      }
    }
    return sb.toString().trim();
  }

  private String getSuperClassName(final Class<?> superClass) {
    final String retVal;

    if (superClass == null) {
      retVal = null;
    } else if (Object.class.getName().equals(superClass.getName())) {
      retVal = null;
    } else if (Enum.class.getName().equals(superClass.getName())) {
      retVal = null;
    } else {
      retVal = superClass.getCanonicalName();
    }

    return retVal;
  }
}
