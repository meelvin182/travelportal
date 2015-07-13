/*
 * Here comes the text of your license
 * Each line should be prefixed with  *
 */

package ru.ipccenter.travelportal.caches;

import ru.ipccenter.travelportal.common.caches.Cache;

import javax.ejb.Local;
import java.io.Serializable;

/**
 *
 * @author Ivan Penkin
 */
@Local
public interface CacheManager extends Serializable {
    void evictAll();
    void evict(Class clazz);
    <T extends Cache> T buildCache(Class<T> clazz) throws InstantiationException, IllegalAccessException;
}
