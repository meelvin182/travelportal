package ru.ipccenter.travelportal.common.caches.impl;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.caches.LoadCallback;

/**
 * Created by Ivan on 19.03.2015.
 */
public class CacheBuilder {
    public static <K, V, T extends AbstractCacheImplementation<K, V>> T buildCache(
            Class<T> clazz,
            LoadCallback<K, V> loadCallback,
            Logger logger, Integer size) {
        try {
            T cache = clazz.newInstance();
            ((AbstractCacheImplementationAutoloadable)cache).setLoadCallback(loadCallback);
            cache.setLogger(logger);
            cache.setSize(size);
            cache.build();
            return cache;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <K, V, T extends AbstractCacheImplementation<K, V>> T buildCache(
            Class<T> clazz,
            Logger logger, Integer size) {
        try {
            T cache = clazz.newInstance();
            cache.setLogger(logger);
            cache.setSize(size);
            cache.build();
            return cache;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
