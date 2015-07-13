package ru.ipccenter.travelportal.services;

import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.common.model.objects.User;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import java.math.BigInteger;

/**
 * Created by Ivan on 18.05.2015.
 */
@ApplicationScoped
public class UserService {

    @EJB
    private TPObjectFactory objectFactory;

    public User createUser(BigInteger employeeId) {
        final User user = objectFactory.createObject(User.class);
        final Employee employee = objectFactory.createObject(employeeId, Employee.class);

        final String name = (employee.getName() != null)
                ? employee.getName().toLowerCase().trim().replaceAll("\\s+", "_"): "";
        final String lastName = (employee.getLastName() != null)
                ? employee.getLastName().toLowerCase().trim().replaceAll("\\s+", "_"): "";

        user.setName(name + "_" + lastName);
        employee.setUserId(user.getId());
        return user;
    }
}
