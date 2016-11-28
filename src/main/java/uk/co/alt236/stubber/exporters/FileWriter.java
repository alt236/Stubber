package uk.co.alt236.stubber.exporters;


import uk.co.alt236.stubber.exporters.template.ClassTemplate;
import uk.co.alt236.stubber.util.FileIo;

import java.io.File;
import java.lang.reflect.Modifier;

/*oackage*/ class FileWriter {

  private final ClassTemplate classTemplate;
  private final File exportDirectory;

  public FileWriter(final File exportDirectory,
                    final ClassTemplate classTemplate) {
    this.exportDirectory = exportDirectory;
    this.classTemplate = classTemplate;
  }

  private static String convertPackageToPath(final String packageName) {
    return packageName.replace('.', File.separatorChar) + File.separatorChar;
  }

  public boolean writeToFile(final Class<?> clazz) {
    final boolean isPublic = Modifier.isPublic(clazz.getModifiers());
    final boolean isAnon = clazz.isAnonymousClass();

    System.out.println(
        "About to export: Public: " + isPublic + ", Anon: " + isAnon
        + " -- '" + clazz.getCanonicalName() + "'");

    if (!isAnon) {
      System.out.println("\tExporting: " + clazz.getCanonicalName());

      final File packagePath = new File(
          exportDirectory,
          convertPackageToPath(clazz.getPackage().getName()));

      packagePath.mkdirs();

      final File classFile = new File(
          packagePath,
          clazz.getSimpleName() + ".java");

      final String classContent = classTemplate.build(clazz);
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
