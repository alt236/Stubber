package uk.co.alt236.stubber.util;

import java.io.*;

public class FileIo {

    public static void delete(final File f) throws IOException {
        if (f.isDirectory()) {
            for (final File c : f.listFiles()) {
                delete(c);
            }
        }

        if (!f.delete()) {
            throw new IllegalStateException("Failed to delete file: " + f);
        }
    }

    public static String readFileAsString(final String filePath) throws java.io.IOException {
        final StringBuilder fileData = new StringBuilder(1000);
        final BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];

        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            final String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * Write to file.
     *
     * @param file    The target file
     * @param text    the text to write to the file.
     * @param append  the append
     * @param newline whether to append a newline at the end to the string.
     */
    public static void writeToFile(final File file, final String text, final boolean append, final boolean newline) {
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8");
            final BufferedWriter bw = new BufferedWriter(osw);
            bw.write(text);
            if (newline) {
                bw.newLine();
            }
            bw.close();
        } catch (final Exception e) {
            System.err.println("ERROR: Error when writing to '" + file + "': " + e.getMessage());
            e.printStackTrace();
        }

    }
}
