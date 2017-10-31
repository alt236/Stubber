package uk.co.alt236.stubber.exporters.templates;

import uk.co.alt236.stubber.resources.Templates;

class TemplateManager {

  private final Templates templates;

  public TemplateManager(final Templates templates) {
    this.templates = templates;
  }

  public String getClassTemplate() {
    return templates.getClassFileTemplate();
  }

  public String getInnerClassTemplate() {
    return templates.getInnerClassFileTemplate();
  }
}
