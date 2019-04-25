package com.better.learn.interfacecommunication.function;

/**
 * Created by better on 2019/4/21 17:11.
 */
public abstract class FunctionHasResultNoParm<R> extends Function {
    public FunctionHasResultNoParm(String name) {
        super(name);
    }

    public abstract R function();
}
