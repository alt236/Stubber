package uk.co.alt236.stubber.exporters.templates;

import java.util.Collections;
import java.util.List;

public class TemplatePayload {

  private final String packageName;
  private final String classDefinition;
  private final String constructors;
  private final String fields;
  private final String methods;
  private final String innerClasses;
  private final List<String> enums;

  private TemplatePayload(final Builder builder) {
    packageName = builder.packageName;
    classDefinition = builder.classDefinition;
    constructors = builder.constructors;
    fields = builder.fields;
    methods = builder.methods;
    innerClasses = builder.innerClasses;
    enums = builder.enums;
  }

  public String getClassDefinition() {
    return classDefinition;
  }

  public String getConstructors() {
    return constructors;
  }

  public List<String> getEnums() {
    return enums == null ? Collections.emptyList() : enums;
  }

  public String getFields() {
    return fields;
  }

  public String getInnerClasses() {
    return innerClasses;
  }

  public String getMethods() {
    return methods;
  }

  public String getPackageName() {
    return packageName;
  }

  public static final class Builder {

    private String packageName;
    private String classDefinition;
    private String constructors;
    private String fields;
    private String methods;
    private String innerClasses;
    private List<String> enums;

    public Builder() {
    }

    public Builder(final TemplatePayload copy) {
      this.packageName = copy.packageName;
      this.classDefinition = copy.classDefinition;
      this.constructors = copy.constructors;
      this.fields = copy.fields;
      this.methods = copy.methods;
      this.innerClasses = copy.innerClasses;
      this.enums = copy.enums;
    }

    public TemplatePayload build() {
      return new TemplatePayload(this);
    }

    public Builder withClassDefinition(final String val) {
      classDefinition = val;
      return this;
    }

    public Builder withConstructors(final String val) {
      constructors = val;
      return this;
    }

    public Builder withEnums(final List<String> val) {
      enums = val;
      return this;
    }

    public Builder withFields(final String val) {
      fields = val;
      return this;
    }

    public Builder withInnerClasses(final String val) {
      innerClasses = val;
      return this;
    }

    public Builder withMethods(final String val) {
      methods = val;
      return this;
    }

    public Builder withPackageName(final String val) {
      packageName = val;
      return this;
    }
  }
}
