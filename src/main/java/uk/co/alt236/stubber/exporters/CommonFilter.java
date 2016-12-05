package uk.co.alt236.stubber.exporters;

import uk.co.alt236.stubber.util.reflection.ReflectionUtils;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CommonFilter {

  private static final EnumSet<ReflectionUtils.Exposure> VALID_CLASS_EXPOSURE =
      EnumSet.of(ReflectionUtils.Exposure.PUBLIC,
                 ReflectionUtils.Exposure.DEFAULT,
                 ReflectionUtils.Exposure.PROTECTED,
                 ReflectionUtils.Exposure.PRIVATE);

  private static final EnumSet<ReflectionUtils.Exposure> VALID_MEMBER_EXPOSURE =
      EnumSet.of(ReflectionUtils.Exposure.PUBLIC, ReflectionUtils.Exposure.PROTECTED);

  public static <T extends Member> List<T> filter(final T[] members) {
    final List<T> retVal = new ArrayList<>();

    for (final T member : members) {
      final ReflectionUtils.Exposure exposure = ReflectionUtils.getExposure(member);

      if (!member.isSynthetic() && VALID_MEMBER_EXPOSURE.contains(exposure)) {
        retVal.add(member);
      }
    }

    return retVal;
  }

  public static List<Class<?>> filter(final Class<?>[] classes) {
    final List<Class<?>> retVal = new ArrayList<>();

    for (final Class<?> clazz : classes) {
      final ReflectionUtils.Exposure exposure = ReflectionUtils.getExposure(clazz.getModifiers());

      if (!clazz.isSynthetic() && VALID_CLASS_EXPOSURE.contains(exposure)) {
        retVal.add(clazz);
      }
    }

    return retVal;
  }
}