package ru.ipccenter.travelportal.common.caches;

/**
 *
 * @author Ivan Penkin
 * @param <K>
 * @param <V>
 */
public interface Cache<K, V> {
    void put(K key, V value);
    V get(K key);
    void evict();
    void evict(K key);
}
