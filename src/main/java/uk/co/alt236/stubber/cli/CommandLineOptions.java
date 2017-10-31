package uk.co.alt236.stubber.cli;

import org.apache.commons.cli.CommandLine;

public class CommandLineOptions {

  private final CommandLine commandLine;

  public CommandLineOptions(final CommandLine commandLine) {
    this.commandLine = commandLine;
  }

  public boolean isBlowOnMethodCall() {
    return commandLine.hasOption(OptionsBuilder.ARG_BLOW_ON_METHOD_CALL_LONG);
  }

  public String getOutputDirectory() {
    return commandLine.getOptionValue(OptionsBuilder.ARG_OUTPUT_LONG);
  }

  public String getDependencyDirectory() {
    return commandLine.getOptionValue(OptionsBuilder.ARG_DEPENDENCIES_LONG);
  }

  public String getInputJar() {
    return commandLine.getOptionValue(OptionsBuilder.ARG_INPUT_LONG);
  }
}
