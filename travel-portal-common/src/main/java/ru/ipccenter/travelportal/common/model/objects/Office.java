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
@ObjectType(id = "9223372036854775783",
    name = "office",
    attributes = {
        @Attribute(id = "9223372036854775781", name = "Address"),
        @Attribute(id = "9223372036854775782", name = "City")
})
public interface Office extends TPObject, Serializable {
    static final BigInteger ADDRESS_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Office.class, "Address");
    static final BigInteger CITY_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Office.class, "City");

    City getCity();
    BigInteger getCityId() ;
    void  setCityId(BigInteger cityId) ;

    String getAddress() ;
    void  setAddress(String address) ;
}
