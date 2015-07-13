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
@ObjectType(id = "9223372036854775758",
    name = "position",
    attributes = {
        @Attribute(id = "9223372036854775799", name = "Department")
})
public interface Position extends TPObject, Serializable {
    static final BigInteger DEPARTMENT_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Position.class, "Department");

    Department getDepartment();
    BigInteger getDepartmentId();
    void setDepartmentId(BigInteger departmentId);
}
