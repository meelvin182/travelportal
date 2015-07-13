package ru.ipccenter.travelportal.common.caches.impl;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.caches.Cache;

/**
 *
 * @author Ivan Penkin
 * @param <K>
 * @param <V>
 */
public abstract class AbstractCacheImplementation<K, V> implements Cache<K, V> {

    private Logger logger;
    private Integer size;

    public Logger getLogger() {
        return logger;
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Integer getSize() {
        return size;
    }

    void setSize(Integer size) {
        this.size = size;
    }

    public abstract void build();
}
