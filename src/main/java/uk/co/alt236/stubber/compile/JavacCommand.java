package uk.co.alt236.stubber.compile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavacCommand {

  private static final List<String>
      COMMAND =
      Arrays.asList("javac", "-source", "8", "-Xmaxerrs", "9999");

  private final String sourceCompatibility;
  private final String buildPath;
  private final int maxErrors;
  private final String sourceFiles;

  private JavacCommand(final Builder builder) {
    sourceCompatibility = builder.sourceCompatibility;
    buildPath = builder.buildPath;
    maxErrors = builder.maxErrors;
    sourceFiles = builder.sourceFiles;
  }

  public String[] toArgArray() {
    final List<String> list = toArgList();
    return list.toArray(new String[list.size()]);
  }

  public List<String> toArgList() {
    final List<String> list = new ArrayList<>();
    list.add("javac");

    if (maxErrors > 0) {
      list.add("-Xmaxerrs");
      list.add(String.valueOf(maxErrors));
    }

    if (sourceCompatibility != null) {
      list.add("-source");
      list.add(sourceCompatibility);
    }

    if (buildPath != null) {
      list.add("-d");
      list.add(buildPath);
    }

    list.add(sourceFiles);

    return list;
  }

  public static final class Builder {

    private String sourceCompatibility;
    private String buildPath;
    private int maxErrors;
    private String sourceFiles;

    public Builder() {
    }

    public JavacCommand build() {
      return new JavacCommand(this);
    }

    public Builder withBuildPath(final String val) {
      buildPath = val;
      return this;
    }

    public Builder withMaxErrors(final int val) {
      maxErrors = val;
      return this;
    }

    public Builder withSourceCompatibility(final String val) {
      sourceCompatibility = val;
      return this;
    }

    public Builder withSourceFiles(final String val) {
      sourceFiles = val;
      return this;
    }
  }
}
