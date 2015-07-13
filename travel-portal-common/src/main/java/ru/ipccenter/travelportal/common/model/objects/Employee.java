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
@ObjectType(id = "9223372036854775798",
    name = "employee",
    attributes = {
        @Attribute(id = "9223372036854775796", name = "Last Name"),
        @Attribute(id = "9223372036854775795", name = "Office"),
        @Attribute(id = "9223372036854775799", name = "Department"),
        @Attribute(id = "9223372036854775794", name = "Manager"),
        @Attribute(id = "9223372036854775793", name = "Position"),
        @Attribute(id = "9223372036854775792", name = "Email"),
        @Attribute(id = "9223372036854775791", name = "User"),
})
public interface Employee extends TPObject, Serializable {
    static final BigInteger LASTNAME_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Employee.class, "Last Name");
    static final BigInteger OFFICE_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Employee.class, "Office");
    static final BigInteger DEPARTMENT_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Employee.class, "Department");
    static final BigInteger MANAGER_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Employee.class, "Manager");
    static final BigInteger POSITION_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Employee.class, "Position");
    static final BigInteger EMAIL_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Employee.class, "Email");
    static final BigInteger USER_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Employee.class, "User");

    List<Employee> getChildren();

    String getLastName();
    void setLastName(String lastName);

    Office getOffice();
    BigInteger getOfficeId();
    void setOfficeId(BigInteger officeId);

    Department getDepartment();
    BigInteger getDepartmentId();
    void setDepartmentId(BigInteger departmentId);

    Employee getManager();
    BigInteger getManagerId();
    void setManagerId(BigInteger managerId);

    Position getPosition();
    BigInteger getPositionId();
    void setPositionId(BigInteger positionId);

    String getEmail();
    void setEmail(String email);

    User getUser();
    BigInteger getUserId();
    void setUserId(BigInteger userId);
}
