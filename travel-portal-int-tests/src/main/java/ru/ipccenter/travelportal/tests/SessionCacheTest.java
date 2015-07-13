package ru.ipccenter.travelportal.tests;

import junit.framework.TestCase;
import ru.ipccenter.travelportal.caches.SessionCache;
import ru.ipccenter.travelportal.caches.impl.SessionCacheImpl;
import ru.ipccenter.travelportal.metamodel.Dao;
import ru.ipccenter.travelportal.metamodel.DaoParameter;
import ru.ipccenter.travelportal.metamodel.entities.MMObject;
import ru.ipccenter.travelportal.metamodel.entities.MMParameter;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Ivan on 19.03.2015.
 */
public class SessionCacheTest extends TestCase {

    private class DaoMock implements Dao {

        @Override
        public <T> void delete(T entity) {

        }

        @Override
        public <T> void delete(Collection<T> entities) {

        }

        @Override
        public <T> void save(T entity) {

        }

        @Override
        public <T> void save(Collection<T> entities) {
            for (T entity: entities) {
                MMObject object = (MMObject) entity;
                assertFalse(object.getId().equals(NOT_MODIFIED));
                assertTrue(object.getId().toString().equals(object.getName()));
            }
        }

        @Override
        public <T> void refresh(T entity) {

        }

        @Override
        public <T> void refresh(Collection<T> entities) {

        }

        @Override
        public <T> T getById(Object id, Class<T> entityclass) {
            try {
                T entity = (T) entityclass.newInstance();
                if (id instanceof BigInteger) {
                    if (entity instanceof MMObject) {
                        ((MMObject) entity).setId((BigInteger) id);
                    } else if (entity instanceof MMParameter) {
                        ((MMParameter) entity).setObjId((BigInteger) id);
                        ((MMParameter) entity).setAttrId((BigInteger) id);
                    }
                }
                return entity;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public <T> List<T> findAll(Class<T> entityClass) {
            return Collections.emptyList();
        }

        @Override
        public <T> List<T> getBySql(String sql, Class<T> clazz) {
            return Collections.emptyList();
        }
    }


    private class DaoParameterMock implements DaoParameter {
        @Override
        public void delete(MMParameter parameter) throws SQLException {

        }

        @Override
        public void delete(Collection<MMParameter> parameters) throws SQLException {

        }

        @Override
        public void save(MMParameter parameter) throws SQLException {

        }

        @Override
        public void save(Collection<MMParameter> parameters) throws SQLException {

        }

        @Override
        public void refresh(MMParameter parameter) throws SQLException {

        }

        @Override
        public void refresh(Collection<MMParameter> parameters) throws SQLException {

        }

        @Override
        public List<MMParameter> getByObjIdAttrId(BigInteger objId, BigInteger attrId) throws SQLException {
            return null;
        }

        @Override
        public MMParameter getByObjIdAttrIdSingle(BigInteger objId, BigInteger attrId) throws SQLException {
            return null;
        }
    }

    private static final BigInteger NOT_MODIFIED = new BigInteger("9");

    private SessionCache cache;

    @Override
    protected void setUp() throws Exception {
        this.cache = new SessionCacheImpl(new DaoMock(), mock(DaoParameter.class));
    }

    public void testFullCycle() throws Exception {
        for (int i = 0; i < 20; i++) {
            BigInteger id = BigInteger.valueOf(i);
            MMObject object = cache.getObject(id);
            if (!id.equals(NOT_MODIFIED)) {
                object.setName(id.toString());
            }
        }

        cache.getCallback().beforeCompletion();
    }
}
