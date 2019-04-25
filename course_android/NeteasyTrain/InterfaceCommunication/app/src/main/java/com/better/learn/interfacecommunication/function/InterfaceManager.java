package com.better.learn.interfacecommunication.function;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Better on 2019/4/21 17:05.
 */
public class InterfaceManager {
    private static final String TAG = "InterfaceManager";
    private static InterfaceManager instance;

    private Map<String, FunctionNoResultNoPrams> noResultNoPram = new HashMap<>();
    private Map<String, FunctionHasResultNoParm> hasResultNoPram = new HashMap<>();
    private Map<String, FunctionHasResultHasParm> hasResultHasPram = new HashMap<>();
    private Map<String, FunctionNoResultHasParm> noResultHasPram = new HashMap<>();

    private InterfaceManager() {

    }

    public static InterfaceManager getInstance() {
        if (null == instance) {
            synchronized (InterfaceManager.class) {
                if (null == instance) {
                    instance = new InterfaceManager();
                }
            }
        }
        return instance;
    }

    public void addFunction(FunctionNoResultNoPrams function) {
        noResultNoPram.put(function.getName(), function);
    }

    public void invoke(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        FunctionNoResultNoPrams f = noResultNoPram.get(name);
        if (null != f) {
            f.function();
        } else {
            Log.w(TAG, name + " null function invvoke");
        }
    }

    public void addFunction(FunctionHasResultNoParm function) {
        hasResultNoPram.put(function.getName(), function);
    }

    public <R> R invoke(String name, Class<R> t) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        FunctionHasResultNoParm f = hasResultNoPram.get(name);
        if (null == f) {
            Log.w(TAG, name + " null function invvoke");
            return null;
        }
        // 转换
        return t.cast(f.function());

    }

    public void addFunction(FunctionNoResultHasParm function) {
        noResultHasPram.put(function.getName(), function);
    }

    public <P> void invoke(String name, P p) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        FunctionNoResultHasParm<P> f = noResultHasPram.get(name);
        if (null != p) {
            f.function(p);
        } else {
            Log.w(TAG, name + " null function invvoke");
        }
    }

    public void addFunction(FunctionHasResultHasParm function) {
        hasResultHasPram.put(function.getName(), function);
    }


    public <R, P> R invoke(String name, Class<R> rClass, P p) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        FunctionHasResultHasParm<R, P> f = hasResultHasPram.get(name);
        if (null == f) {
            Log.w(TAG, name + " null function invvoke");
            return null;
        }
        return rClass.cast(f.function(p));
    }

}
