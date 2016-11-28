package uk.co.alt236.stubber.exporters.v1;

import uk.co.alt236.stubber.exporters.v1.containers.ClassWrapper;
import uk.co.alt236.stubber.exporters.v1.template.ClassTemplate;
import uk.co.alt236.stubber.util.FileIo;
import uk.co.alt236.stubber.util.ReflectionUtils.Exposure;

import java.io.File;
import java.util.List;

public class Exporter {

  private final File mExportDirectory;
  private final ClassTemplate classTemplate;

  public Exporter(final String exportDir,
                  final String templateDirectory,
                  final boolean blowOnException) {
    mExportDirectory = new File(exportDir);
    classTemplate = new ClassTemplate(templateDirectory, blowOnException);
  }

  private static String convertPackageToPath(final String packageName) {
    return packageName.replace('.', File.separatorChar) + File.separatorChar;
  }

  private void cleanup() {
    if (mExportDirectory.exists()) {
      try {
        FileIo.deleteRecursively(mExportDirectory);
      } catch (final Exception e) {
        System.out.println(e.getMessage());
      }
    }
    mExportDirectory.mkdirs();

    if (!mExportDirectory.isDirectory()) {
      mExportDirectory.delete();
      throw new IllegalStateException(mExportDirectory.getAbsolutePath() + " is not a directory!");
    }
  }

  public void export(final List<ClassWrapper> classes) {
    cleanup();
    long count = 0;
    for (final ClassWrapper clazz : classes) {
      if (writeToFile(clazz)) {
        count++;
      }
    }
    System.out.println("Files Written: " + count);
  }

  private boolean writeToFile(final ClassWrapper clazz) {
    final boolean isPublic = clazz.getExposure() == Exposure.PUBLIC;
    if (!isPublic) {
      return false;
    }

    final boolean isInner = clazz.isInnerClass();
    final boolean isAnon = clazz.isAnonymousClass();

    System.out.println(
        "About to export: Public: " + isPublic + ", Inner: " + isInner + ", Anon: " + isAnon
        + " -- '" + clazz.getCanonicalName() + "'");

    if (!isInner && !isAnon && isPublic) {
      System.out.println("\tExporting: " + clazz.getCanonicalName());

      final File packagePath = new File(
          mExportDirectory,
          convertPackageToPath(clazz.getPackageName()));

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
