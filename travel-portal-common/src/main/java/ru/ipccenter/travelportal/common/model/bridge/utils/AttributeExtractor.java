package ru.ipccenter.travelportal.common.model.bridge.utils;

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.bridge.annotations.Attribute;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;

import java.math.BigInteger;

/**
 * Created by Ivan on 26.04.2015.
 */
public class AttributeExtractor {

    public static BigInteger extractAttributeId(Class<? extends TPObject> clazz, String attributeName) {
        ObjectType objectType = clazz.getAnnotation(ObjectType.class);
        for(Attribute attribute: objectType.attributes()) {
            String annotatinonAttrName = NameConverter.convert(attribute.name());
            String inputAttrName = NameConverter.convert(attributeName);
            if (inputAttrName.equals(annotatinonAttrName)) {
                return new BigInteger(attribute.id());
            }
        }

        return null;
    }
}
