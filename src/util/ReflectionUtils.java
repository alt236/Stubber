package util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import containers.ClassWrapper;
import containers.FieldWrapper;
import containers.MethodWrapper;

public class ReflectionUtils {
	private static final String PACKAGE_SEPARATOR = ".";

	public static ClassType getClassType(Class<?> clazz){
		if(Modifier.isInterface(clazz.getModifiers())){
			return ClassType.INTERFACE;
		} else {
			return ClassType.CLASS;
		}
	}

	public static Exposure getExposure(Class<?> clazz){
		return getExposure(clazz.getModifiers());
	}

	private static Exposure getExposure(int modifiers){
		if(Modifier.isPrivate(modifiers)){
			return Exposure.PRIVATE;
		} else if(Modifier.isPublic(modifiers)){
			return Exposure.PUBLIC;
		} else if(Modifier.isProtected(modifiers)){
			return Exposure.PROTECTED;
		} else {
			return Exposure.DEFAULT;
		}
	}

	public static Exposure getExposure(Member member){
		return getExposure(member.getModifiers());
	}

	public static String getPackageName(final Class<?> clazz) {
		if (clazz == null){
			throw new IllegalStateException("Class is null!");
		}

		final String className = clazz.getName();

		if (className == null || className.length() < 1){
			throw new IllegalStateException("Class name is empty/null!");
		}

		final int index = className.lastIndexOf(PACKAGE_SEPARATOR);
		if (index != -1){
			return className.substring(0, index);
		} else {
			throw new IllegalStateException("Could not find a '.' !");
		}
	}

	public static String getSaneType(Class<?> clazz){
		if(clazz.isArray()){
			return clazz.getCanonicalName();
		} else {
			return clazz.getCanonicalName();
		}
	}

	public static String getSaneType(Field field){
		return getSaneType(field.getType());
	}

	public static List<ClassWrapper> getWrapper(final Class<?>[] array){
		final List<ClassWrapper> result = new ArrayList<>();

		for(final Class<?> clazz : array){
			result.add(new ClassWrapper(clazz));
		}

		return result;
	}

	public static List<FieldWrapper> getWrapper(Field[] array){
		final List<FieldWrapper> result = new ArrayList<>();

		for(Field member : array){
			result.add(new FieldWrapper(member));
		}

		return result;
	}

	public static List<ClassWrapper> getWrapper(List<Class<?>> list){
		return getWrapper(list.toArray(new Class<?>[0]));
	}

	public static List<MethodWrapper> getWrapper(Method[] array){
		final List<MethodWrapper> result = new ArrayList<>();

		for(Method method : array){
			result.add(new MethodWrapper(method));
		}

		return result;
	}



	public enum ClassType{
		CLASS,
		INTERFACE
	}

	public enum Exposure{
		DEFAULT,
		PRIVATE,
		PROTECTED,
		PUBLIC
	}
}
