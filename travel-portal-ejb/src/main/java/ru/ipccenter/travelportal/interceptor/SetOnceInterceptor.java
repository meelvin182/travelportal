package ru.ipccenter.travelportal.interceptor;

/**
 * Created by meelvin182 on 29.03.15.
 */

import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.interceptor.binding.SetOnce;


import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;


@SetOnce
@Interceptor
public class SetOnceInterceptor {
    @AroundInvoke
    public Object checker(InvocationContext ctx) throws Exception{
       TPObject o = (TPObject) ctx.getTarget();
       if (o.getId() !=null){
           throw new RuntimeException("Id has already been set");
       } else {
           return ctx.proceed();
       }
   }
}
