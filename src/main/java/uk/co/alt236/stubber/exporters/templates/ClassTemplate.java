package uk.co.alt236.stubber.exporters.templates;

import uk.co.alt236.stubber.exporters.CommonFilter;
import uk.co.alt236.stubber.exporters.sections.FormatterFactory;
import uk.co.alt236.stubber.util.Log;

import java.util.List;

public abstract class ClassTemplate {

  private static final String TAB = "\t";
  private static final String NO_ENUMS = TAB + "// NO ENUMS DECLARED";
  private static final String NO_PACKAGE_NAME = TAB + "// NO PACKAGE_NAME!";
  private static final String NO_FIELDS = TAB + "// NO VISIBLE FIELDS!";
  private static final String NO_CLASS_DEFINITION = TAB + "// NO CLASS DEFINITION!";
  private static final String NO_INNER_CLASSES = TAB + "// NO INNER CLASSES!";
  private static final String NO_METHODS = TAB + "// NO VISIBLE METHODS!";
  private static final String NO_CONSTRUCTORS = TAB + "// NO VISIBLE CONSTRUCTORS!";

  private final TemplateManager templateManager;
  private final boolean blowOnReturn;
  private final String baseTemplatePath;
  private final String key;
  private final FormatterFactory formatterFactory;

  private String packageName;
  private String classDefinition;
  private String constructors;
  private String fields;
  private String methods;
  private String innerClasses;
  private List<String> enums;

  protected ClassTemplate(final String baseTemplatePath,
                          final String template,
                          final FormatterFactory formatterFactory,
                          final boolean blowOnReturn) {

    this.templateManager = new TemplateManager(baseTemplatePath);
    this.baseTemplatePath = baseTemplatePath;
    this.blowOnReturn = blowOnReturn;
    this.formatterFactory = formatterFactory;
    this.key = template;
  }

  private static String apply(final String original,
                              final String key,
                              final String data,
                              final String fallback) {

    if (data == null || data.trim().length() == 0) {
      return original.replace(key, fallback);
    } else {
      return original.replace(key, data);
    }
  }

  public String build(final Class<?> clazz) {
    packageName = clazz.getPackage().getName();
    classDefinition = formatterFactory
        .getFormatter(clazz)
        .format(clazz);
    constructors = formatterFactory
        .getConstructorFormatter()
        .format(clazz.getDeclaredConstructors());
    fields = formatterFactory
        .getFieldFormatter(clazz)
        .format(clazz.getDeclaredFields());
    methods = formatterFactory
        .getMethodFormatter(clazz, blowOnReturn)
        .format(clazz.getDeclaredMethods());

    enums = formatterFactory.getEnumFormatter(clazz)
        .getEnums(clazz.getEnumConstants());

    innerClasses = getInnerClasses(clazz.getDeclaredClasses());

    return build();
  }

  private String build() {
    String data = getTemplate();

    data = apply(data,
                 TemplateConstants.REP_TOKEN_PACKAGE,
                 "package " + packageName + ";",
                 NO_PACKAGE_NAME);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_CLASS_DEFINITION,
                 classDefinition,
                 NO_CLASS_DEFINITION);

    final String enumString = formatEnums(enums);
    data = apply(data,
                 TemplateConstants.REP_TOKEN_ENUMS,
                 enumString,
                 NO_ENUMS);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_CONSTRUCTORS,
                 constructors,
                 NO_CONSTRUCTORS);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_FIELDS,
                 fields,
                 NO_FIELDS);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_METHODS,
                 methods,
                 NO_METHODS);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_INNER_CLASSES,
                 innerClasses,
                 NO_INNER_CLASSES);

    return data;
  }

  private String formatEnums(final List<String> enums) {
    final String retVal;

    if (enums == null || enums.isEmpty()) {
      retVal = "";
    } else {
      final StringBuilder sb = new StringBuilder();

      for (final String anEnum : enums) {
        if (sb.length() > 0) {
          sb.append(",\n");
        }

        sb.append(anEnum);
      }

      sb.append(";");
      retVal = sb.toString();
    }

    return retVal;
  }

  private String getInnerClasses(final Class[] classes) {
    final String retVal;

    if (classes != null && classes.length > 0) {
      final StringBuilder sb = new StringBuilder();

      final InnerClassTemplate template
          = new InnerClassTemplate(baseTemplatePath, formatterFactory, blowOnReturn);

      for (final Class inner : CommonFilter.filter(classes)) {
        Log.outInnerClass("\t\tInner Class: '" + inner.getCanonicalName() + "'");
        sb.append(template.build(inner));
        sb.append('\n');
      }
      retVal = sb.toString();
    } else {
      retVal = null;
    }

    return retVal;
  }

  private String getTemplate() {
    return templateManager.getTemplate(key);
  }
}
