/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.ipccenter.travelportal.common.utils;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.caches.UnlimitedCache;

import javax.naming.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ivan Penkin
 */
public class BeanLookupHelper {

    private static final Logger LOG = Logger.getLogger(BeanLookupHelper.class);
    private static final String[] TOP_NAMESPACES = new String[] {
            "java:global",
            "java:jboss"
    };

    private static final JndiPathCache JNDI_PATH_CACHE = new JndiPathCache();

    private static class JndiPathCache extends UnlimitedCache<Class, String> {

        private static final Logger LOG = Logger.getLogger(JndiPathCache.class);

        protected Logger getLogger() {
            return LOG;
        }

        @Override
        protected Integer getDefaultSize() {
            return 256;
        }

        @Override
        protected Integer getSize() throws Exception {
            return 256;
        }
    }

    public static <D> D lookup(Class<D> clazz) throws NamingException {
        InitialContext ctx = new InitialContext();
        return lookupUseCache(clazz, ctx);
    }

    public static <D> D lookup(Class<D> clazz, Hashtable<?, ?> environment) throws NamingException {
        InitialContext ctx = new InitialContext(environment);
        return lookupUseCache(clazz, ctx);
    }

    @SuppressWarnings("unchecked")
    private static <D> D lookupUseCache(Class<D> clazz, InitialContext ctx) throws NamingException {
        String path = JNDI_PATH_CACHE.get(clazz);
        try {

            if (path == null) {
                //            path = DEFAULT_JNDI_PATH
                //                + clazz.getSimpleName().replace("Local", "")
                //                + ((local)? "!" : "Bean!")
                //                + clazz.getName();
                path = findJNDIName(ctx, clazz);
                JNDI_PATH_CACHE.put(clazz, path);
            }
        } catch (Throwable t) {
            LOG.error(t);
            throw t;
        }

        return (D) ctx.lookup(path);
    }

    private static String findJNDIName(Context ctx, Class clazz) throws NamingException {
        if (LOG.isTraceEnabled()) LOG.trace("Finding JNDI name: " + clazz.getCanonicalName());

        for (String topNamespace: TOP_NAMESPACES) {
            NamingEnumeration list = ctx.listBindings(topNamespace);
            List<String> jndiPath = new LinkedList<>();
            jndiPath.add(topNamespace);

            while (list.hasMore()) {
                Binding item = (Binding) list.next();
                String name = item.getName();

                jndiPath.add(name);

                if (name.contains(clazz.getCanonicalName()) || name.contains(clazz.getSimpleName())) {
                    String path = buildPath(jndiPath);
                    if (LOG.isTraceEnabled())
                        LOG.trace("JNDI name: " + clazz.getCanonicalName() + " was found on: " + path);
                    return path;
                } else {
                    Object o = item.getObject();
                    if (o instanceof Context && findJNDINameDeep((Context) o, clazz.getCanonicalName(), jndiPath)) {
                        String path = buildPath(jndiPath);
                        if (LOG.isTraceEnabled())
                            LOG.trace("JNDI name: " + clazz.getCanonicalName() + " was found on: " + path);
                        return path;
                    } else {
                        int size = jndiPath.size();
                        if (size > 0) {
                            jndiPath.remove(size - 1);
                        }
                    }
                }
            }
        }

        throw new NamingException("JNDI path for " + clazz.getCanonicalName() + "is not found");
    }

    private static boolean findJNDINameDeep(Context ctx, String className, List<String> jndiPath) throws NamingException {
        NamingEnumeration list = null;
        try {
             list = ctx.listBindings("");
        } catch (Throwable t) {
            LOG.warn("Can't get list bindings for: " + buildPath(jndiPath), t);
            return false;
        }

        while (list.hasMore()) {
            Binding item = (Binding) list.next();
            String name = item.getName();
            jndiPath.add(name);

            if (name.contains(className)) {
                return true;
            } else {
                Object o = item.getObject();
                if (o instanceof Context && findJNDINameDeep((Context) o, className, jndiPath)) {
                    return true;
                } else {
                    int size = jndiPath.size();
                    if (size > 0) {
                        jndiPath.remove(size - 1);
                    }
                }
            }
        }

        return false;
    }

    private static String buildPath(List<String> path) {
        StringBuilder jndiPathBuilder = new StringBuilder();
        boolean first = true;
        for (String element: path) {
            if (!first) { jndiPathBuilder.append('/'); } else { first = false; }
            jndiPathBuilder.append(element);
        }
        return jndiPathBuilder.toString();
    }
}
