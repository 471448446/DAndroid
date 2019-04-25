package com.better.learn.ioc.lib;

import android.app.Activity;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 利用反射 达到注解目的
public class InjectManager {

    public static void inject(Activity activity) {
        injectContent(activity);
        injectView(activity);
        injectEvent(activity);
    }

    private static void injectEvent(Activity activity) {
        Class<?> clsActivity = activity.getClass();
        Method[] methods = clsActivity.getDeclaredMethods();
        for (Method method : methods) {
            // 获取方法的所有注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                // 获取注解的类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (OnClick.class != annotationType &&
                        OnLongClick.class != annotationType) {
                    continue;
                }
                // 找到Listener包裹对象
                ListenerDes listenerDes = annotationType.getAnnotation(ListenerDes.class);
                if (null == listenerDes) {
                    continue;
                }
                try {
                    //代理对象
                    ListenerHandler handler = new ListenerHandler(activity);
                    handler.addInvokeMethod(listenerDes.callback(), method);
                    Object listener =
                            Proxy.newProxyInstance(listenerDes.type().getClassLoader(),
                                    new Class[]{listenerDes.type()},
                                    handler);

                    // 获取方法注解值，这里并没有直接通过getValue()直接获取，而是通过反射获取，兼容更多的类型
                    Method value = annotation.getClass().getDeclaredMethod("value");
                    int[] viewIds = (int[]) value.invoke(annotation);
                    for (int viewId : viewIds) {
                        View view = activity.findViewById(viewId);
                        Method setter = view.getClass().getMethod(listenerDes.setter(), listenerDes.type());

                        setter.invoke(view, listener);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private static void injectEvent(Activity activity) {
//        Class<?> cls = activity.getClass();
//        Method[] methods = cls.getDeclaredMethods();
//        for (Method method : methods) {
//            OnClick onClick = method.getAnnotation(OnClick.class);
//            if (null == onClick) {
//                continue;
//            }
//            try {
//                // 获取注解的Class并不是onClick.getClass
//                Class clsOnClick = onClick.annotationType();
//                ListenerDes listenerDes = (ListenerDes) clsOnClick.getAnnotation(ListenerDes.class);
//                if (null == listenerDes) {
//                    continue;
//                }
//                int[] viewId = onClick.value();
//                for (int id : viewId) {
//                    View view = activity.findViewById(id);
//                    Class clsView = view.getClass();
//                    Method setMethod = clsView.getMethod(listenerDes.setter(), listenerDes.type());
//                    setMethod.setAccessible(true);
//                    setMethod.invoke(view, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Log.e("Better","_______");
//                        }
//                    });
//
////                    ListenerHandler handler = new ListenerHandler();
////                    setMethod.invoke(view, Proxy.newProxyInstance(handler.getClass().getClassLoader(),
////                            listenerDes.type().getInterfaces(), handler));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private static void injectView(Activity activity) {
        Class<?> cls = activity.getClass();
        Field[] fields = cls.getDeclaredFields();
        if (fields.length == 0) {
            return;
        }
        Bind bind;
        for (Field field : fields) {
            field.setAccessible(true);
            bind = field.getAnnotation(Bind.class);
            if (null == bind) {
                continue;
            }
            View v = activity.findViewById(bind.value());
            try {
                field.set(activity, v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectContent(Activity activity) {
        Class<?> cls = activity.getClass();
        ContentView contentView = cls.getAnnotation(ContentView.class);
        if (null != contentView) {
//            // method1
//            activity.setContentView(contentView.value());
            // method2
            try {
                Method setView = cls.getMethod("setContentView", int.class);
                setView.setAccessible(true);
                setView.invoke(activity, contentView.value());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
