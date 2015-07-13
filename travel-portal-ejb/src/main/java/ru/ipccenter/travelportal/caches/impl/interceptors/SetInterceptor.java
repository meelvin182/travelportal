package ru.ipccenter.travelportal.caches.impl.interceptors;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by Ivan on 21.03.2015.
 */
public class SetInterceptor<W> implements MethodInterceptor {

    private W wrapped;
    private Set<W> modified;

    public SetInterceptor(W wrapped, Set<W> modified) {
        this.wrapped = wrapped;
        this.modified = modified;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        Object result = method.invoke(wrapped, params);
        modified.add(wrapped);
        return result;
    }
}
