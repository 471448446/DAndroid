package com.better.learn.interfacecommunication.function;

/**
 * 方法：名称、参数、函数体、返回值
 * Created by better on 2019/4/21 17:06.
 */
public abstract class Function {
    // 方法名称
    String name;

    public String getName() {
        return name;
    }

    public Function(String name) {
        this.name = name;
    }
}
