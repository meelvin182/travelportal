package ru.ipccenter.travelportal.ejb.stateful;

import ru.ipccenter.travelportal.caches.CGLibProxy;
import ru.ipccenter.travelportal.caches.SessionCacheProvider;
import ru.ipccenter.travelportal.common.model.TPListAttributeFactory;
import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.metamodel.Dao;
import ru.ipccenter.travelportal.metamodel.entities.MMObject;

import javax.ejb.EJB;
import javax.ejb.Remove;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by user on 22.04.2015.
 */
public abstract class AbstractTPObject implements TPObject {

    @EJB
    protected SessionCacheProvider cacheProvider;
    @EJB
    protected TPObjectFactory objectFactory;
    @EJB
    protected TPListAttributeFactory listAttributeFactory;
    @EJB
    private Dao dao;

    private BigInteger id;

    public String getName() {
        return cacheProvider.provide().getObject(getId()).getName();
    }
    public void setName(String name) {
        cacheProvider.provide().getObject(getId()).setName(name);
    }

    @Override
    public BigInteger getId() {
        return id;
    }

    @Override
    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public BigInteger getParentId() {
        return cacheProvider.provide().getObject(getId()).getParent().getId();
    }

    @Override
    public void setParentId(BigInteger id) {
        MMObject parent = cacheProvider.provide().getObject(id);
        if (parent instanceof CGLibProxy) {
            parent = ((CGLibProxy<MMObject>) parent).getWrapped();
        }
        cacheProvider.provide().getObject(getId()).setParent(parent);
    }

    @Override
    public List<BigInteger> getChildIds() {
        return cacheProvider.provide().getObject(getId()).getChildIds();
    }

    @Override
    @Remove
    public void delete() {
        MMObject object = cacheProvider.provide().getObject(id);
        cacheProvider.provide().evictObject(id);

        if (object instanceof CGLibProxy) {
            dao.delete(((CGLibProxy)object).getWrapped());
        } else {
            dao.delete(object);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractTPObject)) return false;

        AbstractTPObject that = (AbstractTPObject) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Remove
    @Override
    public void unused() {
        id = null;
    }
}
