package com.better.learn.kt.variance.builder;

import java.util.Date;

/**
 * Created by better on 2020-01-12 09:15.
 */
public class Employee extends Person {

    public static class EmployeeBuilder extends PersonBuilder<Employee.EmployeeBuilder> {
        Date hiringData;

        public EmployeeBuilder withHiringDate(Date date) {
            hiringData = date;
            return this;
        }

        @Override
        EmployeeBuilder selfType() {
            return new EmployeeBuilder();
        }
    }
}
