package com.kunwoo.holiday_test.external.nager;

public class ExternalApiUnavailableException extends RuntimeException {
    public ExternalApiUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}