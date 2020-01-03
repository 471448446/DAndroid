package com.better.learn.kt.variance;

/**
 * Created by better on 2020-01-02 16:22.
 */
public class Bound {
    public static <U extends Number> void print(U u1) {
        System.out.print(u1);
    }
    /*public static <? extends Number> void print()*/
}
