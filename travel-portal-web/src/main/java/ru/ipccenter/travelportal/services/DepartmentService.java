package ru.ipccenter.travelportal.services;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.objects.Department;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
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
public class DepartmentService {

    private static final BigInteger DEP_OBJECT_TYPE_ID = new BigInteger(
            Department.class.getAnnotation(ObjectType.class).id());
    private static final String MAJOR_DEP_QUERY =
            "select\n" +
            "  object_id\n" +
            "from\n" +
            "  tp_objects\n" +
            "where\n" +
            "  object_type_id in (\n" +
            "    select \n" +
            "      object_type_id\n" +
            "    from\n" +
            "      tp_object_types\n" +
            "    connect by\n" +
            "      prior object_type_id = parent_id\n" +
            "    start with\n" +
            "      object_type_id = ? /* Department */\n" +
            "  ) and parent_id is null";

    private static final Logger log = Logger.getLogger(DepartmentService.class);

    public class DepartmentByIdFactory extends AbstractByIdFactory<Department> {
        @Override
        public Department create(BigInteger departmentId) {
            return getObjectFactory().createObject(departmentId, Department.class);
        }
    }

    @EJB
    private TPObjectFactory objectFactory;
    @EJB
    private JdbcTemplate jdbcTemplate;
    @Inject
    private EmployeeService employeeService;

    private final DepartmentByIdFactory departmentFactory = new DepartmentByIdFactory();

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public DepartmentByIdFactory getDepartmentFactory() {
        return departmentFactory;
    }

    public List<Department> getMajorDepartments() {

        try {
            return jdbcTemplate.executeSelect(
                    MAJOR_DEP_QUERY,
                    new Object[][] {
                            {Types.NUMERIC, DEP_OBJECT_TYPE_ID}
                    },
                    new ResultSetHandler<List<Department>>() {
                @Override
                public List<Department> handle(ResultSet resultSet) throws SQLException {
                    List<Department> departments = new LinkedList<Department>();

                    while (resultSet.next()) {
                        Department department = objectFactory
                                .createObject(resultSet.getBigDecimal(1).toBigInteger(), Department.class);
                        if (department != null) {
                            departments.add(department);
                        }
                    }

                    return departments;
                }
            });
        } catch (SQLException e) {
            log.error("Can't load major departments", e);
            return Collections.emptyList();
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Department createDepartment(String name, BigInteger parentId) {
        Department department = objectFactory.createObject(Department.class);
        department.setName(name);
        department.setParentId(parentId);
        return department;
    }

    public Department getDepartment(BigInteger id) {
        return departmentFactory.create(id);
    }

    protected TPObjectFactory getObjectFactory() {
        return objectFactory;
    }
}
