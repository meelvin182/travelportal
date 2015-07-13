package ru.ipccenter.travelportal.common.caches.impl;

import org.apache.commons.collections.map.LRUMap;
import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.caches.AbstractCacheWrapper;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Ivan on 01.12.2014.
 */
public final class SimpleLRUCache<K, V> extends AbstractCacheImplementation<K, V> {

    private final ReentrantReadWriteLock.ReadLock  readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;
    private Map<K, SoftReference<V>> cache;

    public SimpleLRUCache(AbstractCacheWrapper wrapper) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock  = lock.readLock();
        writeLock = lock.writeLock();
    }

    public void put(K key, V value) {
        if (key == null || value == null) { return; }

        writeLock.lock();
        try {
            cache.put(key, new SoftReference<V>(value));
        } finally {
            writeLock.unlock();
        }
    }

    public V get(K key) {
        if (key == null) { return null; }

        boolean shouldRemove = false;

        readLock.lock();
        try {
            SoftReference<V> reference = cache.get(key);
            V hardReference = null;

            if (reference == null) {
                return null;
            } else if ((hardReference = reference.get()) != null) {
                return hardReference;
            } else {
                shouldRemove = true;
            }
        } finally {
            readLock.unlock();
        }

        if (shouldRemove) { evict(key); }

        return null;
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
                "size=" + ((LRUMap)cache).maxSize() +
                ']';
    }

    @Override
    public void build() {
        Integer cacheSize = getSize();
        this.cache = new LRUMap(cacheSize);
        Logger log = getLogger();
        if (log.isTraceEnabled()) {
            log.trace(this);
        }
    }
}
