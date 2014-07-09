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
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Stubber {
	final URLClassLoader mJarClassLoader;
	final private String mTargetJarPath;
	final private String mAdditionalClasspath;


	private URL[] getAllJarsInDirectory(String path){
		final File folder = new File(path);
		final File[] listOfFiles = folder.listFiles();
		final List<URL> listOfJars = new ArrayList<URL>();

		for (File f : listOfFiles){
			if (f.isFile()) {
				if(f.getName().toLowerCase(Locale.US).endsWith("jar")){
					System.out.println("File " + f.getName());
					listOfJars.add(getUrl(f));
				}

			}
		}

		return listOfJars.toArray(new URL[0]);
	}

	@SuppressWarnings({ "deprecation" })
	private URL getUrl(File f){
		try {
			return f.toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public Stubber(String additionalJars, String targetJar) {
		mAdditionalClasspath = additionalJars;

		mTargetJarPath = additionalJars + targetJar;

		mJarClassLoader = new URLClassLoader(
				getAllJarsInDirectory(mAdditionalClasspath),
				this.getClass().getClassLoader());
	}

	private Method[] getClassMethods(String className) {
		Class<?> c;
		try {
			c = Class.forName(className, false, mJarClassLoader);
			return c.getDeclaredMethods();
		} catch (ClassNotFoundException e) {
			System.err.println("ERROR: " + e.getMessage());
			return new Method[0];
		} catch (NoClassDefFoundError e){
			System.err.println("ERROR: " + e.getMessage());
			return new Method[0];
		}
	}

	private Set<String> getClasses() {
		final Set<String> classSet = new TreeSet<String>();

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
					classSet.add(className.toString());
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
		final Set<String> classSet = getClasses();
		for(final String className : classSet){
			System.out.println(className);
			final Method[] methods = getClassMethods(className);

			for(final Method method : methods){
				System.out.println("\t" + method);
			}
		}
	}
}
