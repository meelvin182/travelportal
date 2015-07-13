package ru.ipccenter.travelportal.common.caches;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.caches.impl.AbstractCacheImplementation;
import ru.ipccenter.travelportal.common.caches.impl.CacheBuilder;

/**
 *
 * @author Ivan Penkin
 * @param <K>
 * @param <V>
 */
public abstract class AbstractCacheWrapper<K, V> extends AbstractCache<K, V> {

    private AbstractCacheImplementation<K, V> cacheImpl;

    public AbstractCacheWrapper(Class implementation) {
        super();
        Integer size = getDefaultSize();
        try {
            size = getSize();
        } catch (Exception e) {
            getLogger().warn(e.getMessage() + "; Used default size: " + size);
        }

        this.cacheImpl = CacheBuilder.buildCache(implementation, getLogger(), size);
    }

    public AbstractCacheWrapper(Class implementation, LoadCallback<K,V> loadCallback) {
        super(loadCallback);
        Integer size = getDefaultSize();
        try {
            size = getSize();
        } catch (Exception e) {
            getLogger().warn(e.getMessage() + "; Used default size: " + size);
        }

        this.cacheImpl = CacheBuilder.buildCache(implementation, loadCallback, getLogger(), size);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "["
                + "implementation=" + cacheImpl
                + "]";
    }

    @Override
    public void put(K key, V value) {
        cacheImpl.put(key, value);
    }

    @Override
    public V get(K key) {
        return cacheImpl.get(key);
    }

    @Override
    public void evict() {
        cacheImpl.evict();
    }

    @Override
    public void evict(K key) {
        cacheImpl.evict(key);
    }

    protected abstract Logger getLogger();
    protected abstract Integer getDefaultSize();
    protected abstract Integer getSize() throws Exception;
}
