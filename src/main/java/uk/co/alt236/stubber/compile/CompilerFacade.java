package uk.co.alt236.stubber.compile;

import uk.co.alt236.stubber.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompilerFacade {

  private static final String JAVA_FILE_REGEX = ".*\\.java";

  private final CliExec cliExec;

  public CompilerFacade() {
    this.cliExec = new CliExec();
  }

  private static <T> T[] concat(T[] first, T[] second) {
    T[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }

  public int compile(final File sourceDir, final File buildDir) {
    final Path path = Paths.get(sourceDir.toURI());
    final List<File> javaFiles = FileFinder.findFiles(path, JAVA_FILE_REGEX);

    Log.out("Will compile java files in " + path);
    Log.out("Java Files: " + javaFiles.size());

    if (javaFiles.isEmpty()) {
      return 1;
    } else {
      final File file = writeFilesToFile(javaFiles);
      final JavacCommand command = new JavacCommand.Builder()
          .withBuildPath(buildDir.getAbsolutePath())
          .withSourceFiles("@" + file.getAbsolutePath())
          .withMaxErrors(9999)
          .withSourceCompatibility("8")
          .build();

      return cliExec.execute(command.toArgArray());
    }
  }

  private File writeFilesToFile(final List<File> javaFiles) {
    final List<String>
        lines = javaFiles
        .stream()
        .map(File::getAbsolutePath)
        .collect(Collectors.toList());

    try {
      final File
          file =
          File.createTempFile("stubber_" + System.currentTimeMillis(), ".txt");
      file.deleteOnExit();
      Files.write(Paths.get(file.toURI()), lines);

      return file;
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }
}
