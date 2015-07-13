package ru.ipccenter.travelportal.data.holders;

import ru.ipccenter.travelportal.common.model.objects.Employee;

import java.math.BigInteger;

/**
 * Created by Ivan on 14.05.2015.
 */
public final class EmployeeInfo {

    private final BigInteger id;
    private final String name;
    private final String lastName;
    private final String email;

    public EmployeeInfo(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
    }

    public BigInteger getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}