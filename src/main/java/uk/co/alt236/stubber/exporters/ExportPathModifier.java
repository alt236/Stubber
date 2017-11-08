package uk.co.alt236.stubber.exporters;

import java.io.File;

public final class ExportPathModifier {

  private static final String SRC_DIR = "src" + File.separator;
  private static final String BUILD_DIR = "build" + File.separator;

  public static File getBuildDirectory(final File baseExportDirectory) {
    return new File(baseExportDirectory, BUILD_DIR);
  }

  public static File getSrcDirectory(final File baseExportDirectory) {
    return new File(baseExportDirectory, SRC_DIR);
  }
}
