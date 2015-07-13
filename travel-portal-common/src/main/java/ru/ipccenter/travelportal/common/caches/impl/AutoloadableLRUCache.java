package ru.ipccenter.travelportal.common.caches.impl;

import org.apache.commons.collections.map.LRUMap;
import org.apache.log4j.Logger;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Ivan on 01.12.2014.
 */
public final class AutoloadableLRUCache<K, V> extends AbstractCacheImplementationAutoloadable<K, V> {

    private final ReentrantReadWriteLock.ReadLock  readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;

    private Map<K, SoftReference<V>> cache;

    public AutoloadableLRUCache() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock  = lock.readLock();
        this.writeLock = lock.writeLock();
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
        V value = null;

        readLock.lock();
        try {
            SoftReference<V> reference = cache.get(key);
            V hardReference = null;
            if (reference != null) {
                if ((hardReference = reference.get()) == null) {
                    shouldRemove = true;
                }
            }
            value = hardReference;
        } finally {
            readLock.unlock();
        }

        if (shouldRemove) { evict(key); }
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
                "size=" + ((LRUMap)cache).maxSize() +
                "loadCallback=" + getLoadCallback() +
                ']';
    }

    @Override
    public void build() {
        if (getLoadCallback() == null) {
            throw new IllegalStateException("Load callback is not set");
        }

        this.cache = new LRUMap(getSize());
        Logger log = getLogger();
        if (log.isTraceEnabled()) {
            log.trace(this);
        }
    }
}
