package ru.ipccenter.travelportal.ejb.stateful;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.objects.Department;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;

import javax.ejb.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Oparin on 17.03.2015.
 */
@Stateful
@Local(Department.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DepartmentBean extends AbstractTPObject implements Department {

    private static final Logger LOG = Logger.getLogger(UserBean.class);

    @EJB
    private TPObjectFactory objectFactory;
    @EJB
    private JdbcTemplate jdbcTemplate;

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
    public List<BigInteger> getChildIds() {
        return cacheProvider.provide().getObject(getId()).getChildIds();
    }

    @Override
    public List<Department> getChildren() {

        List<BigInteger> childIds = getChildIds();
        List<Department> childDepartments = new ArrayList<>(childIds.size());

        for (BigInteger childId: childIds) {
            Department child = objectFactory.createObject(childId, Department.class);
            if (child != null) {
                childDepartments.add(child);
            }
        }

        return Collections.unmodifiableList(childDepartments);
    }

    @Override
    public BigInteger getManagerId() {
        return cacheProvider.provide().getParameter(getId(), MANAGER_ATTRIBUTE).getReference();
    }

    public Employee getManager() {
        return objectFactory.createObject(getManagerId(), Employee.class);
    }

    @Override
    public void setManagerId(BigInteger majorDepartmentId) {
        cacheProvider.provide().getParameter(getId(), MANAGER_ATTRIBUTE).setReference(majorDepartmentId);
    }
}