package uk.co.alt236.stubber.exporters;

import uk.co.alt236.stubber.exporters.sections.FormatterFactory;
import uk.co.alt236.stubber.exporters.templates.PayloadFactory;
import uk.co.alt236.stubber.exporters.templates.factory.TemplateFactory;
import uk.co.alt236.stubber.exporters.templates.factory.TemplatePopulator;
import uk.co.alt236.stubber.resources.Templates;
import uk.co.alt236.stubber.util.FileIo;
import uk.co.alt236.stubber.util.Log;

import java.io.File;
import java.util.Collection;

public class Exporter2 {

  private final File exportDirectory;
  private final SourceFileWriter sourceFileWriter;

  public Exporter2(final File baseExportDirectory,
                   final Templates templates,
                   final boolean blowOnException) {
    final FormatterFactory formatterFactory = new FormatterFactory();
    final TemplatePopulator templatePopulator = new TemplatePopulator();
    final TemplateFactory templateFactory = new TemplateFactory(templates, templatePopulator);
    final PayloadFactory
        payloadFactory =
        new PayloadFactory(templateFactory, formatterFactory, blowOnException);

    exportDirectory = baseExportDirectory;
    sourceFileWriter = new SourceFileWriter(
        templateFactory,
        payloadFactory,
        ExportPathModifier.getSrcDirectory(exportDirectory));
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
    ExportPathModifier.getSrcDirectory(exportDirectory).mkdirs();
    ExportPathModifier.getBuildDirectory(exportDirectory).mkdirs();

    if (!exportDirectory.isDirectory()) {
      exportDirectory.delete();
      throw new IllegalStateException(exportDirectory.getAbsolutePath() + " is not a directory!");
    }
  }

  public void export(final Collection<Class<?>> classes) {
    cleanup();
    long count = 0;
    for (final Class<?> clazz : classes) {
      if (sourceFileWriter.writeToFile(clazz)) {
        count++;
      }
    }
    Log.out("Files Written: " + count);
  }
}
