package uk.co.alt236.stubber.exporters.sections;

/*package*/ class ClassFormatterInternal {

  private static final String EXTENDS_KEYWORD = "extends ";
  private static final String IMPLEMENTS_KEYWORD = "implements ";

  private String appendOptionalValue(final StringBuilder sb,
                                     final String prefix,
                                     final String value,
                                     final String suffix) {

    if (value != null && value.trim().length() > 0) {
      if (prefix != null) {
        sb.append(prefix);
      }

      sb.append(value);

      if (suffix != null) {
        sb.append(suffix);
      }
    }

    return sb.toString();
  }

  public String getDefinition(final boolean isInterface,
                              final String annotations,
                              final String modifiers,
                              final String classType,
                              final String className,
                              final String superClassName,
                              final String interfaces) {

    final StringBuilder sb = new StringBuilder();

    appendOptionalValue(sb, null, annotations, "\n");
    appendOptionalValue(sb, null, modifiers, " ");
    appendOptionalValue(sb, null, classType, " ");
    appendOptionalValue(sb, null, className, " ");

    if (isInterface) {
      // Interfaces do not extend classes, but extend other interfaces
      appendOptionalValue(sb, EXTENDS_KEYWORD, interfaces, " ");
    } else {
      appendOptionalValue(sb, EXTENDS_KEYWORD, superClassName, " ");
      appendOptionalValue(sb, IMPLEMENTS_KEYWORD, interfaces, " ");
    }

    return sb.toString();
  }
}
