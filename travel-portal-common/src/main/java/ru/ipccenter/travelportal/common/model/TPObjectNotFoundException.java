package ru.ipccenter.travelportal.common.model;

import javax.ejb.ApplicationException;

/**
 * Created by Ivan on 12.03.2015.
 */
@ApplicationException(rollback = true)
public class TPObjectNotFoundException extends RuntimeException {

    public TPObjectNotFoundException(Throwable cause) {
        super(cause);
    }

    public TPObjectNotFoundException(String message) {
        super(message);
    }

    public TPObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    protected TPObjectNotFoundException(String message, Throwable cause,
                                  boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
