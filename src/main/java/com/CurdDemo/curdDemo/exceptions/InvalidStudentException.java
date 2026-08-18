package com.CurdDemo.curdDemo.exceptions;

public class InvalidStudentException extends RuntimeException {
    
    public InvalidStudentException(String message) {
        super(message);
    }

    public InvalidStudentException(String message, Throwable cause) {
        super(message, cause);
    }
}
