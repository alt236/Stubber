package uk.co.alt236.stubber.resources;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Templates {
  private final ClassLoader classLoader;

  public Templates() {
    classLoader = getClass().getClassLoader();
  }

  public String getClassFileTemplate() {
    return readResource("templates/class_file.template");
  }

  public String getInnerClassFileTemplate() {
    return readResource("templates/inner_class.template");
  }


  private String readResource(String fileName) {
    final StringBuilder result = new StringBuilder("");
    final File file = new File(classLoader.getResource(fileName).getFile());

    try (Scanner scanner = new Scanner(file)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        result.append(line).append("\n");
      }

      scanner.close();

    } catch (IOException e) {
      throw new IllegalArgumentException("Could not open file: " + fileName);
    }

    return result.toString();

  }
}
