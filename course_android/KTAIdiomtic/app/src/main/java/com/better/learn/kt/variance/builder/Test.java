package com.better.learn.kt.variance.builder;


import java.util.Date;

/**
 * Created by better on 2020-01-12 09:17.
 */
public class Test {
    public static void main(String[] args) {
        new Employee.EmployeeBuilder()
                .withName("Haha")
                .withHiringDate(new Date())
                .build();
    }
}
