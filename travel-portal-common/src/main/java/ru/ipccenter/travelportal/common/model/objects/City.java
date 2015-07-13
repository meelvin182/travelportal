package ru.ipccenter.travelportal.common.model.objects;

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.bridge.annotations.Attribute;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.bridge.utils.AttributeExtractor;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by meelvin182 on 05.03.15.
 */
@ObjectType(id = "9223372036854775780",
    name = "city",
    attributes = {
        @Attribute(id = "9223372036854775779", name = "Country")
})
public interface City extends TPObject, Serializable {
    static final BigInteger ATTR_ID_COUNTRY = AttributeExtractor.extractAttributeId(City.class, "Country");

    Country getCountry();
    BigInteger getCountryId();
    void setCountryId(BigInteger countryId);
}
