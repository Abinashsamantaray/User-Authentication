package com.project.User_Authentication.Exception;

@SuppressWarnings("serial")
public class InvalidOTPException extends Exception {
    public InvalidOTPException(String message) {
        super(message);
    }
}