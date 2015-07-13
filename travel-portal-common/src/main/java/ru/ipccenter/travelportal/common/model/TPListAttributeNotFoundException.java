package ru.ipccenter.travelportal.common.model;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class TPListAttributeNotFoundException extends RuntimeException {
    public TPListAttributeNotFoundException(Throwable cause) {
        super(cause);
    }

    public TPListAttributeNotFoundException(String message) {
        super(message);
    }

    public TPListAttributeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    protected TPListAttributeNotFoundException(String message, Throwable cause,
                                        boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
