package com.better.learn.ioc.lib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ListenerHandler implements InvocationHandler {
    // 需要代理的对象
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(args);
    }
}
