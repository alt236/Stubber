package uk.co.alt236.stubber.exporters;

import uk.co.alt236.stubber.exporters.sections.FormatterFactory;
import uk.co.alt236.stubber.exporters.templates.TemplateFactory;
import uk.co.alt236.stubber.util.FileIo;
import uk.co.alt236.stubber.util.Log;

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
        TemplateFactory.create(templateDirectory, new FormatterFactory(), blowOnException));
  }

  private void cleanup() {
    if (exportDirectory.exists()) {
      try {
        FileIo.deleteRecursively(exportDirectory);
      } catch (final Exception e) {
        Log.err(e.getMessage());
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
    Log.out("Files Written: " + count);
  }
}
