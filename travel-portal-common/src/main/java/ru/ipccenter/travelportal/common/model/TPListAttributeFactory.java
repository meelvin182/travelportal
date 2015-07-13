package ru.ipccenter.travelportal.common.model;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Anastasia on 17.03.2015.
 */
public interface TPListAttributeFactory {
    public <O extends TPListAttribute> O createListAttribute (BigInteger id, Class<O> clazz);
	public <O extends TPListAttribute> List<O> createAllListAttributes (Class<O> clazz);
}
