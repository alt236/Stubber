package uk.co.alt236.stubber.exporters.v2.Containers2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FullClassListFactory {

  private static final Comparator<Class<?>> COMPARATOR =
      (o1, o2) -> o1.getCanonicalName().compareTo(o2.getCanonicalName());

  private Collection<? extends Class<?>> filterValidClasses(final Class<?>[] classes) {
    final List<Class<?>> retVal = new ArrayList<>();

    for (final Class<?> clazz : classes) {
      if (!clazz.isAnonymousClass()) {
        retVal.add(clazz);
      }
    }

    return retVal;
  }

  public Collection<Class<?>> getAllClasses(final List<Class<?>> initialList) {
    final Set<Class<?>> retVal = new TreeSet<>(COMPARATOR);
//    final List<Class<?>> tmp = new ArrayList<>();
//
//    for (final Class<?> clazz : initialList) {
//      tmp.addAll(getInterfaces(clazz));
//      tmp.addAll(getInnerClasses(clazz));
//    }

    retVal.addAll(initialList);
    return retVal;
  }

  private Collection<? extends Class<?>> getInnerClasses(final Class<?> clazz) {
    final Class<?>[] classes = clazz.getDeclaredClasses();
    return filterValidClasses(classes);
  }

  private Collection<? extends Class<?>> getInterfaces(final Class<?> clazz) {
    final Class<?>[] classes = clazz.getInterfaces();
    return filterValidClasses(classes);
  }
}
