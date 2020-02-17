package com.better.learn.kt.variance.builder1;

import java.util.Date;

/**
 * Created by better on 2020-01-12 09:15.
 */
public class Employee extends Person {

    public static class EmployeeBuilder extends Person.PersonBuilder {
        Date hiringData;

        public EmployeeBuilder withHiringDate(Date date) {
            hiringData = date;
            return this;
        }

//        public EmployeeBuilder withName(String name) {
//            return (EmployeeBuilder) super.withName(name);
//        }

    }
}
