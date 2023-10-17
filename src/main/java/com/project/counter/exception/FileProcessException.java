package com.project.counter.exception;

public class FileProcessException extends Exception {
    public FileProcessException() {
    }

    public FileProcessException(String message) {
        super(message);
    }

    public FileProcessException(Throwable cause) {
        super(cause);
    }

    public FileProcessException(String message, Throwable cause) {
        super(message, cause);
    }


}
