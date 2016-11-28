package uk.co.alt236.stubber.jar.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarClassParser {

  private final String jarFile;
  private final ClassLoader classLoader;

  public JarClassParser(final ClassLoader classLoader,
                        final String jarFile) {
    this.jarFile = jarFile;
    this.classLoader = classLoader;
  }
  private static final Comparator<Class<?>> COMPARATOR =
      (o1, o2) -> o1.getCanonicalName().compareTo(o2.getCanonicalName());

  private Class<?> getClass(final String className) {
    Class<?> c = null;

    try {
      c = Class.forName(className, false, classLoader);
    } catch (final ClassNotFoundException | NoClassDefFoundError e) {
      System.err.println("ERROR: " + e.getMessage());
      e.printStackTrace();
    }

    return c;
  }

  public Collection<Class<?>> getClasses() {
    return getClassesInternal();
  }

  private Collection<Class<?>> getClassesInternal() {
    final Set<Class<?>> retVal = new TreeSet<>(COMPARATOR);

    try {
      final ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile));
      for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
        if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
          // This ZipEntry represents a class. Now, what class does it
          // represent?
          final StringBuilder className = new StringBuilder();
          for (final String part : entry.getName().split("/")) {
            if (className.length() != 0) {
              className.append(".");
            }

            className.append(part);
            if (part.endsWith(".class")) {
              className.setLength(className.length() - ".class".length());
            }
          }
          retVal.add(getClass(className.toString()));
        }
      }

      zip.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return retVal;
  }
}
