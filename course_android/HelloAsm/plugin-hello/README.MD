如何自定义Gradle插件
流程如下,4个步骤
1. 准备插件工程
2. 生成插件
3. 运用插件
4. 检查是否生效

**需要注意`build.gradle`文件中申明的plugin id配置，以及生成`resources/META-INF/xxx.properties`文件，`resources/META-INF`是两个文件夹**

# 准备插件工程
1. 创建Android Library Module，名称任意取`plugin-hello`
2. 删除其中的文件，使得只剩`plugin-hello/src/main/java/package`文件夹,`plugin-hello/build.gradle`。
3. 替换`plugin-hello/build.gradle`中的类容，主要是修改`group`，`artifactId`,`version`，以及生成的插件保存地址`repository()`。
4. 创建插件的实现者，在`plugin-hello/src/main/groovy/package`文件夹,创建插件实现者`HelloPlugin`。内容很简单，只是打印一句日志。
5. 生成插件配置文件`properties`,在`plugin-hello/src/main`文件下下创建`resources/META-INF`文件夹，创建文件`com.better.hello.asm.properties`。
    * 写入内容`implementation-class=com.better.learn.plugin.HelloPlugin`，表明插件的实现类
    * 其中`com.better.hello.asm`,是插件的名称。后面会用到，也就是`apply plugin: 'com.better.hello.asm'`
6. 到此插件的生成准备以完成

# 生成插件
执行module的gradle任务，`uploadArchives`，会生成对应的插件类。

# 运用插件
1. 在主工程中申明插件的`repository`库，这里时放在本地的，所以是`maven { url '../plugin-hello-repo' }`。
2. 最后应用插件`apply plugin: 'com.better.hello.asm'`，这里的plugin名称就是前面生成`properties`文件的文件名

最后运用的代码如下
```groovy
apply plugin: 'com.better.hello.asm'
buildscript {
    repositories {
        google()
        jcenter()
        //自定义插件maven地址
        maven { url '../plugin-hello-repo' }
    }
    dependencies {
        //加载自定义插件 group + module + version
        classpath 'com.better.learn.plugin:hello-asm-plugin:1.0.0'
    }
}
```

# 验证插件是否成功
执行运行APP，或者打包，会先执行自定义的插件，查看输出信息:
```bash
Executing tasks: [:app:assembleDebug] in project D:\WP\gitHub\DAndroid\course_android\HelloAsm

> Configure project :app
========== [HelloPlugin]: hello ==========

> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:generateDebugBuildConfig UP-TO-DATE
...
```