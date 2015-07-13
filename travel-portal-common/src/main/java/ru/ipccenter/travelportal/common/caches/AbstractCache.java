package ru.ipccenter.travelportal.common.caches;

/**
 *
 * @author Ivan Penkin
 * @param <K>
 * @param <V>
 */
public abstract class AbstractCache<K, V> implements Cache<K,V>{

    protected final boolean autoLoadable;
    protected final LoadCallback<K, V> loadCallback;

    public AbstractCache(LoadCallback<K,V> loadCallback) {
        this.autoLoadable = true;
        this.loadCallback = loadCallback;
    }

    public AbstractCache() {
        this.autoLoadable = false;
        this.loadCallback = null;
    }
}
