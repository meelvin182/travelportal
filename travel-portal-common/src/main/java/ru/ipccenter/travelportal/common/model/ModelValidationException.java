package ru.ipccenter.travelportal.common.model;

/**
 * Created by Ivan on 26.04.2015.
 */
public class ModelValidationException extends RuntimeException {
    public ModelValidationException() {
    }

    public ModelValidationException(String message) {
        super(message);
    }

    public ModelValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelValidationException(Throwable cause) {
        super(cause);
    }

    public ModelValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
