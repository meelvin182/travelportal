package ru.ipccenter.travelportal.dialogs;

import org.primefaces.context.RequestContext;
import ru.ipccenter.travelportal.common.model.objects.*;
import ru.ipccenter.travelportal.services.*;
import ru.ipccenter.travelportal.session.CustomLoginModule;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by Ivan on 18.05.2015.
 */
@ViewScoped
@ManagedBean
public class EmployeeBean {

    public static final String EMPLOYEE_ID_KEY = "employee-id";

    @Inject
    private EmployeeService employeeService;
    @Inject
    private OfficeService officeService;
    @Inject
    private PositionService positionService;
    @Inject
    private RoleService roleService;
    @Inject
    private UserService userService;

    private BigInteger id;
    private String name;
    private String lastName;
    private String email;
    private BigInteger officeId;
    private BigInteger positionId;
    private Department department;
    private String managerName;

    private boolean hasUser;
    private String userName;
    private String password;
    private BigInteger roleId;

    public String getId() {
        return (id != null)? id.toString(): null;
    }

    public void setId(String employeeId) {
        final BigInteger id = new BigInteger(employeeId);
        final Employee employee = employeeService.getEmployee(id);
        final User user = employee.getUser();

        this.id = id;
        this.name = employee.getName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.officeId = employee.getOfficeId();
        this.positionId = employee.getPositionId();
        this.department = employee.getDepartment();
        Employee manager = employee.getManager();
        this.managerName = (manager != null)
                ? (manager.getName() + " " + manager.getLastName()).trim()
                : null;

        if (user != null) {
            this.hasUser = true;
            this.userName = user.getName();
            this.roleId = user.getRoleId();
            user.unused();
        } else {
            this.hasUser = false;
        }

        employee.unused();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void saveEmployee() {
        final Employee employee = employeeService.getEmployee(id);

        employee.setName(name);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setOfficeId(officeId);
        employee.setPositionId(positionId);

        final User user = employee.getUser();
        if (user != null) {
            final String salt = CustomLoginModule.createSalt();
            final String hash = CustomLoginModule.createHash(password, salt);

            user.setName(userName);
            user.setSalt(salt);
            user.setPassword(hash);
            user.setRoleId(roleId);
        }

        RequestContext.getCurrentInstance().closeDialog(employee.getDepartmentId());
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void createUser() {
        Employee employee = employeeService.getEmployee(id);
        User user = userService.createUser(id);
        this.userName = user.getName();
        this.hasUser = true;
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

    public BigInteger getOfficeId() {
        return officeId;
    }

    public BigInteger getPositionId() {
        return positionId;
    }

    public Department getDepartment() {
        return department;
    }

    public String getManagerName() {
        return managerName;
    }

    public boolean isHasUser() {
        return hasUser;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public BigInteger getRoleId() {
        return roleId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOfficeId(BigInteger officeId) {
        this.officeId = officeId;
    }

    public void setPositionId(BigInteger positionId) {
        this.positionId = positionId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoleId(BigInteger roleId) {
        this.roleId = roleId;
    }

    public List<Office> getOffices() {
        return officeService.getOffices();
    }

    public List<Position> getPositions() {
        return positionService.getByDepartmentId(department.getId());
    }

    public List<Role> getRoles() {
        return roleService.getRolesById();
    }
}

