package com.better.learn.kt.variance.builder1;

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

    public static class PersonBuilder {
        private String name;

        public PersonBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public Person build() {
            Person person = new Person();
            person.setName(name);
            return person;
        }
    }
}
