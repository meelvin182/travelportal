package ru.ipccenter.travelportal.tests;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.utils.BeanLookupHelper;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.metamodel.Dao;

import javax.transaction.UserTransaction;
import java.math.BigInteger;


public class StatusTest extends TestCase {

    private static final Logger LOG = Logger.getLogger(StatusTest.class);
    private static final String TEST_OBJECT_NAME = "";

    private BigInteger objectId;
    private TPObjectFactory objectFactory;
    private JdbcTemplate jdbcTemplate;
    private Dao dao;
    private UserTransaction userTransaction;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dao = BeanLookupHelper.lookup(Dao.class);
        objectFactory = BeanLookupHelper.lookup(TPObjectFactory.class);
        jdbcTemplate = BeanLookupHelper.lookup(JdbcTemplate.class);
        userTransaction = BeanLookupHelper.lookup(UserTransaction.class);
    }

    public void testStatus() throws Exception {

    }
}
