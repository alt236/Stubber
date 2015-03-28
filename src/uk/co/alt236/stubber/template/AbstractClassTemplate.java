package uk.co.alt236.stubber.template;

import uk.co.alt236.stubber.containers.ClassWrapper;
import uk.co.alt236.stubber.exporters.ClassExporterUtils;
import uk.co.alt236.stubber.util.ReflectionUtils;

import java.util.List;

/**
 * Created by alex on 28/03/15.
 */
abstract class AbstractClassTemplate {
    private static final String TAB = "\t";
    private static final String NO_PACKAGE_NAME = TAB + "// NO PACKAGE_NAME!";
    private static final String NO_FIELDS = TAB + "// NO VISIBLE FIELDS!";
    private static final String NO_CLASS_DEFINITION = TAB + "// NO CLASS DEFINITION!";
    private static final String NO_INNER_CLASSES = TAB + "// NO INNER CLASSES!";
    private static final String NO_METHODS = TAB + "// NO VISIBLE METHODS!";
    private static final String NO_CONSTRUCTORS = TAB + "// NO VISIBLE CONSTRUCTORS!";

    private final TemplateManager templateManager;
    private final String baseTemplatePath;
    private final String key;

    private String packageName;
    private String classDefinition;
    private String constructors;
    private String fields;
    private String methods;
    private String innerClasses;

    protected AbstractClassTemplate(final String baseTemplatePath, final String template){
        this.templateManager = new TemplateManager(baseTemplatePath);
        this.baseTemplatePath = baseTemplatePath;
        this.key = template;
    }

    public String build(final ClassWrapper clazz) {
        setPackageName(clazz.getPackageName());
        setClassDefinitions(ClassExporterUtils.getClassDefinition(clazz));
        setConstructors(ClassExporterUtils.getConstructors(clazz));
        setFields(ClassExporterUtils.getFieldDefinition(clazz));
        setMethods(ClassExporterUtils.getMethods(clazz));
        setInnerClasses(clazz.getInnerClasses());

        return build();
    }

    private String build() {
        String data = getTemplate();

        data = apply(data,
                TemplateConstants.REP_TOKEN_PACKAGE,
                packageName + ";",
                NO_PACKAGE_NAME);


        data = apply(data,
                TemplateConstants.REP_TOKEN_CLASS_DEFINITION,
                classDefinition,
                NO_CLASS_DEFINITION);

        data = apply(data,
                TemplateConstants.REP_TOKEN_CONSTRUCTORS,
                constructors,
                NO_CONSTRUCTORS);

        data = apply(data,
                TemplateConstants.REP_TOKEN_FIELDS,
                fields,
                NO_FIELDS);

        data = apply(data,
                TemplateConstants.REP_TOKEN_METHODS,
                methods,
                NO_METHODS);

        data = apply(data,
                TemplateConstants.REP_TOKEN_INNER_CLASSES,
                innerClasses,
                NO_INNER_CLASSES);

        return data;
    }

    private String apply(final String original, final String key, final String data, final String fallback){
        if(data == null || data.trim().length() == 0){
            return original.replace(key, fallback);
        } else {
            return original.replace(key, data);
        }
    }

    private String getTemplate(){
        return templateManager.getTemplate(key);
    }

    private void setClassDefinitions(final String text){
        classDefinition = text;
    }

    private void setConstructors(final String text){
        constructors = text;
    }

    private void setFields(final String text){
        fields = text;
    }

    public void setInnerClasses(final List<ClassWrapper> classes) {

        if(classes.size() > 0){
            final StringBuilder sb = new StringBuilder();

            final InnerClassTemplate template = new InnerClassTemplate(baseTemplatePath);

            for(final ClassWrapper inner : classes){
                if(inner.getExposure() == ReflectionUtils.Exposure.PUBLIC){
                    sb.append(template.build(inner));
                    sb.append('\n');
                }
            }
            innerClasses = sb.toString();
        } else {
            innerClasses = null;
        }
    }

    private void setMethods(final String text){
        methods = text;
    }

    private void setPackageName(final String text){
        packageName = text;
    }
}
