package ru.ipccenter.travelportal.common.caches;

import ru.ipccenter.travelportal.common.caches.impl.AutoloadableUnlimitedCache;
import ru.ipccenter.travelportal.common.caches.impl.SimpleUnlimitedCache;

/**
 * Created by Ivan on 02.12.2014.
 */
public abstract class UnlimitedCache<K, V> extends AbstractCacheWrapper<K, V> {

    protected UnlimitedCache() {
        super(SimpleUnlimitedCache.class);
    }

    protected UnlimitedCache(LoadCallback<K, V> loadCallback) {
        super(AutoloadableUnlimitedCache.class, loadCallback);
    }
}
