演示注解使用

1. `com.better.app.annotation.source` 演示使用源码级别的注解 
    * `com.better.app.annotation.apt` 使用apt的注解，这里注解的保留类型也是源码 demo参考：[annotationprocessing101](https://github.com/sockeqwe/annotationprocessing101)
2. `com.better.app.annotation.cls` 演示的使用`CLASS`级别的注解
3. `com.better.app.annotation.runtime` 演示的使用`RUNTIME`级别的注解

一般而言，apt的工程模板如下：
1. `xxx-annotation`（Java Library） 注解定义的地方
2. `xxx-compiler` （Java Library） 注解解析的地方
3. `xxx-api` （Android Library） 这是一个Android的依赖工程，注解对外提供API的地方，依赖模块要向外部暴露`xxx-annotation`。