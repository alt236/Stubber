package uk.co.alt236.stubber.exporters.templates;

import uk.co.alt236.stubber.exporters.sections.FormatterFactory;

/*package*/ class OuterClassTemplate extends ClassTemplate {

  public OuterClassTemplate(final String templateBasePath, final FormatterFactory formatterFactory,
                            final boolean blowOnReturn) {
    super(templateBasePath, TemplateManager.TEMPLATE_NAME_CLASS_FILE, formatterFactory,
          blowOnReturn);
  }
}
