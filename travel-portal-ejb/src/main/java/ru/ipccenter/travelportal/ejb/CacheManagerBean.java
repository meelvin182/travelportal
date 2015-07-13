package ru.ipccenter.travelportal.ejb;

import ru.ipccenter.travelportal.common.caches.Cache;
import ru.ipccenter.travelportal.caches.CacheManager;

import javax.ejb.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ivan Penkin
 */
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton
@Local(CacheManager.class)
public class CacheManagerBean implements CacheManager {

    private final Map<Class, Cache> managedCaches = new ConcurrentHashMap<>();

    @Lock(LockType.WRITE)
    public void evictAll() {
        for (Map.Entry<Class, Cache> cache: managedCaches.entrySet()) {
            cache.getValue().evict();
        }
    }

    @Lock(LockType.WRITE)
    public void evict(Class clazz) {
        Cache cache = managedCaches.get(clazz);
        if (cache != null) {
            cache.evict();
        }
    }

    @Lock(LockType.WRITE)
    public <T extends Cache> T buildCache(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T cache = clazz.newInstance();
        managedCaches.put(clazz, cache);
        return cache;
    }

    public String toString() {
        return getClass().getCanonicalName() + "#" + System.identityHashCode(this);
    }
}
