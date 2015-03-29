package uk.co.alt236.stubber.util;

import uk.co.alt236.stubber.containers.ClassWrapper;
import uk.co.alt236.stubber.containers.ConstructorWrapper;
import uk.co.alt236.stubber.containers.FieldWrapper;
import uk.co.alt236.stubber.containers.MethodWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 28/03/15.
 */
public class WrapperFactory {
    public static List<ClassWrapper> getWrapper(final Class<?>[] array) {
        final List<ClassWrapper> result = new ArrayList<>();

        for (final Class<?> clazz : array) {
            result.add(new ClassWrapper(clazz));
        }

        return result;
    }

    public static List<ConstructorWrapper> getWrapper(final Constructor<?>[] array) {
        final List<ConstructorWrapper> result = new ArrayList<>();

        for (final Constructor<?> method : array) {
            result.add(new ConstructorWrapper(method));
        }

        return result;
    }

    public static List<FieldWrapper> getWrapper(final Field[] array) {
        final List<FieldWrapper> result = new ArrayList<>();

        for (final Field member : array) {
            result.add(new FieldWrapper(member));
        }

        return result;
    }

    public static List<ClassWrapper> getWrapper(final List<Class<?>> list) {
        return getWrapper(list.toArray(new Class<?>[0]));
    }

    public static List<MethodWrapper> getWrapper(final Method[] array) {
        final List<MethodWrapper> result = new ArrayList<>();

        for (final Method method : array) {
            result.add(new MethodWrapper(method));
        }

        return result;
    }
}
