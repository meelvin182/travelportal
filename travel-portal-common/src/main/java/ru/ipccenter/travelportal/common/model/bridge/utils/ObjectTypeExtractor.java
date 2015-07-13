package ru.ipccenter.travelportal.common.model.bridge.utils;

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;

import java.math.BigInteger;

/**
 * Created by Ivan on 26.04.2015.
 */
public class ObjectTypeExtractor {

    public static BigInteger extractObjectTypeId(Class<? extends TPObject> clazz) {
        ObjectType objectType = clazz.getAnnotation(ObjectType.class);
        return new BigInteger(objectType.id());
    }
}
