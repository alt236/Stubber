package uk.co.alt236.stubber.exporters.templates.factory;

import uk.co.alt236.stubber.resources.Templates;

public class TemplateFactory {

  private final Templates templates;
  private final TemplatePopulator templatePopulator;

  public TemplateFactory(final Templates templates,
                         final TemplatePopulator templatePopulator) {
    this.templates = templates;
    this.templatePopulator = templatePopulator;
  }

  public Template getInnerClassTemplate() {
    return new InnerClassTemplate(templatePopulator, templates.getInnerClassFileTemplate());
  }

  public Template getOuterClassTemplate() {
    return new InnerClassTemplate(templatePopulator, templates.getClassFileTemplate());
  }
}
