package uk.co.alt236.stubber.exporters.templates.factory;

import uk.co.alt236.stubber.exporters.templates.TemplatePayload;

import java.util.List;

public class TemplatePopulator {

  private static final String TAB = "\t";
  private static final String COMMENT_NO_ENUMS = TAB + "// NO ENUMS DECLARED";
  private static final String COMMENT_NO_PACKAGE_NAME = TAB + "// NO PACKAGE_NAME!";
  private static final String COMMENT_NO_FIELDS = TAB + "// NO VISIBLE FIELDS!";
  private static final String COMMENT_NO_CLASS_DEFINITION = TAB + "// NO CLASS DEFINITION!";
  private static final String COMMENT_NO_INNER_CLASSES = TAB + "// NO INNER CLASSES!";
  private static final String COMMENT_NO_METHODS = TAB + "// NO VISIBLE METHODS!";
  private static final String COMMENT_NO_CONSTRUCTORS = TAB + "// NO VISIBLE CONSTRUCTORS!";

  private static String apply(final String original,
                              final String key,
                              final String data,
                              final String fallback) {

    if (data == null || data.trim().length() == 0) {
      return original.replace(key, fallback);
    } else {
      return original.replace(key, data);
    }
  }

  private String formatEnums(final List<String> enums) {
    final String retVal;

    if (enums == null || enums.isEmpty()) {
      retVal = "";
    } else {
      final StringBuilder sb = new StringBuilder();

      for (final String anEnum : enums) {
        if (sb.length() > 0) {
          sb.append(",\n");
        }

        sb.append(anEnum);
      }

      sb.append(";");
      retVal = sb.toString();
    }

    return retVal;
  }

  public String populate(final String baseTemplate,
                         TemplatePayload payload) {
    String data = baseTemplate;

    data = apply(data,
                 TemplateConstants.REP_TOKEN_PACKAGE,
                 "package " + payload.getPackageName() + ";",
                 COMMENT_NO_PACKAGE_NAME);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_CLASS_DEFINITION,
                 payload.getClassDefinition(),
                 COMMENT_NO_CLASS_DEFINITION);

    final String enumString = formatEnums(payload.getEnums());
    data = apply(data,
                 TemplateConstants.REP_TOKEN_ENUMS,
                 enumString,
                 COMMENT_NO_ENUMS);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_CONSTRUCTORS,
                 payload.getConstructors(),
                 COMMENT_NO_CONSTRUCTORS);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_FIELDS,
                 payload.getFields(),
                 COMMENT_NO_FIELDS);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_METHODS,
                 payload.getMethods(),
                 COMMENT_NO_METHODS);

    data = apply(data,
                 TemplateConstants.REP_TOKEN_INNER_CLASSES,
                 payload.getInnerClasses(),
                 COMMENT_NO_INNER_CLASSES);

    return data;
  }
}
