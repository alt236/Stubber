package uk.co.alt236.stubber.exporters.templates.factory;

import uk.co.alt236.stubber.exporters.templates.TemplatePayload;

public class InnerClassTemplate implements Template {

  private final String templateString;
  private final TemplatePopulator populator;

  public InnerClassTemplate(final TemplatePopulator populator,
                            final String templateString) {
    this.templateString = templateString;
    this.populator = populator;
  }

  @Override
  public String populate(final TemplatePayload payload) {
    return populator.populate(templateString, payload);
  }
}
