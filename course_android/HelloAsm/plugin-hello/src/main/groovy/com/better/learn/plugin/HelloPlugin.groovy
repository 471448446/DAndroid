package com.better.learn.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class HelloPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        /**
         * 插件被调用时
         */
        System.out.println("========== [HelloPlugin]: hello ==========")
    }
}
