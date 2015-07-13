package ru.ipccenter.travelportal.common.caches.impl;

import ru.ipccenter.travelportal.common.caches.LoadCallback;

/**
 *
 * @author Ivan Penkin
 * @param <K>
 * @param <V>
 */
public abstract class AbstractCacheImplementationAutoloadable<K, V> extends AbstractCacheImplementation<K, V> {

    private LoadCallback<K, V> loadCallback;

    LoadCallback<K, V> getLoadCallback() {
        return loadCallback;
    }

    void setLoadCallback(LoadCallback<K, V> loadCallback) {
        this.loadCallback = loadCallback;
    }
}
