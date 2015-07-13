package ru.ipccenter.travelportal.ejb.stateful;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.objects.*;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Oparin on 17.03.2015.
 */
@Stateful
@Local(Employee.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EmployeeBean extends AbstractTPObject implements Employee {
    private static final Logger LOG = Logger.getLogger(EmployeeBean.class);

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
    public List<Employee> getChildren() {

        List<BigInteger> childIds = getChildIds();
        List<Employee> childDepartments = new ArrayList<>(childIds.size());

        for (BigInteger childId: childIds) {
            Employee child = objectFactory.createObject(childId, Employee.class);
            if (child != null) {
                childDepartments.add(child);
            }
        }

        return Collections.unmodifiableList(childDepartments);
    }

    @Override
    public String getLastName() {
        return cacheProvider.provide().getParameter(super.getId(), LASTNAME_ATTRIBUTE).getValue();
    }

    @Override
    public void setLastName(String lastName) {
        cacheProvider.provide().getParameter(super.getId(), LASTNAME_ATTRIBUTE).setValue(lastName);
    }

    @Override
    public BigInteger getOfficeId() {
        return cacheProvider.provide().getParameter(super.getId(), OFFICE_ATTRIBUTE).getReference();
    }

    @Override
    public void setOfficeId(BigInteger officeId) {
        cacheProvider.provide().getParameter(super.getId(), OFFICE_ATTRIBUTE).setReference(officeId);
    }

    @Override
    public BigInteger getDepartmentId() {
        return cacheProvider.provide().getParameter(super.getId(), DEPARTMENT_ATTRIBUTE).getReference();
    }

    @Override
    public void setDepartmentId(BigInteger departmentId) {
        cacheProvider.provide().getParameter(super.getId(), DEPARTMENT_ATTRIBUTE).setReference(departmentId);
    }

    @Override
    public BigInteger getManagerId() {
        return cacheProvider.provide().getParameter(super.getId(), MANAGER_ATTRIBUTE).getReference();
    }

    @Override
    public void setManagerId(BigInteger managerId) {
        cacheProvider.provide().getParameter(super.getId(), MANAGER_ATTRIBUTE).setReference(managerId);
    }

    @Override
    public BigInteger getPositionId() {
        return cacheProvider.provide().getParameter(super.getId(), POSITION_ATTRIBUTE).getReference();
    }

    @Override
    public void setPositionId(BigInteger positionId) {
        cacheProvider.provide().getParameter(super.getId(), POSITION_ATTRIBUTE).setReference(positionId);
    }

    @Override
    public String getEmail(){
        return cacheProvider.provide().getParameter(super.getId(), EMAIL_ATTRIBUTE).getValue();
    }

    @Override
    public void setEmail(String email) {
        cacheProvider.provide().getParameter(super.getId(), EMAIL_ATTRIBUTE).setValue(email);
    }

    @Override
    public BigInteger getUserId() {
        return cacheProvider.provide().getParameter(super.getId(), USER_ATTRIBUTE).getReference();
    }

    @Override
    public void setUserId(BigInteger userId) {
        cacheProvider.provide().getParameter(super.getId(), USER_ATTRIBUTE).setReference(userId);
    }

    @Override
    public User getUser() {
        return objectFactory.createObject(getUserId(), User.class);
    }

    @Override
    public Employee getManager() {
        return objectFactory.createObject(getManagerId(), Employee.class);
    }

    @Override
    public Department getDepartment() {
        return objectFactory.createObject(getDepartmentId(), Department.class);
    }

    @Override
    public Office getOffice() {
        return objectFactory.createObject(getOfficeId(), Office.class);
    }

    @Override
    public Position getPosition() {
        return objectFactory.createObject(getPositionId(), Position.class);
    }
}