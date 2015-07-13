package ru.ipccenter.travelportal.tests;

import junit.framework.TestCase;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.objects.Employee;
import ru.ipccenter.travelportal.common.utils.BeanLookupHelper;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.transaction.UserTransaction;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by Ivan on 23.03.2015.
 */
public class EmployeeTest extends TestCase {

    private static final String EMP_NAME = "Vasya";
    private static final String EMP_L_NAME = "Pupkin";

    private UserTransaction userTransaction;
    private TPObjectFactory objectFactory;
    private JdbcTemplate jdbcTemplate;

    private BigInteger employeeId;

    @Override
    protected void setUp() throws Exception {
        userTransaction = BeanLookupHelper.lookup(UserTransaction.class);
        objectFactory = BeanLookupHelper.lookup(TPObjectFactory.class);
        jdbcTemplate = BeanLookupHelper.lookup(JdbcTemplate.class);
    }

    public void testEmployeeNoTransaction() throws Exception {
        Employee employee = objectFactory.createObject(Employee.class);
        employee.setName(EMP_NAME);
        employee.setLastName(EMP_L_NAME);

        employeeId = employee.getId();

        jdbcTemplate.executeSelect(
                "select NAME from TP_OBJECTS where OBJECT_ID = ?",
                new Object[][] {
                        {Types.NUMERIC, employeeId}
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {

                        assertTrue(resultSet.next());
                        assertEquals(resultSet.getString(1), EMP_NAME);
                        return null;
                    }
                }
        );

        jdbcTemplate.executeSelect(
                "select VALUE from TP_PARAMS where OBJECT_ID = ?",
                new Object[][] {
                        {Types.NUMERIC, employeeId}
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {

                        assertTrue(resultSet.next());
                        assertEquals(resultSet.getString(1), EMP_L_NAME);
                        return null;
                    }
                }
        );
    }

    public void testEmployeeInTransaction() throws Exception {

        userTransaction.begin();
        try {
            Employee employee = objectFactory.createObject(Employee.class);
            employee.setName(EMP_NAME);
            employee.setLastName(EMP_L_NAME);

            userTransaction.commit();

            employeeId = employee.getId();
        } catch (Exception e) {
            userTransaction.rollback();
            throw e;
        }

        jdbcTemplate.executeSelect(
                "select NAME from TP_OBJECTS where OBJECT_ID = ?",
                new Object[][] {
                        {Types.NUMERIC, employeeId}
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {

                        assertTrue(resultSet.next());
                        assertEquals(resultSet.getString(1), EMP_NAME);
                        return null;
                    }
                }
        );

        jdbcTemplate.executeSelect(
                "select VALUE from TP_PARAMS where OBJECT_ID = ?",
                new Object[][] {
                        {Types.NUMERIC, employeeId}
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {

                        assertTrue(resultSet.next());
                        assertEquals(resultSet.getString(1), EMP_L_NAME);
                        assertFalse(resultSet.next());
                        return null;
                    }
                }
        );
    }

    public void testEmployeeDeleteInTransaction() throws Exception {
        Employee employee;
        BigInteger employeeId;

        userTransaction.begin();
        try {
            employee = objectFactory.createObject(Employee.class);
            employee.setName(EMP_NAME);
            employee.setLastName(EMP_L_NAME);

            userTransaction.commit();

            employeeId = employee.getId();
        } catch (Exception e) {
            userTransaction.rollback();
            throw e;
        }

        jdbcTemplate.executeSelect(
                "select NAME from TP_OBJECTS where OBJECT_ID = ?",
                new Object[][] {
                        {Types.NUMERIC, employeeId}
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {

                        assertTrue(resultSet.next());
                        assertEquals(resultSet.getString(1), EMP_NAME);
                        return null;
                    }
                }
        );

        jdbcTemplate.executeSelect(
                "select VALUE from TP_PARAMS where OBJECT_ID = ?",
                new Object[][] {
                        {Types.NUMERIC, employeeId}
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {

                        assertTrue(resultSet.next());
                        assertEquals(resultSet.getString(1), EMP_L_NAME);
                        assertFalse(resultSet.next());
                        return null;
                    }
                }
        );

        userTransaction.begin();
        try {
            employee.delete();
            userTransaction.commit();
        } catch (Exception e) {
            userTransaction.rollback();
            throw e;
        }

        jdbcTemplate.executeSelect(
                "select NAME from TP_OBJECTS where OBJECT_ID = ?",
                new Object[][] {
                        {Types.NUMERIC, employeeId}
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {
                        assertFalse(resultSet.next());
                        return null;
                    }
                }
        );

        jdbcTemplate.executeSelect(
                "select VALUE from TP_PARAMS where OBJECT_ID = ?",
                new Object[][] {
                        {Types.NUMERIC, employeeId}
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {
                        assertFalse(resultSet.next());
                        return null;
                    }
                }
        );
    }


    @Override
    protected void tearDown() throws Exception {
        jdbcTemplate.executeUpdate(
                "delete from TP_OBJECTS where OBJECT_ID = ?",
                new Object[][]{
                        {Types.NUMERIC, employeeId}
                }
        );

        super.tearDown();
    }
}
