package com.better.learn.kt.variance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by better on 2019-12-26 22:37.
 */
public class JavaVariance {
    public static void main(String[] args) {
        A item = new B();
        B[] b = new B[]{new B(), new B()};
        A[] a = b;
        // generic default invariance
//        List<A> a = new ArrayList<B>();
//        List<B> b = new ArrayList<A>();
        // generic covariance contravariance
        List<? extends A> listA = new ArrayList<B>();
        List<? super B> listB = new ArrayList<A>();
        // try declaration set variance
        //1 generic type fail
//        BoxDeclaration<A> aBoxDeclar = new BoxDeclaration<B>();
        // 2 generic method
        printDeclaration(new B());
    }

    static class BoxDeclaration<T extends A> {

    }

    static <T extends A> void printDeclaration(T r) {
        System.out.print(r);
    }


}
