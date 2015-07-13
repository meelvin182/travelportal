package ru.ipccenter.travelportal.common.model.objects;

/**
 * Created by meelvin182 on 05.03.15.
 */

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.bridge.annotations.Attribute;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.bridge.utils.AttributeExtractor;

import java.io.Serializable;
import java.math.BigInteger;

@ObjectType(id = "9223372036854775790",
    name = "user",
    attributes = {
        @Attribute(id = "9223372036854775788", name = "Password"),
        @Attribute(id = "9223372036854775791", name = "User"),
        @Attribute(id = "9223372036854775787", name = "Salt"),
        @Attribute(id = "9223372036854775786", name = "Role"),
})
public interface User extends TPObject, Serializable{
    static final BigInteger PASSWORD_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(User.class, "Password");
    static final BigInteger USER_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(Employee.class, "User");
    static final BigInteger SALT_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(User.class, "Salt");
    static final BigInteger ROLE_ATTRIBUTE = AttributeExtractor
            .extractAttributeId(User.class, "Role");

    String getPassword();
    void setPassword(String password);

    Employee getEmployee();

    String getSalt();
    void setSalt(String salt);

    Role getRole();
    BigInteger getRoleId();
    void setRoleId(BigInteger roleId);
}
