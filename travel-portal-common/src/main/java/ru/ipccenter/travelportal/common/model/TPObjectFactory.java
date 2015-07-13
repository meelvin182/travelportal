package ru.ipccenter.travelportal.common.model;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Ivan on 12.03.2015.
 */
public interface TPObjectFactory extends Serializable {
    public <O extends TPObject> O createObject(Class<O> clazz);
    public <O extends TPObject> O createObject(BigInteger id, Class<O> clazz);
}
