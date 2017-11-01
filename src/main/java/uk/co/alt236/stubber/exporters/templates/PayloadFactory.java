package uk.co.alt236.stubber.exporters.templates;

import uk.co.alt236.stubber.exporters.CommonFilter;
import uk.co.alt236.stubber.exporters.sections.FormatterFactory;
import uk.co.alt236.stubber.exporters.templates.factory.TemplateFactory;
import uk.co.alt236.stubber.util.Log;

import java.util.List;

public class PayloadFactory {

  private final boolean blowOnReturn;
  private final FormatterFactory formatterFactory;
  private final TemplateFactory templateFactory;

  public PayloadFactory(final TemplateFactory templateFactory,
                        final FormatterFactory formatterFactory,
                        final boolean blowOnReturn) {

    this.blowOnReturn = blowOnReturn;
    this.formatterFactory = formatterFactory;
    this.templateFactory = templateFactory;
  }

  public TemplatePayload createPayload(final Class<?> clazz) {
    final String packageName;
    final String classDefinition;
    final String constructors;
    final String fields;
    final String methods;
    final String innerClasses;
    final List<String> enums;

    packageName = clazz.getPackage().getName();

    classDefinition = formatterFactory
        .getFormatter(clazz)
        .format(clazz, clazz);

    constructors = formatterFactory
        .getConstructorFormatter()
        .format(clazz, clazz.getDeclaredConstructors());

    fields = formatterFactory
        .getFieldFormatter(clazz)
        .format(clazz, clazz.getDeclaredFields());

    methods = formatterFactory
        .getMethodFormatter(clazz, blowOnReturn)
        .format(clazz, clazz.getDeclaredMethods());

    enums = formatterFactory.getEnumFormatter(clazz)
        .getEnums(clazz.getEnumConstants());

    innerClasses = getInnerClasses(clazz.getDeclaredClasses());

    return new TemplatePayload
        .Builder()
        .withPackageName(packageName)
        .withClassDefinition(classDefinition)
        .withConstructors(constructors)
        .withEnums(enums)
        .withFields(fields)
        .withInnerClasses(innerClasses)
        .withMethods(methods)
        .build();
  }

  private String getInnerClasses(final Class[] classes) {
    final String retVal;

    if (classes != null && classes.length > 0) {
      final StringBuilder sb = new StringBuilder();

      for (final Class inner : CommonFilter.filter(classes)) {
        Log.outInnerClass("\t\tInner Class: '" + inner.getCanonicalName() + "'");
        final TemplatePayload payload = createPayload(inner);
        sb.append(templateFactory.getInnerClassTemplate().populate(payload));
        sb.append('\n');
      }
      retVal = sb.toString();
    } else {
      retVal = null;
    }

    return retVal;
  }
}
