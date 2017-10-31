package uk.co.alt236.stubber.exporters.templates;

import uk.co.alt236.stubber.exporters.sections.FormatterFactory;

/*package*/ class OuterClassTemplate extends ClassTemplate {

  public OuterClassTemplate(final TemplateManager templates,
                            final FormatterFactory formatterFactory,
                            final boolean blowOnReturn) {
    super(templates, formatterFactory, blowOnReturn);
  }
}
