package ru.ipccenter.travelportal.security;

import ru.ipccenter.travelportal.common.model.objects.Role;
import ru.ipccenter.travelportal.common.model.objects.User;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by Vadik on 05/04/15.
 */
public interface UserService {

    public BigInteger getUserId(String userName);
    public User getUser(String userName);
    public String getPasswordHash(String userName);
    public String getSalt(String userName);
    public Role getRole(String userName);
}