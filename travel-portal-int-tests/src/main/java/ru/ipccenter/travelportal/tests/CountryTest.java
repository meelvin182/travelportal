package ru.ipccenter.travelportal.tests;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.objects.Country;
import ru.ipccenter.travelportal.common.utils.BeanLookupHelper;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;
import ru.ipccenter.travelportal.metamodel.Dao;

import javax.transaction.UserTransaction;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by 111 on 27.03.2015.
 */
public class CountryTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(CountryTest.class);
    private static final String COUNTRY_NAME = "Cuba";

    private BigInteger objectId;
    private TPObjectFactory objectFactory;
    private JdbcTemplate jdbcTemplate;
    private Dao dao;
    private UserTransaction userTransaction;

    private BigInteger countryId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        objectFactory = BeanLookupHelper.lookup(TPObjectFactory.class);
        jdbcTemplate = BeanLookupHelper.lookup(JdbcTemplate.class);
        userTransaction = BeanLookupHelper.lookup(UserTransaction.class);
        dao = BeanLookupHelper.lookup(Dao.class);
    }

    public void testCountry() throws Exception{
        Country country = objectFactory.createObject(Country.class);
        countryId = country.getId();

        LOG.debug("country id: " + countryId);

        try {
            userTransaction.begin();
            country.setName(COUNTRY_NAME);
            userTransaction.commit();
        } catch (Exception e) {
            LOG.error(e);
            userTransaction.rollback();
            throw e;
        }

        jdbcTemplate.executeSelect(
                "select name from tp_objects where object_id = ?",
                new Object[][] {
                        {Types.NUMERIC, countryId}
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {

                        LOG.debug("in handle country");
                        assertTrue(resultSet.next());

                        LOG.debug("name is: " + resultSet.getString(1));

                        assertEquals(resultSet.getString(1), COUNTRY_NAME);
                        return null;
                    }
                }
        );
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        jdbcTemplate.executeUpdate(
                "delete from tp_objects where object_id = ?",
                new Object[][]{
                        {Types.NUMERIC, countryId}
                }
        );

    }
}
