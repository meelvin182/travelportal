package ru.ipccenter.travelportal.common.caches.impl;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Ivan on 02.12.2014.
 */
public final class SimpleUnlimitedCache<K, V> extends AbstractCacheImplementationSimple<K, V> {

    private final ReentrantReadWriteLock.ReadLock  readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;
    private Map<K, V> cache;

    public SimpleUnlimitedCache() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock  = lock.readLock();
        writeLock = lock.writeLock();
    }

    public void put(K key, V value) {
        if (key == null || value == null) { return; }

        writeLock.lock();
        try {
            cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public V get(K key) {
        if (key == null) { return null; }

        readLock.lock();
        try {
            return cache.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void evict() {
        writeLock.lock();
        cache.clear();
        writeLock.unlock();
    }

    public void evict(K key) {
        if (key == null) { return; }

        writeLock.lock();
        try {
            cache.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    public String toString() {
        return this.getClass().getCanonicalName() + '[' +
                    "size=" + cache.size() +
                ']';
    }

    @Override
    public void build() {
        Integer cacheSize = getSize();
        this.cache = new HashedMap(cacheSize);
        Logger log = getLogger();
        if (log.isTraceEnabled()) {
            log.trace(this);
        }
    }
}
