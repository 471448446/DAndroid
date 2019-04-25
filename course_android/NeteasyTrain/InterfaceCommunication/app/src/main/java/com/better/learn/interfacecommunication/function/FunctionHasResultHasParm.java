package com.better.learn.interfacecommunication.function;

/**
 * Created by better on 2019/4/21 17:12.
 */
public abstract class FunctionHasResultHasParm<R, P> extends Function {
    public FunctionHasResultHasParm(String name) {
        super(name);
    }

    public abstract R function(P parm);
}
