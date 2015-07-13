package ru.ipccenter.travelportal.caches.impl.interceptors;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by Ivan on 21.03.2015.
 */
public class GetWrappedInterceptor implements MethodInterceptor {

    private Object wrapped;

    public GetWrappedInterceptor(Object wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        return wrapped;
    }
}
