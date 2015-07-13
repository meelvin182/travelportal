package ru.ipccenter.travelportal.utils;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.utils.BeanLookupHelper;

import javax.transaction.UserTransaction;
import java.util.concurrent.Callable;

/**
 * Created by Ivan on 26.04.2015.
 */
public class TransactionUtils {

    private static final Logger log = Logger.getLogger(TransactionUtils.class);

    public static <V> V doInTransactionNoException(Callable<V> callable) {
        try {
            return doInTransaction(callable);
        } catch (Exception e) {
            return null;
        }
    }

    public static <V> V doInTransaction(Callable<V> callable) throws Exception {
        UserTransaction ut = null;
        V result = null;

        try {
            ut = BeanLookupHelper.lookup(UserTransaction.class);
            ut.begin();
            result = callable.call();
            ut.commit();
        } catch (Exception e) {
            if (ut != null) {
                try { ut.rollback(); } catch (Exception ex) {};
            }
            log.error(e.getMessage(), e);
            throw e;
        }

        return result;
    }
}
