package ru.ipccenter.travelportal.ejb.stateful;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.DataAccessException;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.common.model.objects.Role;
import ru.ipccenter.travelportal.common.model.objects.User;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.ejb.*;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by Ivan on 12.03.2015.
 */
@Stateful
@Local(User.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserBean extends AbstractTPObject implements User {
    private static final Logger LOG = Logger.getLogger(UserBean.class);

    public static final BigInteger EMPLOYEE_OBJECT_TYPE_ID = new BigInteger(
            Employee.class.getAnnotation(ObjectType.class).id());

    @EJB
    private JdbcTemplate jdbcTemplate;

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public BigInteger getId() {
        return super.getId();
    }

    @Override
    @SetOnce
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void setId(BigInteger id) {
        super.setId(id);
    }

    @Override
    public String getPassword() {
        return cacheProvider.provide().getParameter(getId(), PASSWORD_ATTRIBUTE).getValue();
    }

    @Override
    public void setPassword(String Password) {
        cacheProvider.provide().getParameter(getId(), PASSWORD_ATTRIBUTE).setValue(Password);
    }

    @Override
    public String getSalt() {
        return cacheProvider.provide().getParameter(getId(), SALT_ATTRIBUTE).getValue();
    }

    @Override
    public void setSalt(String salt) {
        cacheProvider.provide().getParameter(getId(), SALT_ATTRIBUTE).setValue(salt);
    }

    @Override
    public Employee getEmployee() {
        try {
            Employee employee =  jdbcTemplate.executeSelect(
                    "select object_id from tp_objects where object_type_id = ? and object_id in (" +
                    "select object_id from TP_PARAMS where attr_id = ? and reference = ?)",
                    new Object[][] {
                            {Types.NUMERIC, EMPLOYEE_OBJECT_TYPE_ID},
                            {Types.NUMERIC, USER_ATTRIBUTE},
                            {Types.NUMERIC, getId()}
                    },
                    new ResultSetHandler<Employee>() {
                        @Override
                        public Employee handle(ResultSet resultSet) throws SQLException {
                            if (resultSet.next()) {
                                BigInteger employeeId = resultSet.getBigDecimal(1).toBigInteger();
                                return objectFactory.createObject(employeeId, Employee.class);
                            } else {
                                return null;
                            }
                        }
                    }
            );
            return employee;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DataAccessException("Can't get employee", e);
        }
    }

    @Override
    public BigInteger getRoleId() {
        return cacheProvider.provide().getParameter(getId(), ROLE_ATTRIBUTE).getReference();
    }

    @Override
    public Role getRole() {
        return objectFactory.createObject(getRoleId(), Role.class);
    }

    @Override
    public void setRoleId(BigInteger RoleId) {
        cacheProvider.provide().getParameter(getId(), ROLE_ATTRIBUTE).setReference(RoleId);
    }
}
