package ru.ipccenter.travelportal.common.model.objects;

/**
 * Created by meelvin182 on 05.03.15.
 */

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.attributes.Tab;
import ru.ipccenter.travelportal.common.model.bridge.annotations.Attribute;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.bridge.utils.AttributeExtractor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@ObjectType(id = "9223372036854775756",
    name = "role",
    attributes = {
        @Attribute(id = "9223372036854775750", name = "Tabs")
})
public interface Role extends TPObject, Serializable {
    public static final BigInteger TABS_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Role.class, "Tabs");
    public List<Tab> getTabs();
}
