package exporters;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import util.ReflectionUtils;
import util.ReflectionUtils.ClassType;
import util.ReflectionUtils.Exposure;
import containers.ClassWrapper;
import containers.FieldWrapper;
import containers.MethodWrapper;
import containers.Modifiers;

public class ClassExporterUtils {
	public static String getClassDefinition(ClassWrapper clazz){
		final ClassType type = clazz.getType();
		final ClassWrapper superClass = clazz.getSuperClass();
		final List<ClassWrapper> interfaces = clazz.getInterfaces();
		final List<String> modifiers = getModifiers(clazz);
		final StringBuilder sb = new StringBuilder();

		for(final String mod : modifiers){
			sb.append(mod);
			sb.append(' ');
		}

		sb.append(type.name().toLowerCase(Locale.US));
		sb.append(' ');
		sb.append(clazz.getSimplename());


		if(superClass != null){
			sb.append(" extends ");
			sb.append(superClass.getCanonicalName());
		}


		if(interfaces.size() > 1){
			sb.append(" implements ");
			boolean firstRun = true;

			for(ClassWrapper iface : interfaces){
				if(!firstRun){
					sb.append(", ");
				} else {
					firstRun = true;
				}

				sb.append(iface.getCanonicalName());
			}
		}

		return sb.toString().trim();
	}


	public static String getFieldDefinition(ClassWrapper clazz){
		final StringBuilder sb = new StringBuilder();

		boolean wroteSomething = false;
		for(final FieldWrapper field : clazz.getFields()){
			if(field.getExposure() == Exposure.PUBLIC){
				//We only care for static final values
				if(field.isStaticFinal()){
					wroteSomething = true;
					sb.append('\t');

					final List<String> modifiers = getModifiers(field);

					for(final String mod : modifiers){
						sb.append(mod);
						sb.append(' ');
					}

					final Field f = field.getEncapsulatedMember();
					final Class<?> t = f.getType();

					sb.append(field.getTypeAsString());
					sb.append(' ');
					sb.append(field.getName());

					final boolean isAccessible = f.isAccessible();
					if(!isAccessible){
						f.setAccessible(true);
					}
					try {
						final String value;
						boolean error = false;

						if(t == int.class){
							value = String.valueOf(f.getInt(null));
						}else if(t == double.class){
							value = String.valueOf(f.getDouble(null));
						} else if (t == boolean.class){
							value = String.valueOf(f.getBoolean(null));
						} else if (t == float.class){
							value = String.valueOf(f.getFloat(null));
						} else if (t == String.class){
							final String tmp = (String) f.get(null);
							value = tmp == null? "null" : "\"" + f.get(null).toString() + "\"";
						} else if (t == short.class){
							value = String.valueOf(f.getShort(null));
						} else if (t == byte.class){
							value = String.valueOf(f.getByte(null));
						} else if (t == long.class){
							value = String.valueOf(f.getLong(null));
						} else if (t == char.class){
							value = String.valueOf(f.getChar(null));
						} else {
							value = "null";
							error = true;
						}

						if(error){
							System.err.println("\t\tField: No idea what to set as value for '" + f + "' with value '" + f.get(null) + "'");
						} else {
							System.out.println("\t\tField: '" + f + "'= '" + value + "'");
						}

						sb.append(" = ");
						sb.append(value);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new IllegalStateException(e);
					}

					if(!isAccessible){
						f.setAccessible(false);
					}

					sb.append(';');
					sb.append('\n');
				}
			}
		}

		if(!wroteSomething){
			sb.append("\t// NO VALID FIELDS!");
		}

		return sb.toString();
	}

	public static String getMethods(ClassWrapper clazz){
		final StringBuilder sb = new StringBuilder();

		boolean wroteSomething = false;
		for(final MethodWrapper method : clazz.getMethods()){
			if(method.getExposure() == Exposure.PUBLIC){
				final List<String> modifiers = getModifiers(method);
				wroteSomething = true;

				sb.append('\t');
				for(final String mod : modifiers){
					sb.append(mod);
					sb.append(' ');
				}

				sb.append(method.getReturnType());
				sb.append(' ');
				sb.append(method.getName());

				sb.append('(');

				final Method f = method.getEncapsulatedMember();

				if(f.getParameterTypes().length > 0){
					for(int i = 0; i < f.getParameterTypes().length; i++){
						if(i > 0){
							sb.append(", ");
						}
						sb.append(ReflectionUtils.getSaneType(f.getParameterTypes()[i]));
						sb.append(' ');
						sb.append("param");
						sb.append(i);
					}
				}

				//TODO: Add parameter declaration
				sb.append(')');

				sb.append('{');
				sb.append('\n');
				sb.append("\t}");
				sb.append('\n');
				sb.append('\n');
			}
		}

		if(!wroteSomething){
			sb.append("\t// NO VALID METHODS!");
		}
		return sb.toString();
	}


	private static List<String> getModifiers(Modifiers mod){
		final List<String> modifiers = new ArrayList<>();

		if(mod.getExposure() != Exposure.DEFAULT){
			modifiers.add(mod.getExposure().name().toLowerCase(Locale.US));
		}

		if(mod.isFinal()){
			modifiers.add("final");
		}

		if(mod.isAbstract()){
			modifiers.add("abstract");
		}

		if(mod.isStatic()){
			modifiers.add("static");
		}

		if(mod.isSynchronized()){
			modifiers.add("synchronized");
		}

		if(mod.isTransient()){
			modifiers.add("transient");
		}

		if(mod.isVolatile()){
			modifiers.add("volatile");
		}

		return modifiers;
	}
}
