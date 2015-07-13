package ru.ipccenter.travelportal.common.caches.impl;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Ivan on 02.12.2014.
 */
public final class AutoloadableUnlimitedCache<K, V> extends AbstractCacheImplementationAutoloadable<K, V> {

    private final ReentrantReadWriteLock.ReadLock  readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;

    private Map<K, V> cache;

    public AutoloadableUnlimitedCache() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock  = lock.readLock();
        this.writeLock = lock.writeLock();
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
        V value = null;

        readLock.lock();
        try {
            value = cache.get(key);
        } finally {
            readLock.unlock();
        }

        if (value == null) {
            value = getLoadCallback().load(key);
            put(key, value);
        }

        return value;
    }

    public void evict() {
        writeLock.lock();
        try {
            cache.clear();
        } finally {
            writeLock.unlock();
        }
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
        if (getLoadCallback() == null) {
            throw new IllegalStateException("Load callback is not set");
        }

        this.cache = new HashMap<>(getSize());
        Logger log = getLogger();
        if (log.isTraceEnabled()) {
            log.trace(this);
        }
    }
}
