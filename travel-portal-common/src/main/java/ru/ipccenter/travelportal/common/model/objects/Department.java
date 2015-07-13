package ru.ipccenter.travelportal.common.model.objects;

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.bridge.annotations.Attribute;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.bridge.utils.AttributeExtractor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by meelvin182 on 05.03.15.
 */
@ObjectType(id = "9223372036854775785",
    name = "department",
    attributes = {
        @Attribute(id = "9223372036854775784", name = "Manager")
})
public interface Department extends TPObject, Serializable {
    static final BigInteger MANAGER_ATTRIBUTE = AttributeExtractor.
            extractAttributeId(Department.class, "Manager");

    List<BigInteger> getChildIds();
    List<Department> getChildren();

    BigInteger getManagerId();
    Employee getManager();
    void setManagerId(BigInteger managerId);
}
