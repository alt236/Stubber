package uk.co.alt236.stubber.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import uk.co.alt236.stubber.resources.Strings;

public class OptionsBuilder {

  /*package*/ static final String ARG_OUTPUT = "o";
  /*package*/ static final String ARG_OUTPUT_LONG = "output";

  /*package*/ static final String ARG_INPUT = "i";
  /*package*/ static final String ARG_INPUT_LONG = "input";

  /*package*/ static final String ARG_DEPENDENCIES = "d";
  /*package*/ static final String ARG_DEPENDENCIES_LONG = "dependencies";

  /*package*/ static final String ARG_BLOW_ON_METHOD_CALL = "b";
  /*package*/ static final String ARG_BLOW_ON_METHOD_CALL_LONG = "blowup";

  private final Strings strings;

  public OptionsBuilder(Strings strings) {
    this.strings = strings;
  }

  public Options compileOptions() {
    final Options options = new Options();

    options.addOption(createOptionInputJar());
    options.addOption(createOptionOutputLocation());
    options.addOption(createOptionDependenciesLocation());
    options.addOption(createOptionBlowOnMethodCall());

    return options;
  }

  private Option createOptionOutputLocation() {
    final String desc = strings.getString("cli_cmd_output_location");
    return Option.builder(ARG_OUTPUT)
        .longOpt(ARG_OUTPUT_LONG)
        .hasArg()
        .required(true)
        .desc(desc)
        .build();
  }

  private Option createOptionInputJar() {
    final String desc = strings.getString("cli_cmd_input_jar");
    return Option.builder(ARG_INPUT)
        .longOpt(ARG_INPUT_LONG)
        .hasArg()
        .required(true)
        .desc(desc)
        .build();
  }

  private Option createOptionDependenciesLocation() {
    final String desc = strings.getString("cli_cmd_input_dependencies_dir");
    return Option.builder(ARG_DEPENDENCIES)
        .longOpt(ARG_DEPENDENCIES_LONG)
        .hasArg()
        .required(false)
        .desc(desc)
        .build();
  }

  private Option createOptionBlowOnMethodCall() {
    final String desc = strings.getString("cli_cmd_blow_on_calling_method");
    return Option.builder(ARG_BLOW_ON_METHOD_CALL)
        .longOpt(ARG_BLOW_ON_METHOD_CALL_LONG)
        .hasArg(false)
        .required(false)
        .desc(desc)
        .build();
  }
}
