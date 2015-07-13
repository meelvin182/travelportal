package ru.ipccenter.travelportal.common.caches;

/**
 * Created by Ivan on 19.03.2015.
 */
public abstract class LoadCallback<K, V> {
    public abstract V load(K key);
}
