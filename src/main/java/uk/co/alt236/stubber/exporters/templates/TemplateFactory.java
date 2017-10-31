package uk.co.alt236.stubber.exporters.templates;

import uk.co.alt236.stubber.exporters.sections.FormatterFactory;
import uk.co.alt236.stubber.resources.Templates;

public final class TemplateFactory {

  private TemplateFactory() {
    // NOOP
  }

  public static ClassTemplate create(final Templates templates,
                                     final FormatterFactory formatterFactory,
                                     final boolean blowOnReturn) {
    return new OuterClassTemplate(new TemplateManager(templates), formatterFactory, blowOnReturn);
  }
}
