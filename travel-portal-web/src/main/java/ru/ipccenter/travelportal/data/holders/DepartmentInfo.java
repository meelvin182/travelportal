package ru.ipccenter.travelportal.data.holders;

import ru.ipccenter.travelportal.common.model.objects.Department;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.common.model.objects.User;
import ru.ipccenter.travelportal.services.AbstractByIdFactory;
import ru.ipccenter.travelportal.services.DepartmentService;
import ru.ipccenter.travelportal.services.EmployeeService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public final class DepartmentInfo {
    public static enum DepartmentType {
        COMMON(new BigInteger("9223372036854775728"), "Common department"),
        TRAVEL_SUPPORT(new BigInteger("9223372036854775727"), "Travel suppport"),
        IT(new BigInteger("9223372036854775726"), "System support department");

        private DepartmentType(BigInteger roleId, String roleName) {
           this.roleId = roleId;
           this.roleName = roleName;
        }

        private final BigInteger roleId;
        private final String roleName;

        public BigInteger getRoleId() {
            return roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public static DepartmentType valueOf(BigInteger roleId) {
            for (DepartmentType type: values()) {
                if (type.getRoleId().equals(roleId)) {
                    return type;
                }
            }

            return null;
        }
    }

    private BigInteger departmentId;
    private String name;
    private DepartmentType type;
    private BigInteger managerId;
    private List<EmployeeInfo> employees;
    private EmployeeInfo selectedEmployee;

    public DepartmentInfo() {
        this.type = DepartmentType.COMMON;
        this.employees = Collections.emptyList();
    }

    public DepartmentInfo(BigInteger id, DepartmentService service) {
        this();
        if (id != null) {
            loadDepartmentInfo(id, service);
        }
    }

    private void loadDepartmentInfo(final BigInteger id, final DepartmentService departmentService) {
        final Department departmentBean = departmentService.getDepartment(id);
        final Employee managerBean = departmentBean.getManager();

        departmentId = departmentBean.getId();
        name = departmentBean.getName();

        if (managerBean != null) {
            final User userBean = managerBean.getUser();

            managerId = managerBean.getId();
            if (userBean != null) {
                type = DepartmentType.valueOf(userBean.getRoleId());
            }

            managerBean.unused();
        }

        final EmployeeService employeeService = departmentService.getEmployeeService();
        employees = employeeService.getEmployeesForDepartment(
                departmentId,
                new AbstractByIdFactory<EmployeeInfo>() {
                    @Override
                    public EmployeeInfo create(BigInteger id) {
                        final Employee employee = employeeService.getEmployee(id);
                        final EmployeeInfo employeeInfo = new EmployeeInfo(employee);
                        employee.unused();
                        return employeeInfo;
                    }
                });

        departmentBean.unused();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DepartmentType getType() {
        return type;
    }

    public void setType(DepartmentType type) {
        this.type = type;
    }

    public BigInteger getId() {
        return departmentId;
    }

    public BigInteger getManagerId() {
        return managerId;
    }

    public void setManagerId(BigInteger managerId) {
        this.managerId = managerId;
    }

    public List<EmployeeInfo> getEmployees() {
        return employees;
    }

    public EmployeeInfo getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(EmployeeInfo selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }
}
