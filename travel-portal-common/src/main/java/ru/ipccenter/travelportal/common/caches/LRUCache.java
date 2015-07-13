package ru.ipccenter.travelportal.common.caches;

import ru.ipccenter.travelportal.common.caches.impl.AutoloadableLRUCache;
import ru.ipccenter.travelportal.common.caches.impl.SimpleLRUCache;

/**
 * Created by Ivan on 01.12.2014.
 */
public abstract class LRUCache<K, V> extends AbstractCacheWrapper<K, V> {

    public LRUCache() {
        super(SimpleLRUCache.class);
    }

    public LRUCache(LoadCallback<K,V> loadCallback) {
        super(AutoloadableLRUCache.class, loadCallback);
    }
}
