package ru.ipccenter.travelportal.caches;

import ru.ipccenter.travelportal.metamodel.entities.MMObject;
import ru.ipccenter.travelportal.metamodel.entities.MMParameter;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Ivan on 16.03.2015.
 */
public interface SessionCache {

    public static abstract class SynchronizationCallback {
        public abstract void beforeCompletion();
        public abstract void afterCompletion();
    }

    void evictObject(BigInteger id);

    MMObject getObject(BigInteger id);
    MMParameter getParameter(BigInteger objectId, BigInteger attributeId);
    List<MMParameter> getParameters(BigInteger objectId, BigInteger attributeId);
    List<MMParameter> getParametersWithEmptyLast(BigInteger objectId, BigInteger attributeId);
    SynchronizationCallback getCallback();
}
