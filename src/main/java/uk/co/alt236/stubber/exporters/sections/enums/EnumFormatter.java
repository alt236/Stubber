package uk.co.alt236.stubber.exporters.sections.enums;

import java.util.ArrayList;
import java.util.List;

public class EnumFormatter {

  private final Class<?> clazz;

  public EnumFormatter(final Class<?> clazz) {
    this.clazz = clazz;
  }

  public List<String> getEnums(final Object[] enums) {
    final List<String> retVal = new ArrayList<>();
    if (enums != null) {
      for (final Object anEnum : enums) {
        final String name = ((Enum<?>) anEnum).name();
        retVal.add(name);
      }
    }
    return retVal;
  }
}
