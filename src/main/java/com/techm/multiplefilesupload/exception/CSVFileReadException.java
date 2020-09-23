package com.techm.multiplefilesupload.exception;

import java.io.FileNotFoundException;

public class CSVFileReadException extends RuntimeException {
    public CSVFileReadException(String string, FileNotFoundException ex) {
        super(string, ex);
    }
    public CSVFileReadException(String string) {
        super(string);
    }
}