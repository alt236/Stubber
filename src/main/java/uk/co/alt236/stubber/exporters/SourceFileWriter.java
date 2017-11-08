package uk.co.alt236.stubber.exporters;


import uk.co.alt236.stubber.exporters.templates.PayloadFactory;
import uk.co.alt236.stubber.exporters.templates.TemplatePayload;
import uk.co.alt236.stubber.exporters.templates.factory.TemplateFactory;
import uk.co.alt236.stubber.util.FileIo;
import uk.co.alt236.stubber.util.Log;

import java.io.File;
import java.lang.reflect.Modifier;

/*oackage*/ class SourceFileWriter {

  private final PayloadFactory payloadFactory;
  private final TemplateFactory templateFactory;
  private final File exportDirectory;

  public SourceFileWriter(final TemplateFactory templateFactory,
                          final PayloadFactory payloadFactory,
                          final File exportDirectory) {
    this.templateFactory = templateFactory;
    this.exportDirectory = exportDirectory;
    this.payloadFactory = payloadFactory;
  }

  private static String convertPackageToPath(final String packageName) {
    return packageName.replace('.', File.separatorChar) + File.separatorChar;
  }

  public boolean writeToFile(final Class<?> clazz) {
    final boolean isPublic = Modifier.isPublic(clazz.getModifiers());
    final boolean isAnon = clazz.isAnonymousClass();

    Log.out(
        "About to export: Public: " + isPublic + ", Anon: " + isAnon
        + " -- '" + clazz.getCanonicalName() + "'");

    if (!isAnon) {
      Log.outClass("Exporting: " + clazz.getCanonicalName());

      final File packagePath = new File(
          exportDirectory,
          convertPackageToPath(clazz.getPackage().getName()));

      packagePath.mkdirs();

      final File classFile = new File(
          packagePath,
          clazz.getSimpleName() + ".java");

      final TemplatePayload payload = payloadFactory.createPayload(clazz);
      final String classContent = templateFactory.getOuterClassTemplate().populate(payload);
      FileIo.writeToFile(
          classFile,
          classContent,
          false,
          true);
      return true;
    } else {
      return false;
    }
  }

}
