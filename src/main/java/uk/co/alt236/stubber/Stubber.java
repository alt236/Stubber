package uk.co.alt236.stubber;

import uk.co.alt236.stubber.containers.ClassWrapper;
import uk.co.alt236.stubber.exporters.Exporter;
import uk.co.alt236.stubber.util.StubberClassLoader;
import uk.co.alt236.stubber.util.WrapperFactory;
import uk.co.alt236.stubber.util.validators.FileValidator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Stubber {

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

    private Class<?> getClass(final String className) {
        Class<?> c = null;

        try {
            c = Class.forName(className, false, mJarClassLoader);
        } catch (final ClassNotFoundException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        } catch (final NoClassDefFoundError e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        return c;
    }

    private List<Class<?>> getClasses() {
        final List<Class<?>> classSet = new ArrayList<>();

        try {
            final ZipInputStream zip = new ZipInputStream(new FileInputStream(mTargetJarPath));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
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

            zip.close();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
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

    public void stubIt() {
        final List<Class<?>> classArray = getClasses();
        final List<ClassWrapper> myClassArray = WrapperFactory.getWrapper(classArray);

        for (final Class<?> clazz : classArray) {
            myClassArray.add(new ClassWrapper(clazz));
        }

        final Exporter exporter = new Exporter(
                outputDir,
                mTemplatePath,
                blowOnReturn);

        exporter.export(myClassArray);

        System.out.println("  ----   DONE  -----");
    }

    public static class Builder {
        private String dependenciesDir;
        private String outputDir;
        private String templateDir;
        private String targetJar;
        private boolean blowOnReturn;

        public Builder setDependencyDirectory(final String directory) {
            this.dependenciesDir = directory;
            return this;
        }

        public Builder setOutputDir(final String directory) {
            this.outputDir = directory;
            return this;
        }

        public Builder setOutputTemplateDir(final String directory) {
            this.templateDir = directory;
            return this;
        }

        public Builder setTargetJar(final String jar) {
            this.targetJar = jar;
            return this;
        }

        public Builder setBlowOnReturn(final boolean value) {
            this.blowOnReturn = value;
            return this;
        }

        public Stubber build() {
            return new Stubber(this);
        }
    }
}
