package ru.ipccenter.travelportal.ejb.stateful;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.objects.Department;
import ru.ipccenter.travelportal.common.model.objects.Position;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.math.BigInteger;

/**
 * Created by Oparin on 17.03.2015.
 */

@Stateful
@Local(Position.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PositionBean extends AbstractTPObject implements Position {
    private static final Logger LOG = Logger.getLogger(UserBean.class);

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
    public BigInteger getDepartmentId() {
        return cacheProvider.provide().getParameter(getId(), DEPARTMENT_ATTRIBUTE).getReference();
    }

    @Override
    public void setDepartmentId(BigInteger departmentId) {
        cacheProvider.provide().getParameter(getId(), DEPARTMENT_ATTRIBUTE).setReference(departmentId);
    }

    @Override
    public Department getDepartment() {
        return objectFactory.createObject(getDepartmentId(), Department.class);
    }
}