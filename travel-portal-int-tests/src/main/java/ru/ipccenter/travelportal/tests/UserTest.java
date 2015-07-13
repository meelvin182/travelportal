package ru.ipccenter.travelportal.tests;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.utils.BeanLookupHelper;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;
import ru.ipccenter.travelportal.metamodel.Dao;
import ru.ipccenter.travelportal.metamodel.entities.MMObject;
import ru.ipccenter.travelportal.metamodel.entities.MMObjectType;

import javax.transaction.UserTransaction;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


public class UserTest extends TestCase {

    private static final Logger LOG = Logger.getLogger(UserTest.class);
    private static final String TEST_OBJECT_NAME = "vadan was here";

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

    public void testUser() throws Exception {
        //User user = objectFactory.createObject(User.class);
        userTransaction.begin();
        MMObjectType mmObjectType = dao.getById(new BigInteger("9223372036854775798"), MMObjectType.class);

        MMObject mmObject = new MMObject();
        mmObject.setObjType(mmObjectType);

        mmObject.setName(TEST_OBJECT_NAME);

        try {
            dao.save(mmObject);
            objectId = mmObject.getId();
            userTransaction.commit();
        } catch (Exception e) {
            LOG.info(e);
            userTransaction.rollback();
            throw e;
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        jdbcTemplate.executeSelect(
                "select * from TP_OBJECTS where object_id = ?",
                new Object[][] {
                        {Types.NUMERIC, objectId}
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {
                        assertTrue(resultSet.next());
                        return null;
                    }
                });


        userTransaction.begin();
        try {
            dao.delete(dao.getById(objectId, MMObject.class));
            userTransaction.commit();
        } catch (Exception e) {
            LOG.info(e);
            userTransaction.rollback();
            throw e;
        }
    }
}
