package com.better.learn.kt.variant;

import android.os.Build;

public class PersioneJ {
    static class Builder<T extends Build> {
        private String name;

        public T setName(String name) {
            this.name = name;
            return ;
        }
    }
}
