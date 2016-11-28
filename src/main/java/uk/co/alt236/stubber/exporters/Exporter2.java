package uk.co.alt236.stubber.exporters;

import uk.co.alt236.stubber.exporters.template.ClassTemplate;
import uk.co.alt236.stubber.util.FileIo;

import java.io.File;
import java.util.Collection;

public class Exporter2 {

  private final File exportDirectory;
  private final FileWriter fileWriter;

  public Exporter2(final String exportDir,
                   final String templateDirectory,
                   final boolean blowOnException) {
    exportDirectory = new File(exportDir);
    fileWriter = new FileWriter(
        exportDirectory,
        new ClassTemplate(templateDirectory, blowOnException));
  }

  private void cleanup() {
    if (exportDirectory.exists()) {
      try {
        FileIo.deleteRecursively(exportDirectory);
      } catch (final Exception e) {
        System.out.println(e.getMessage());
      }
    }
    exportDirectory.mkdirs();

    if (!exportDirectory.isDirectory()) {
      exportDirectory.delete();
      throw new IllegalStateException(exportDirectory.getAbsolutePath() + " is not a directory!");
    }
  }

  public void export(final Collection<Class<?>> classes) {
    cleanup();
    long count = 0;
    for (final Class<?> clazz : classes) {
      if (fileWriter.writeToFile(clazz)) {
        count++;
      }
    }
    System.out.println("Files Written: " + count);
  }
}
