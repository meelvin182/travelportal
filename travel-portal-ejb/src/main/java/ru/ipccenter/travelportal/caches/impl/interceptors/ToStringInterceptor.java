package ru.ipccenter.travelportal.caches.impl.interceptors;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by Ivan on 21.03.2015.
 */
public class ToStringInterceptor implements MethodInterceptor {

    public static final ToStringInterceptor INSTANCE = new ToStringInterceptor();

    private ToStringInterceptor() {

    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return o.getClass() + "#" + System.identityHashCode(o);
    }
}
