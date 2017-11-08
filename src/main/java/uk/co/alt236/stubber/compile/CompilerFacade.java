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
  private static final List<String>
      COMMAND =
      Arrays.asList("javac", "-source", "8", "-Xmaxerrs", "9999");
  private final List<String> javacCommand;
  private final CliExec cliExec;

  public CompilerFacade() {
    this.javacCommand = COMMAND;
    this.cliExec = new CliExec();
  }

  private static <T> T[] concat(T[] first, T[] second) {
    T[] result = Arrays.copyOf(first, first.length + second.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }

  public int compile(final String sourceDir) {
    final Path path = Paths.get(sourceDir + File.separator);
    final List<File> javaFiles = FileFinder.findFiles(path, JAVA_FILE_REGEX);

    Log.out("Will compile java files in " + path);
    Log.out("Java Files: " + javaFiles.size());

    if (javaFiles.isEmpty()) {
      return 1;
    } else {
      final File file = writeFilesToFile(javaFiles);
      final String[] command = concat(javacCommand.toArray(
          new String[javacCommand.size()]),
                                      new String[]{"@" + file.getAbsolutePath()});

      return cliExec.execute(command);
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
