package uk.co.alt236.stubber.exporters.templates.factory;

import uk.co.alt236.stubber.exporters.templates.TemplatePayload;

public interface Template {

  String populate(TemplatePayload payload);
}
