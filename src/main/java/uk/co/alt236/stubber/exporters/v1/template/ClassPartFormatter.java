package uk.co.alt236.stubber.exporters.v1.template;

import uk.co.alt236.stubber.exporters.v1.containers.ClassWrapper;
import uk.co.alt236.stubber.exporters.v1.containers.ConstructorWrapper;
import uk.co.alt236.stubber.exporters.v1.containers.FieldWrapper;
import uk.co.alt236.stubber.exporters.v1.containers.MethodWrapper;
import uk.co.alt236.stubber.exporters.v1.containers.Modifiers;
import uk.co.alt236.stubber.util.ReflectionUtils;
import uk.co.alt236.stubber.util.ReflectionUtils.ClassType;
import uk.co.alt236.stubber.util.ReflectionUtils.Exposure;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClassPartFormatter {

  private final boolean blowOnReturn;

  public ClassPartFormatter(final boolean throwExceptionOnReturn) {
    this.blowOnReturn = throwExceptionOnReturn;
  }

  public String getClassDefinition(final ClassWrapper clazz) {
    final ClassType type = clazz.getType();
    final ClassWrapper superClass = clazz.getSuperClass();
    final List<ClassWrapper> interfaces = clazz.getInterfaces();
    final List<String> modifiers = getModifiers(clazz);
    final StringBuilder sb = new StringBuilder();

    for (final String mod : modifiers) {
      sb.append(mod);
      sb.append(' ');
    }

    sb.append(type.name().toLowerCase(Locale.US));
    sb.append(' ');
    sb.append(clazz.getSimpleName());

    if (superClass != null) {
      sb.append(" extends ");
      sb.append(superClass.getCanonicalName());
    }

    if (interfaces.size() > 1) {
      sb.append(" implements ");
      boolean firstRun = true;

      for (final ClassWrapper iFace : interfaces) {
        if (!firstRun) {
          sb.append(", ");
        } else {
          firstRun = true;
        }

        sb.append(iFace.getCanonicalName());
      }
    }

    return sb.toString().trim();
  }

  public String getConstructors(final ClassWrapper clazz) {
    final StringBuilder sb = new StringBuilder();

    boolean wroteSomething = false;
    for (final ConstructorWrapper method : clazz.getConstructors()) {
      if (method.getExposure() == Exposure.PUBLIC) {
        final List<String> modifiers = getModifiers(method);
        wroteSomething = true;

        sb.append('\t');
        for (final String mod : modifiers) {
          sb.append(mod);
          sb.append(' ');
        }

        sb.append(method.getName());

        sb.append('(');

        final Constructor<?> f = method.getEncapsulatedMember();

        if (f.getParameterTypes().length > 0) {
          for (int i = 0; i < f.getParameterTypes().length; i++) {
            if (i > 0) {
              sb.append(", ");
            }
            sb.append(ReflectionUtils.getSaneType(f.getParameterTypes()[i]));
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
    }

    if (!wroteSomething) {
      sb.append("\t// NO VISIBLE CONSTRUCTORS!");
    }
    return sb.toString();
  }

  public String getFieldDefinition(final ClassWrapper clazz) {
    final StringBuilder sb = new StringBuilder();

    boolean wroteSomething = false;
    for (final FieldWrapper field : clazz.getFields()) {
      if (field.getExposure() == Exposure.PUBLIC) {
        //We only care for static final values
        if (field.isStaticFinal()) {
          wroteSomething = true;
          sb.append('\t');

          final List<String> modifiers = getModifiers(field);

          for (final String mod : modifiers) {
            sb.append(mod);
            sb.append(' ');
          }

          final Field f = field.getEncapsulatedMember();
          final Class<?> fieldClass = f.getType();

          sb.append(field.getTypeAsString());
          sb.append(' ');
          sb.append(field.getName());

          final boolean isAccessible = f.isAccessible();
          if (!isAccessible) {
            f.setAccessible(true);
          }
          try {
            final String value;
            boolean error = false;

            if (fieldClass.isEnum()) {
              final Object tmp = f.get(null);

              if (tmp == null) {
                value = "null";
              } else {
                value = fieldClass.getCanonicalName() + "." + String.valueOf(tmp);
              }
            } else {
              if (fieldClass == int.class) {
                value = String.valueOf(f.getInt(null));
              } else if (fieldClass == double.class) {
                value = String.valueOf(f.getDouble(null));
              } else if (fieldClass == boolean.class) {
                value = String.valueOf(f.getBoolean(null));
              } else if (fieldClass == float.class) {
                value = String.valueOf(f.getFloat(null));
              } else if (fieldClass == String.class) {
                final String tmp = (String) f.get(null);
                value = tmp == null ? "null" : "\"" + f.get(null).toString() + "\"";
              } else if (fieldClass == short.class) {
                value = String.valueOf(f.getShort(null));
              } else if (fieldClass == byte.class) {
                value = String.valueOf(f.getByte(null));
              } else if (fieldClass == long.class) {
                value = String.valueOf(f.getLong(null));
              } else if (fieldClass == char.class) {
                value = String.valueOf(f.getChar(null));
              } else {
                value = "null";
                error = true;
              }
            }
            if (error) {
              System.err.println(
                  "\t\tField: No idea what to set as value for '" + f + "' with value '" + f
                      .get(null) + "'");
            } else {
              System.out.println("\t\tField: '" + f + "'= '" + value + "'");
            }

            sb.append(" = ");
            sb.append(value);
          } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
          }

          if (!isAccessible) {
            f.setAccessible(false);
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

  public String getMethods(final ClassWrapper clazz) {
    final StringBuilder sb = new StringBuilder();
    final boolean isInterface = clazz.getType() == ClassType.INTERFACE;

    boolean wroteSomething = false;
    for (final MethodWrapper method : clazz.getMethods()) {
      if (method.getExposure() == Exposure.PUBLIC) {
        final List<String> modifiers = getModifiers(method);
        final boolean isAbstract = method.isAbstract();

        wroteSomething = true;

        sb.append('\t');
        for (final String mod : modifiers) {
          sb.append(mod);
          sb.append(' ');
        }

        sb.append(method.getReturnTypeAsString());
        sb.append(' ');
        sb.append(method.getName());

        sb.append('(');

        final Method f = method.getEncapsulatedMember();

        if (f.getParameterTypes().length > 0) {
          for (int i = 0; i < f.getParameterTypes().length; i++) {
            if (i > 0) {
              sb.append(", ");
            }
            sb.append(ReflectionUtils.getSaneType(f.getParameterTypes()[i]));
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


  private List<String> getModifiers(final Modifiers mod) {
    final List<String> modifiers = new ArrayList<>();

    if (mod.getExposure() != Exposure.DEFAULT) {
      modifiers.add(mod.getExposure().name().toLowerCase(Locale.US));
    }

    if (mod.isFinal()) {
      modifiers.add("final");
    }

    if (mod.isAbstract()) {
      modifiers.add("abstract");
    }

    if (mod.isStatic()) {
      modifiers.add("static");
    }

    if (mod.isSynchronized()) {
      modifiers.add("synchronized");
    }

    if (mod.isTransient()) {
      modifiers.add("transient");
    }

    if (mod.isVolatile()) {
      modifiers.add("volatile");
    }

    return modifiers;
  }
}
