package ru.ipccenter.travelportal.services;

import java.math.BigInteger;

/**
 * Created by Ivan on 15.05.2015.
 */
public abstract class AbstractByIdFactory<T> {
    public abstract T create(BigInteger id);
}
