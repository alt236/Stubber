package uk.co.alt236.stubber.exporters.templates;

import uk.co.alt236.stubber.exporters.sections.FormatterFactory;

public final class TemplateFactory {

  private TemplateFactory() {
    // NOOP
  }

  public static ClassTemplate create(final String templateDirectory,
                                     final FormatterFactory formatterFactory,
                                     final boolean blowOnReturn) {
    return new OuterClassTemplate(templateDirectory, formatterFactory, blowOnReturn);
  }
}
