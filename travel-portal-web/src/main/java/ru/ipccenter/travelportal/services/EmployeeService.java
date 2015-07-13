package ru.ipccenter.travelportal.services;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.common.model.objects.Position;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ivan on 25.04.2015.
 */
@ApplicationScoped
public class EmployeeService {

    private static final Logger log = Logger.getLogger(EmployeeService.class);

    private static final BigInteger EMPLOYEE_DEPARTMENT_ATTR = Employee.DEPARTMENT_ATTRIBUTE;
    private static final String EMPLOYEE_BY_DEPARTMENT_QUERY =
            "select\n" +
            "  object_id\n" +
            "from\n" +
            "  tp_params\n" +
            "where\n" +
            "  attr_id = ?\n" +
            "  and reference = ?";

    public class EmployeeByIdFactory extends AbstractByIdFactory<Employee> {
        @Override
        public Employee create(BigInteger employeeId) {
            return getObjectFactory().createObject(employeeId, Employee.class);
        }
    }

    @EJB
    private TPObjectFactory objectFactory;
    @EJB
    private JdbcTemplate jdbcTemplate;

    private final EmployeeByIdFactory employeeFactory = new EmployeeByIdFactory();

    public EmployeeByIdFactory getEmployeeFactory() {
        return employeeFactory;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Employee createEmployee(String name, BigInteger parentId, BigInteger departmentId) {
        final Employee employee = objectFactory.createObject(Employee.class);
        employee.setName(name);
        employee.setParentId(parentId);
        employee.setManagerId(parentId);
        employee.setDepartmentId(departmentId);
        return employee;
    }

    public Employee getEmployee(BigInteger id) {
        return employeeFactory.create(id);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Position> getAllPositions(BigInteger departmentId) {
        return Collections.emptyList();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public <T> List<T> getEmployeesForDepartment(BigInteger departmentId, final AbstractByIdFactory<T> factory) {
        try {
            return jdbcTemplate.executeSelect(
                    EMPLOYEE_BY_DEPARTMENT_QUERY,
                    new Object[][] {
                            {Types.NUMERIC, EMPLOYEE_DEPARTMENT_ATTR},
                            {Types.NUMERIC, departmentId}
                    },
                    new ResultSetHandler<List<T>>() {
                        @Override
                        public List<T> handle(ResultSet resultSet) throws SQLException {
                            List<T> result = new LinkedList<T>();
                            while (resultSet.next()) {
                                result.add(factory.create(resultSet.getBigDecimal(1).toBigInteger()));
                            }
                            return result;
                        }
                    });
        } catch (SQLException e) {
            log.error("Can't retrieve employees by department: #" + departmentId, e);
            return Collections.emptyList();
        }
    }

    protected TPObjectFactory getObjectFactory() {
        return objectFactory;
    }
}
