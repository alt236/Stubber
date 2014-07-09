import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import util.ReflectionUtils;
import containers.ClassWrapper;
import exporters.ClassExporter;

public class Stubber {
	final private String mAdditionalClasspath;
	final private URLClassLoader mJarClassLoader;
	final private String mTargetJarPath;
	final private String mTemplatePath;

	public Stubber(String jarDirectory, String templateDir, String targetJar) {
		mTemplatePath = templateDir;
		mAdditionalClasspath = jarDirectory;
		mTargetJarPath = jarDirectory + targetJar;

		mJarClassLoader = new URLClassLoader(
				getAllJarsInDirectory(mAdditionalClasspath),
				this.getClass().getClassLoader());
	}

	private URL[] getAllJarsInDirectory(String path){
		final File folder = new File(path);
		final File[] listOfFiles = folder.listFiles();
		final List<URL> listOfJars = new ArrayList<>();

		for (final File f : listOfFiles){
			if (f.isFile()) {
				if(f.getName().toLowerCase(Locale.US).endsWith("jar")){
					System.out.println("File " + f.getName());
					listOfJars.add(getUrl(f));
				}

			}
		}

		return listOfJars.toArray(new URL[0]);
	}

	private Class<?> getClass(String className){
		Class<?> c = null;

		try {
			c = Class.forName(className, false, mJarClassLoader);
			System.out.println(c);
		} catch (ClassNotFoundException e) {
			System.err.println("ERROR: " + e.getMessage());
		} catch (NoClassDefFoundError e){
			System.err.println("ERROR: " + e.getMessage());
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

	@SuppressWarnings({ "deprecation" })
	private URL getUrl(File f){
		try {
			return f.toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public void listJarContents() {
		final List<Class<?>> classSet = getClasses();
		for(final Class<?> clazz : classSet){
			System.out.println(clazz);
			final Method[] methods = clazz.getDeclaredMethods();

			for(final Method method : methods){
				System.out.println("\t" + method);
			}
		}
	}

	public void stubIt(){
		final List<Class<?>> classArray = getClasses();
		final List<ClassWrapper> myClassArray =
				ReflectionUtils.getWrapper(classArray);

		for(final Class<?> clazz : classArray){
			System.out.println(clazz);
			myClassArray.add(new ClassWrapper(clazz));
		}

		final ClassExporter exporter = new ClassExporter(
				mAdditionalClasspath + "/export/",
				mTemplatePath);

		exporter.export(myClassArray);
	}
}
