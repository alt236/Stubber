package uk.co.alt236.stubber;

import uk.co.alt236.stubber.exporters.v2.Containers2.FullClassListFactory;
import uk.co.alt236.stubber.exporters.v2.Exporter2;
import uk.co.alt236.stubber.util.StubberClassLoader;
import uk.co.alt236.stubber.util.validators.FileValidator;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class Stubber {

  private final URLClassLoader mJarClassLoader;
  private final String mAdditionalClasspath;
  private final String mTargetJarPath;
  private final String mTemplatePath;
  private final String outputDir;
  private final boolean blowOnReturn;

  private Stubber(final Builder builder) {
    mTemplatePath = builder.templateDir;
    mAdditionalClasspath = builder.dependenciesDir;
    mTargetJarPath = builder.targetJar;
    outputDir = builder.outputDir;
    blowOnReturn = builder.blowOnReturn;

    validate();

    mJarClassLoader = new StubberClassLoader(
        this.getClass().getClassLoader(),
        mTargetJarPath,
        mAdditionalClasspath);
  }

  private Class<?> getClass(final String className) {
    Class<?> c = null;

    try {
      c = Class.forName(className, false, mJarClassLoader);
    } catch (final ClassNotFoundException | NoClassDefFoundError e) {
      System.err.println("ERROR: " + e.getMessage());
      e.printStackTrace();
    }

    return c;
  }

  private List<Class<?>> getClasses() {
    final List<Class<?>> classSet = new ArrayList<>();

    try {
      final ZipInputStream zip = new ZipInputStream(new FileInputStream(mTargetJarPath));
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
          classSet.add(getClass(className.toString()));
        }
      }

      zip.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return classSet;
  }

  public void listJarContents() {
    final List<Class<?>> classSet = getClasses();
    for (final Class<?> clazz : classSet) {
      System.out.println("Class: " + clazz);
      final Method[] methods = clazz.getDeclaredMethods();

      for (final Method method : methods) {
        System.out.println("\t" + method);
      }
    }
  }

//  public void stubIt() {
//    final List<Class<?>> classArray = getClasses();
//    final List<ClassWrapper> myClassArray = WrapperFactory.getWrapper(classArray);
//
//    final Exporter exporterOld = new Exporter(
//        outputDir,
//        mTemplatePath,
//        blowOnReturn);
//
//    exporterOld.export(myClassArray);
//
//    System.out.println("  ----   DONE  -----");
//  }


  public void stubIt() {
    final FullClassListFactory factory = new FullClassListFactory();
    final List<Class<?>> classArray = getClasses();
    final Collection<Class<?>> myClassArray = factory.getAllClasses(classArray);

    final Exporter2 exporter2 = new Exporter2(
        outputDir,
        mTemplatePath,
        blowOnReturn);

    exporter2.export(myClassArray);

    System.out.println("  ----   DONE  -----");
  }

  private void validate() {
    final List<FileValidator> validators = new ArrayList<>();
    validators.add(new FileValidator("Template directory", mTemplatePath));
    validators.add(new FileValidator("Target Jar", mTargetJarPath));
    validators.add(new FileValidator("Output directory", outputDir));
    validators.add(new FileValidator("Dependencies path", mAdditionalClasspath, true));

    for (final FileValidator validator : validators) {
      validator.validate();
    }
  }

  public static class Builder {

    private String dependenciesDir;
    private String outputDir;
    private String templateDir;
    private String targetJar;
    private boolean blowOnReturn;

    /**
     * Will construct a {@link Stubber} with the settings of this builder.
     *
     * @return a constructed {@link Stubber}
     */
    public Stubber build() {
      return new Stubber(this);
    }

    /**
     * Set what happens when a stubbed method gets called. If set to TRUE then the stubbed method
     * will throw an {@link UnsupportedOperationException}. If set to FALSE then the method will do
     * nothing if the return type is void, or return the default value of primitive return types, or
     * null otherwise.
     *
     * @param value whether or not a stubbed method should blow up
     * @return this {@link uk.co.alt236.stubber.Stubber.Builder} object
     */
    public Builder setBlowOnReturn(final boolean value) {
      this.blowOnReturn = value;
      return this;
    }

    /**
     * Set the directory where any dependencies of the target Jar are located
     *
     * @param directory the directory where the dependencies are located
     * @return this {@link uk.co.alt236.stubber.Stubber.Builder} object
     */
    public Builder setDependencyDirectory(final String directory) {
      this.dependenciesDir = directory;
      return this;
    }

    /**
     * Set the directory where the created stub classes will be saved. This directory will be auto
     * deleted before each run.
     *
     * @param directory the target directory
     * @return this {@link uk.co.alt236.stubber.Stubber.Builder} object
     */
    public Builder setOutputDir(final String directory) {
      this.outputDir = directory;
      return this;
    }

    /**
     * Set the directory where the output file templates are located.
     *
     * @param directory the template directory
     * @return this {@link uk.co.alt236.stubber.Stubber.Builder} object
     */
    public Builder setOutputTemplateDir(final String directory) {
      this.templateDir = directory;
      return this;
    }

    /**
     * Set the location of the Jar file you want to stub.
     *
     * @param jar the target Jar file
     * @return this {@link uk.co.alt236.stubber.Stubber.Builder} object
     */
    public Builder setTargetJar(final String jar) {
      this.targetJar = jar;
      return this;
    }
  }
}
