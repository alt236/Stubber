package uk.co.alt236.stubber;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import uk.co.alt236.stubber.util.ReflectionUtils;
import uk.co.alt236.stubber.containers.ClassWrapper;
import uk.co.alt236.stubber.exporters.ClassExporter;
import uk.co.alt236.stubber.util.StubberClassLoader;
import uk.co.alt236.stubber.util.validators.FileValidator;

public class Stubber {

	final private URLClassLoader mJarClassLoader;
	final private String mAdditionalClasspath;
	final private String mTargetJarPath;
	final private String mTemplatePath;
	final private String outputDir;

	private Stubber(Builder builder) {
		mTemplatePath = builder.templateDir;
		mAdditionalClasspath = builder.dependenciesDir;
		mTargetJarPath =  builder.targetJar;
		outputDir = builder.outputDir;

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

		for(final FileValidator validator : validators){
			validator.validate();
		}
	}

	private Class<?> getClass(String className){
		Class<?> c = null;

		try {
			c = Class.forName(className, false, mJarClassLoader);
		} catch (ClassNotFoundException e) {
			System.err.println("ERROR: " + e.getMessage());
			e.printStackTrace();
		} catch (NoClassDefFoundError e){
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
						if (className.length() != 0){
							className.append(".");
						}

						className.append(part);
						if (part.endsWith(".class")){
							className.setLength(className.length() - ".class".length());
						}
					}
					classSet.add(getClass(className.toString()));
				}

			zip.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classSet;
	}



	public void listJarContents() {
		final List<Class<?>> classSet = getClasses();
		for(final Class<?> clazz : classSet){
			System.out.println("Class: " + clazz);
			final Method[] methods = clazz.getDeclaredMethods();

			for(final Method method : methods){
				System.out.println("\t" + method);
			}
		}
	}

	public void stubIt(){
		final List<Class<?>> classArray = getClasses();
		final List<ClassWrapper> myClassArray = ReflectionUtils.getWrapper(classArray);

		for(final Class<?> clazz : classArray){
			myClassArray.add(new ClassWrapper(clazz));
		}

		final ClassExporter exporter = new ClassExporter(
				outputDir,
				mTemplatePath);

		exporter.export(myClassArray);

		System.out.println("  ----   DONE  -----");
	}

	public static class Builder {
        private String dependenciesDir;
		private String outputDir;
        private String templateDir;
        private String targetJar;

        public Builder setDependencyDirectory(String directory) {
            this.dependenciesDir = directory;
            return this;
        }

		public Builder setOutputDir(String directory) {
			this.outputDir = directory;
			return this;
		}

        public Builder setOutputTemplateDir(String directory) {
            this.templateDir = directory;
            return this;
        }

        public Builder setTargetJar(String jar) {
            this.targetJar = jar;
            return this;
        }

        public Stubber build() {
            return new Stubber(this);
        }
    }
}
