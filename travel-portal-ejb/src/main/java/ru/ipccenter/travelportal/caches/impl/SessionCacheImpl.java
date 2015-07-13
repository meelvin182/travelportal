package ru.ipccenter.travelportal.caches.impl;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.caches.SessionCache;
import ru.ipccenter.travelportal.common.caches.LoadCallback;
import ru.ipccenter.travelportal.common.caches.UnlimitedCache;
import ru.ipccenter.travelportal.common.model.DataAccessException;
import ru.ipccenter.travelportal.metamodel.Dao;
import ru.ipccenter.travelportal.metamodel.DaoParameter;
import ru.ipccenter.travelportal.metamodel.entities.MMObject;
import ru.ipccenter.travelportal.metamodel.entities.MMParameter;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Ivan on 16.03.2015.
 */
public class SessionCacheImpl implements SessionCache {

    private static final Logger log = Logger.getLogger(SessionCacheImpl.class);
    private static final Integer INITIAL_OBJECT_CACHE_SIZE = 15;
    private static final Integer INITIAL_PARAMETER_CACHE_SIZE = 25;

    private class MMObjectCache extends UnlimitedCache<BigInteger, MMObject> {
        private MMObjectCache() {
            super(new LoadCallback<BigInteger, MMObject>() {
                @Override
                public MMObject load(BigInteger key) {
                    MMObject object = objectDao.getById(key, MMObject.class);
                    if (object != null) {
                        return CGLibProxyBuilder.buildProxy(object, modifiedObjects);
                    } else {
                        return null;
                    }
                }
            });
        }

        @Override
        public void evict() {
            try {
                objectDao.save(new LinkedList<>(modifiedObjects));
            } catch (Exception e) {
                log.error(e);
                throw new DataAccessException("Can't save objects from cache", e);
            }

            super.evict();
        }

        public void evict(BigInteger key) {
            if (key == null) { return; }

            MMObject forEviction = this.get(key);
            if (forEviction != null && modifiedObjects.contains(forEviction)) {
                try {
                    objectDao.save(forEviction);
                } catch (Exception e) {
                    log.error(e);
                    throw new DataAccessException("Can't save parameter from cache", e);
                }
            }
            super.evict(key);
        }

        protected Logger getLogger() { return log; }
        protected Integer getDefaultSize() { return INITIAL_OBJECT_CACHE_SIZE; }
        protected Integer getSize() throws Exception { return INITIAL_OBJECT_CACHE_SIZE; }
    }

    private class MMParameterKey {
        private final BigInteger objectId;
        private final BigInteger attrId;

        public MMParameterKey(BigInteger objectId, BigInteger attrId) {
            this.objectId = objectId;
            this.attrId = attrId;
        }

        public BigInteger getObjectId() {
            return objectId;
        }

        public BigInteger getAttrId() {
            return attrId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MMParameterKey)) return false;

            MMParameterKey that = (MMParameterKey) o;

            if (attrId != null ? !attrId.equals(that.attrId) : that.attrId != null) return false;
            if (objectId != null ? !objectId.equals(that.objectId) : that.objectId != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = objectId != null ? objectId.hashCode() : 0;
            result = 31 * result + (attrId != null ? attrId.hashCode() : 0);
            return result;
        }
    }

    private class SingleMMParameterCache extends UnlimitedCache<MMParameterKey, MMParameter> {
        private SingleMMParameterCache() {
            super(new LoadCallback<MMParameterKey, MMParameter>() {
                @Override
                public MMParameter load(MMParameterKey key) {
                    MMParameter parameter = null;
                    try {
                        parameter = parameterDao.getByObjIdAttrIdSingle(key.getObjectId(), key.getAttrId());
                    } catch (SQLException e) {
                        log.error(e);
                        throw new DataAccessException("Can't retrieve MMParameter from cache", e);
                    }

                    if (parameter == null) {
                        parameter = new MMParameter(key.getObjectId(), key.getAttrId());
                    }

                    return CGLibProxyBuilder.buildProxy(parameter, modifiedSingleParameters);
                }
            });
        }

        @Override
        public void evict() {
            try {
                parameterDao.save(new LinkedList<MMParameter>(modifiedSingleParameters));
            } catch (Exception e) {
                log.error(e);
                throw new DataAccessException("Can't save parameters from cache", e);
            }

            super.evict();
        }

        public void evict(MMParameterKey key) {
            if (key == null) { return; }

            MMParameter forEviction = this.get(key);
            if (forEviction != null && modifiedSingleParameters.contains(forEviction)) {
                try {
                    parameterDao.save(forEviction);
                } catch (SQLException e) {
                    log.error(e);
                    throw new DataAccessException("Can't save parameter from cache", e);
                }
            }
            super.evict(key);
        }

        protected Logger getLogger() { return log; }
        protected Integer getDefaultSize() { return INITIAL_PARAMETER_CACHE_SIZE; }
        protected Integer getSize() throws Exception { return INITIAL_PARAMETER_CACHE_SIZE; };
    }

    private class MultipleMMParameterCache extends UnlimitedCache<MMParameterKey, List<MMParameter>> {
        private MultipleMMParameterCache() {
            super(new LoadCallback<MMParameterKey, List<MMParameter>>() {
                @Override
                public List<MMParameter> load(MMParameterKey key) {
                    List<MMParameter> parameters = null;

                    try {
                        parameters = parameterDao.getByObjIdAttrId(key.getObjectId(), key.getAttrId());
                    } catch (SQLException e) {
                        log.error(e);
                        throw new DataAccessException("Can't retrieve MMParameter", e);
                    }

                    if (!parameters.isEmpty()) {
                        List<MMParameter> proxyList = new ArrayList<MMParameter>(parameters.size());
                        for (MMParameter parameter: parameters) {
                            proxyList.add(CGLibProxyBuilder.buildProxy(parameter, modifiedMultipleParameters));
                        }
                        return proxyList;
                    } else {
                        return parameters;
                    }
                }
            });
        }

        @Override
        public void evict() {
            try {
                parameterDao.save(new LinkedList<MMParameter>(modifiedMultipleParameters));
            } catch (Exception e) {
                log.error(e);
                throw new DataAccessException("Can't save parameters from cache", e);
            }

            super.evict();
        }

        @Override
        public void evict(MMParameterKey key) {
            if (key == null) { return; }

            List<MMParameter> parameters = this.get(key);
            for (MMParameter parameter: parameters) {
                if (parameter != null && modifiedMultipleParameters.contains(parameter)) {
                    try {
                        parameterDao.save(parameter);
                    } catch (SQLException e) {
                        log.error(e);
                        throw new DataAccessException("Can't save parameter from cache", e);
                    }
                }
            }
            super.evict(key);
        }

        protected Logger getLogger() { return log; }
        protected Integer getDefaultSize() { return INITIAL_PARAMETER_CACHE_SIZE; }
        protected Integer getSize() { return INITIAL_PARAMETER_CACHE_SIZE; }
    }


    private final Dao objectDao;
    private final DaoParameter parameterDao;
    private final MMObjectCache objectCache;
    private final SingleMMParameterCache singleParameterCache;
    private final MultipleMMParameterCache multipleParameterCache;
    private final Set<MMObject> modifiedObjects;
    private final Set<MMParameter> modifiedSingleParameters;
    private final Set<MMParameter> modifiedMultipleParameters;

    public SessionCacheImpl(Dao dao, DaoParameter parameterDao) {
        this.objectDao = dao;
        this.parameterDao = parameterDao;

        this.modifiedObjects = new HashSet<>();
        this.modifiedSingleParameters = new HashSet<>();
        this.modifiedMultipleParameters = new HashSet<>();

        this.objectCache = new MMObjectCache();
        this.singleParameterCache = new SingleMMParameterCache();
        this.multipleParameterCache = new MultipleMMParameterCache();
    }

    @Override
    public void evictObject(BigInteger id) {
        objectCache.evict(id);
    }

    @Override
    public MMObject getObject(BigInteger id) {
        return objectCache.get(id);
    }

    @Override
    public MMParameter getParameter(BigInteger objectId, BigInteger attributeId) {
        return singleParameterCache.get(new MMParameterKey(objectId, attributeId));
    }

    @Override
    public List<MMParameter> getParameters(BigInteger objectId, BigInteger attributeId) {
        return multipleParameterCache.get(new MMParameterKey(objectId, attributeId));
    }

    @Override
    public List<MMParameter> getParametersWithEmptyLast(BigInteger objectId, BigInteger attributeId) {
        List<MMParameter> parameters = multipleParameterCache.get(new MMParameterKey(objectId, attributeId));
        int listSize = parameters.size();
        int newOrderNumber = (listSize > 0)
                ? parameters.get(listSize - 1).getOrderNum() + 1
                : 1;

        parameters.add(CGLibProxyBuilder.buildProxy(
                new MMParameter(objectId, attributeId, newOrderNumber),
                modifiedMultipleParameters));
        return parameters;
    }

    @Override
    public SynchronizationCallback getCallback() {
        return new SynchronizationCallback() {
            @Override
            public void beforeCompletion() {
                objectCache.evict();
                singleParameterCache.evict();
                multipleParameterCache.evict();
            }

            @Override
            public void afterCompletion() {

            }
        };
    }
}
