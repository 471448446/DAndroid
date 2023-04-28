package com.better.proxycrash;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Cooker {
    public interface ScheduleListener {
        void onDone();
    }

    private ScheduleListener listener;

    public void setListener(ScheduleListener listener) {
        this.listener = getBitmapListenerProxy(listener);
    }

    public void cook() {
        try {
            // 伪代码
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        listener.onDone();
    }

    public static Cooker.ScheduleListener getBitmapListenerProxy(final Cooker.ScheduleListener listener) {
        Class<?>[] interfaces = listener.getClass().getSuperclass().getInterfaces();
        Log.e("Better", "now print listener ↓," + listener.getClass().getName());
        for (Class<?> anInterface : interfaces) {
            Log.e("Better", "listener:getInterfaces:" + anInterface.getName());
        }

        Object instance = Proxy.newProxyInstance(Cooker.class.getClassLoader(),
                interfaces, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
                        try {
                            Object object = method.invoke(listener, args);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
        Log.e("Better", "now print ProxyInstance ↓," + instance.getClass().getName());
        Class<?> instanceClass = instance.getClass();
        Class<?>[] instanceClassInterfaces = instanceClass.getInterfaces();
        for (Class<?> anInterface : instanceClassInterfaces) {
            Log.e("Better", "ProxyInstance:getInterfaces:" + anInterface);
        }
        Log.e("Better", "now print match ↓");
        Log.e("Better", "1" + instance.getClass().getName() + "," + instance.getClass().getClassLoader().hashCode());
        Log.e("Better", "2" + Cooker.ScheduleListener.class.getName() + "," + Cooker.ScheduleListener.class.getClassLoader().hashCode());
        return (Cooker.ScheduleListener) instance;
    }

}
