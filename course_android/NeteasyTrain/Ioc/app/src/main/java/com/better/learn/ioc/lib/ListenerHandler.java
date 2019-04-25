package com.better.learn.ioc.lib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ListenerHandler implements InvocationHandler {
    // 被代理的对象
    Object target;
    // 准备用来拦截的方法
    private Map<String, Method> invokeMethod = new HashMap<>();

    public ListenerHandler(Object target) {
        this.target = target;
    }

    public void addInvokeMethod(String name, Method method) {
        invokeMethod.put(name, method);
    }

    // 需要代理的对象
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取代理的方法名称 onClick(View)
        String name = method.getName();
        // 对应的需要拦截的方法 被注解的方法
        Method methodInvoke = invokeMethod.get(name);
        if (null != methodInvoke) {
            // 配置了拦截的方法
            if (methodInvoke.getGenericParameterTypes().length == 0) {
                return methodInvoke.invoke(target);
            }
            return methodInvoke.invoke(target, args);
        }
        return null;
    }
}
