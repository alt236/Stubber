package uk.co.alt236.stubber.compile;

import uk.co.alt236.stubber.util.Log;

import java.io.IOException;
import java.util.Arrays;

/*package*/ class CliExec {

  public int execute(final String[] command) {
    Log.out("Will execute : " + Arrays.toString(command));
    try {
      ProcessBuilder pb = new ProcessBuilder(command);
      pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
      pb.redirectError(ProcessBuilder.Redirect.INHERIT);
      final int res = pb.start().waitFor();

      Log.out("Result: " + res);
      return res;
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

}
