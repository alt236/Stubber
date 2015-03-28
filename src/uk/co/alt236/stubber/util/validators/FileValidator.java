package uk.co.alt236.stubber.util.validators;

import java.io.File;

/**
 * Created by alex on 28/03/15.
 */
public class FileValidator {
    private final String path;
    private final boolean optional;

    public FileValidator(final String path, final boolean optional){
        this.path = path;
        this.optional = optional;
    }

    public void validate(){
        final File file = new File(path);

        if(!optional){
            if(!file.exists()){
                throw new IllegalArgumentException("File '" + path + "' does not exist");
            }
        }
    }
}
