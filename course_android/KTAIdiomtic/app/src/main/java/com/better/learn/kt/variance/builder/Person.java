package com.better.learn.kt.variance.builder;

/**
 * Created by better on 2020-01-12 09:11.
 */
public class Person {
    String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static abstract class PersonBuilder<T extends PersonBuilder<T>> {
        private String name;

        public T withName(String name) {
            this.name = name;
            return selfType();
        }

        abstract T selfType();


        public Person build() {
            Person person = new Person();
            person.setName(name);
            return person;
        }
    }
}
