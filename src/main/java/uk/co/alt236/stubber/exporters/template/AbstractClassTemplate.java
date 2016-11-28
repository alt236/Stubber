package uk.co.alt236.stubber.exporters.template;

import uk.co.alt236.stubber.exporters.template.sections.ClassFormatter;
import uk.co.alt236.stubber.exporters.template.sections.ConstructorFormatter;
import uk.co.alt236.stubber.exporters.template.sections.FieldFormatter;
import uk.co.alt236.stubber.exporters.template.sections.MethodFormatter;

abstract class AbstractClassTemplate {

  private static final String TAB = "\t";
  private static final String NO_PACKAGE_NAME = TAB + "// NO PACKAGE_NAME!";
  private static final String NO_FIELDS = TAB + "// NO VISIBLE FIELDS!";
  private static final String NO_CLASS_DEFINITION = TAB + "// NO CLASS DEFINITION!";
  private static final String NO_INNER_CLASSES = TAB + "// NO INNER CLASSES!";
  private static final String NO_METHODS = TAB + "// NO VISIBLE METHODS!";
  private static final String NO_CONSTRUCTORS = TAB + "// NO VISIBLE CONSTRUCTORS!";

  private final TemplateManager templateManager;
  private final String baseTemplatePath;
  private final String key;
  private final boolean blowOnReturn;

  private final ClassFormatter classFormatter;
  private final ConstructorFormatter constructorFormatter;
  private final FieldFormatter fieldFormatter;
  private final MethodFormatter methodFormatter;
  private String packageName;
  private String classDefinition;
  private String constructors;
  private String fields;
  private String methods;
  private String innerClasses;

  protected AbstractClassTemplate(final String baseTemplatePath,
                                  final String template,
                                  final boolean blowOnReturn) {

    this.templateManager = new TemplateManager(baseTemplatePath);
    this.classFormatter = new ClassFormatter();
    this.constructorFormatter = new ConstructorFormatter();
    this.fieldFormatter = new FieldFormatter();
    this.methodFormatter = new MethodFormatter(blowOnReturn);
    this.baseTemplatePath = baseTemplatePath;
    this.blowOnReturn = blowOnReturn;
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
    classDefinition = classFormatter.getClassDefinition(clazz);
    constructors = constructorFormatter.getConstructors(clazz);
    fields = fieldFormatter.getFieldDefinition(clazz);
    methods = methodFormatter.getMethods(clazz);
    innerClasses = getInnerClasses(clazz.getDeclaredClasses());

    return build();
  }

  private String build() {
    String data = getTemplate();

    data = apply(data,
                 TemplateConstants.REP_TOKEN_PACKAGE,
                 packageName + ";",
                 NO_PACKAGE_NAME);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_CLASS_DEFINITION,
                 classDefinition,
                 NO_CLASS_DEFINITION);

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

  public String getInnerClasses(final Class<?>[] classes) {
    final String retVal;

    if (classes != null && classes.length > 0) {
      final StringBuilder sb = new StringBuilder();

      final InnerClassTemplate template = new InnerClassTemplate(baseTemplatePath, blowOnReturn);

      for (final Class<?> inner : classes) {
        System.out.println("\t\tInner Class: '" + inner.getCanonicalName() + "'");
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
