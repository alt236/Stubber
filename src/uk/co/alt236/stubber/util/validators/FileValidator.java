package uk.co.alt236.stubber.util.validators;

import java.io.File;

/**
 * Created by alex on 28/03/15.
 */
public class FileValidator {
    private final String path;
    private final boolean optional;
    private final String description;

    public FileValidator(final String description, final String path){
        this(description, path, false);
    }

    public FileValidator(final String description, final String path, final boolean optional){
        this.path = path;
        this.description = description;
        this.optional = optional;
    }

    public void validate(){
        if(path == null){
            if(optional){
                return;
            } else {
                throw new IllegalArgumentException(description + " -- Path cannot be null!");
            }
        }

        final File file = new File(path);

        if(!optional){
            if(!file.exists()){
                throw new IllegalArgumentException(description + " -- File '" + path + "' does not exist");
            }
        }
    }
}
