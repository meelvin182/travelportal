package ru.ipccenter.travelportal.tests;

import junit.framework.TestCase;
import ru.ipccenter.travelportal.common.utils.BeanLookupHelper;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.metamodel.Dao;

import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

/**
 * Created by Ivan on 23.03.2015.
 */
public class BeanLookupHelperTest extends TestCase {

    public void testLookup() throws Exception {
        BeanLookupHelper.lookup(Dao.class);
        BeanLookupHelper.lookup(JdbcTemplate.class);
        BeanLookupHelper.lookup(UserTransaction.class);
        BeanLookupHelper.lookup(TransactionSynchronizationRegistry.class);
    }
}
