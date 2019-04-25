package com.better.learn.interfacecommunication.function;

/**
 * Created by better on 2019/4/21 17:21.
 */
public abstract class FunctionNoResultHasParm<P> extends Function {

    public FunctionNoResultHasParm(String name) {
        super(name);
    }

    public abstract void function(P prams);
}
