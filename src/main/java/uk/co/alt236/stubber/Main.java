package uk.co.alt236.stubber;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import uk.co.alt236.stubber.cli.CommandHelpPrinter;
import uk.co.alt236.stubber.cli.CommandLineOptions;
import uk.co.alt236.stubber.cli.OptionsBuilder;
import uk.co.alt236.stubber.resources.Strings;

import java.io.File;

public final class Main {

  private Main() {
    //NOOP
  }


  public static void main(String[] args) {
    final Strings strings = new Strings();
    final CommandLineOptions cliOptions = parseArgs(strings, args);

    if (cliOptions != null) {
      final Stubber stubber = new Stubber.Builder()
          .setDependencyDirectory(cliOptions.getDependencyDirectory())
          .setOutputDir(cliOptions.getOutputDirectory())
          .setTargetJar(cliOptions.getInputJar())
          .setBlowOnReturn(cliOptions.isBlowOnMethodCall())
          .build();

      stubber.stubIt();
    }
  }

  private static CommandLineOptions parseArgs(Strings strings, String[] args) {
    final CommandLineParser parser = new DefaultParser();
    final Options options = new OptionsBuilder(strings).compileOptions();
    final CommandLineOptions retVal;

    if (args.length == 0) {
      new CommandHelpPrinter(options, getJarName()).printHelp();
      retVal = null;
    } else {
      CommandLine line = null;

      try {
        line = parser.parse(options, args);
      } catch (final ParseException exp) {
        final String message = exp.getMessage();
        System.err.println(message);
        System.exit(1);
      }

      retVal = new CommandLineOptions(line);
    }

    return retVal;
  }

  private static String getJarName() {
    final File
        f =
        new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toString());
    return f.getName();
  }
}
