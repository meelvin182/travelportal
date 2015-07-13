package ru.ipccenter.travelportal.tests;

import junit.framework.TestCase;
import ru.ipccenter.travelportal.common.utils.BeanLookupHelper;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class JdbcTemplateTest extends TestCase {

    private JdbcTemplate template;

    protected void setUp() throws Exception {
        super.setUp();
        template = BeanLookupHelper.lookup(JdbcTemplate.class);
    }

    public void testSelectWithParams() throws Exception {
        final String query = "select ?, ? from dual";
        final BigInteger testBigInteger = new BigInteger("1000000000000000000");
        final String testString = "Hello";

        template.executeSelect(
                query,
                new Object[][] {
                        {Types.NUMERIC, testBigInteger},
                        {Types.VARCHAR, testString},
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {
                        assertTrue(resultSet.next());
                        assertEquals(resultSet.getBigDecimal(1).toBigInteger(), testBigInteger);
                        assertEquals(resultSet.getString(2), testString);
                        return null;
                    }
                });
    }
}
