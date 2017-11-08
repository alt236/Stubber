package uk.co.alt236.stubber.compile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class FileFinder {

  private static void filterFiles(final Path path,
                                  final Pattern pattern,
                                  final List<File> result) throws IOException {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
      for (Path entry : stream) {
        if (Files.isDirectory(entry)) {
          filterFiles(entry, pattern, result);
        } else {
          if (pattern.matcher(entry.toString()).matches()) {
            result.add(entry.toFile());
          }
        }
      }
    }
  }

  public static List<File> findFiles(final Path path,
                                     final String regex) {
    final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    final List<File> files = new ArrayList<>();

    try {
      filterFiles(path, pattern, files);
      return files;
    } catch (IOException x) {
      throw new RuntimeException(String.format("Error reading folder %s: %s",
                                               path,
                                               x.getMessage()), x);
    }
  }
}
