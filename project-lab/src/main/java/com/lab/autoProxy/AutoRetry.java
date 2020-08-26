package com.lab.autoProxy;


import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

@Data
public class AutoRetry implements MethodInterceptor {

    Object Target;

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Method method = mi.getMethod();
        System.out.println("开始做一些事情");
        Object o = method.invoke(getTarget(),mi.getArguments());
        System.out.println("结束了做一些事情");
        return o;
    }
}
