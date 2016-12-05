package uk.co.alt236.stubber.exporters.templates;

import uk.co.alt236.stubber.exporters.sections.FormatterFactory;

/*package*/ class InnerClassTemplate extends ClassTemplate {

  public InnerClassTemplate(final String templateBasePath, final FormatterFactory formatterFactory,
                            final boolean blowOnReturn) {
    super(templateBasePath, TemplateManager.TEMPLATE_NAME_INNER_CLASS, formatterFactory,
          blowOnReturn);
  }
}
