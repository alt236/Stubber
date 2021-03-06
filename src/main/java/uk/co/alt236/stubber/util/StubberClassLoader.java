package uk.co.alt236.stubber.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StubberClassLoader extends URLClassLoader {

    public StubberClassLoader(final ClassLoader classLoader, final String... jarPaths) {
        super(getAllJars(jarPaths), classLoader);
    }

    private static URL[] getAllJars(final String... paths) {
        final List<URL> listOfJars = new ArrayList<>();

        for (final String path : paths) {
            if (path != null) {
                final File f = new File(path);
                if (f.isDirectory()) {
                    final File[] listOfFiles = f.listFiles();

                    for (final File file : listOfFiles) {
                        if (file.isFile()) {
                            if (isJarFile(file)) {
                                Log.out("File " + file.getName());
                                listOfJars.add(getUrl(file));
                            }
            }
                    }
                } else {
                    if (isJarFile(f)) {
                        Log.out("File " + f.getName());
                        listOfJars.add(getUrl(f));
                    }
        }
            }
    }

        return listOfJars.toArray(new URL[listOfJars.size()]);
    }

    @SuppressWarnings({"deprecation"})
    private static URL getUrl(final File f) {
        try {
            return f.toURL();
        } catch (final MalformedURLException e) {
            return null;
    }
    }

    private static boolean isJarFile(final File file) {
        return file.getName().toLowerCase(Locale.US).endsWith("jar");
    }
}
