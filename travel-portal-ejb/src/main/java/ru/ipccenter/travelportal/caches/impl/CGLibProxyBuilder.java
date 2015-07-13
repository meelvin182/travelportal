package ru.ipccenter.travelportal.caches.impl;

import net.sf.cglib.proxy.Enhancer;
import ru.ipccenter.travelportal.caches.CGLibProxy;

import java.util.Set;

/**
 * Created by Ivan on 21.03.2015.
 */
public class CGLibProxyBuilder {
    @SuppressWarnings("unchecked")
    public static<W> W buildProxy(W wrapped, Set<W> modifiedObjects) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(wrapped.getClass());
        enhancer.setInterfaces(new Class[] { CGLibProxy.class });
        enhancer.setCallbackFilter(CGLibProxyCallbackFilter.getInstance());
        enhancer.setCallbacks(CGLibProxyCallbackFilter.buildCallbacks(wrapped, modifiedObjects));
        return (W) enhancer.create();
    }
}
