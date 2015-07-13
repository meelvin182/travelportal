package ru.ipccenter.travelportal.ejb.stateless;

import ru.ipccenter.travelportal.caches.SessionCache;
import ru.ipccenter.travelportal.caches.SessionCacheProvider;
import ru.ipccenter.travelportal.caches.impl.SessionCacheImpl;
import ru.ipccenter.travelportal.metamodel.Dao;
import ru.ipccenter.travelportal.metamodel.DaoParameter;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;

/**
 * Created by Ivan on 16.03.2015.
 */
@Stateless
@Local(SessionCacheProvider.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class SessionCacheProviderBean implements SessionCacheProvider {

    private enum Resources {
        SYNCHRONIZATION,
        CACHE,
    }

    private class CacheSynchronization implements Synchronization {

        private SessionCache.SynchronizationCallback callback;

        public CacheSynchronization(SessionCache.SynchronizationCallback callback) {
            this.callback = callback;
        }

        @Override
        public void beforeCompletion() {
            callback.beforeCompletion();
        }

        @Override
        public void afterCompletion(int status) {
            callback.afterCompletion();
        }
    }

    @Resource(lookup = "java:comp/TransactionSynchronizationRegistry")
    private TransactionSynchronizationRegistry registry;
    @EJB
    private Dao dao;
    @EJB
    private DaoParameter parameterDao;

    public SessionCache provide() {

        boolean noSynchronization = (registry.getResource(Resources.SYNCHRONIZATION) == null);

        if (noSynchronization) {
            SessionCache cache = new SessionCacheImpl(dao, parameterDao);
            Synchronization synchronization = new CacheSynchronization(cache.getCallback());

            registry.registerInterposedSynchronization(synchronization);

            registry.putResource(Resources.CACHE, cache);
            registry.putResource(Resources.SYNCHRONIZATION, synchronization);
            return cache;
        } else {
            return (SessionCache) registry.getResource(Resources.CACHE);
        }
    };
}
