package ru.ipccenter.travelportal.caches.impl;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.NoOp;
import ru.ipccenter.travelportal.caches.impl.interceptors.AnyInterceptor;
import ru.ipccenter.travelportal.caches.impl.interceptors.GetWrappedInterceptor;
import ru.ipccenter.travelportal.caches.impl.interceptors.SetInterceptor;
import ru.ipccenter.travelportal.caches.impl.interceptors.ToStringInterceptor;

import java.util.Set;

/**
 * Created by Ivan on 21.03.2015.
 */
public class CGLibProxyCallbackFilter implements CallbackFilter {

        private static final CGLibProxyCallbackFilter instance = new CGLibProxyCallbackFilter();

        public static <W> Callback[] buildCallbacks(W wrapped, Set<W> modifiedObjects) {
            return new Callback[] {
                    NoOp.INSTANCE, /* finalize */
                    ToStringInterceptor.INSTANCE, /* toString */
                    new GetWrappedInterceptor(wrapped), /* getWrapped */
                    new SetInterceptor<W>(wrapped, modifiedObjects), /* set */
                    new AnyInterceptor<W>(wrapped) /* any */
            };
        }

        private enum Method {
            finalize,
            toString,
            getWrapped,
            set,
            any;

            public static Method valueOf(java.lang.reflect.Method method) {
                if (method.getName().startsWith("set")) {
                    return set;
                } else if (method.getName().equalsIgnoreCase("toString")) {
                    return toString;
                } else if (method.getName().equalsIgnoreCase("getWrapped")) {
                    return getWrapped;
                } else if (method.getName().equalsIgnoreCase("finalize")) {
                    return finalize;
                } else {
                    return any;
                }
            }
        }

        private CGLibProxyCallbackFilter() {

        }

        @Override
        public int accept(java.lang.reflect.Method method) {
            return Method.valueOf(method).ordinal();
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof CGLibProxyCallbackFilter && o == this);
        }

        public static CGLibProxyCallbackFilter getInstance() {
            return instance;
        }
    }
