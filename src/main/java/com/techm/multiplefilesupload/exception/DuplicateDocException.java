package com.techm.multiplefilesupload.exception;

import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

public class DuplicateDocException extends RuntimeException {
    public DuplicateDocException(String string, HttpClientErrorException ex) {

        super(string, ex);
    }
    public DuplicateDocException(String string, IOException ex) {

        super(string, ex);
    }
    public DuplicateDocException(String string) {
        super(string);
    }
}
