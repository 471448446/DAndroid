# Build

将原始的工程发布到本地Maven
`buildScripts/gradle/common.gradle`文件中可以修改发布的版本信息

```groovy
    ext.ARTIFACT_VERSION = ext.VERSION_NAME + ext.VERSION_SUFFIX
```

执行命令

```bash
./gradlew publish
```

windows电脑可以在路基：`C:\Users\Administrator\.m2\repository\com\tencent\shadow`下看到发布的maven文件。

# 修改处

manager:

```
SamplePluginManager.java
/**
 * @return 宿主中注册的PluginProcessService实现的类名
 */
@Override
protected String getPluginProcessServiceName() {
        return "com.better.app.plugin_host_lib.MainPluginProcessService";
}
com.tencent.shadow.dynamic.impl.WhiteList
```

# 错误集锦

## 找不到插件里面的APK

插件配置错误：

```
shadow {
    // 插件默认添加flavor：normal、plugin,所以下面的APK路基也要修改
    packagePlugin {
        pluginTypes {
            debug {
                ...
                pluginApks {
                    pluginApk1 {
                        ...
                        apkName = 'plugin1-plugin-debug.apk'
                        apkPath = 'plugin_project/plugin1/build/outputs/apk/plugin/debug/plugin1-plugin-debug.apk'
                    }
                }
            }
```

这里的`apkName`，要和`apkPath`中的APK名称一致

## AndroidX

启动插件后报错，导致无法启动插件里面的Activity 报错信息：
`Cannot cast androidx.core.app.CoreComponentFactory to com.tencent.shadow.core.runtime.ShadowAppComponentFactory`

```bash
04-06 16:16:00.248 E/JavaBinder(32468): *** Uncaught remote exception!  (Exceptions are not yet supported across processes.)
04-06 16:16:00.248 E/JavaBinder(32468): java.util.concurrent.ExecutionException: java.util.concurrent.ExecutionException: java.util.concurrent.ExecutionException: java.lang.ClassCastException: Cannot cast androidx.core.app.CoreComponentFactory to com.tencent.shadow.core.runtime.ShadowAppComponentFactory
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.util.concurrent.FutureTask.report(FutureTask.java:123)
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.util.concurrent.FutureTask.get(FutureTask.java:193)
04-06 16:16:00.248 E/JavaBinder(32468): 	at com.tencent.shadow.dynamic.loader.impl.DynamicPluginLoader.loadPlugin(DynamicPluginLoader.kt:82)
04-06 16:16:00.248 E/JavaBinder(32468): 	at com.tencent.shadow.dynamic.loader.impl.PluginLoaderBinder.onTransact(PluginLoaderBinder.kt:43)
04-06 16:16:00.248 E/JavaBinder(32468): 	at android.os.Binder.execTransact(Binder.java:739)
04-06 16:16:00.248 E/JavaBinder(32468): Caused by: java.util.concurrent.ExecutionException: java.util.concurrent.ExecutionException: java.lang.ClassCastException: Cannot cast androidx.core.app.CoreComponentFactory to com.tencent.shadow.core.runtime.ShadowAppComponentFactory
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.util.concurrent.FutureTask.report(FutureTask.java:123)
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.util.concurrent.FutureTask.get(FutureTask.java:193)
04-06 16:16:00.248 E/JavaBinder(32468): 	at com.tencent.shadow.core.loader.blocs.LoadPluginBloc$loadPlugin$buildRunningPlugin$1.run(Unknown Source:51)
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:458)
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.lang.Thread.run(Thread.java:764)
04-06 16:16:00.248 E/JavaBinder(32468): Caused by: java.util.concurrent.ExecutionException: java.lang.ClassCastException: Cannot cast androidx.core.app.CoreComponentFactory to com.tencent.shadow.core.runtime.ShadowAppComponentFactory
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.util.concurrent.FutureTask.report(FutureTask.java:123)
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.util.concurrent.FutureTask.get(FutureTask.java:193)
04-06 16:16:00.248 E/JavaBinder(32468): 	at com.tencent.shadow.core.loader.blocs.LoadPluginBloc$loadPlugin$buildApplication$1.call(Unknown Source:37)
04-06 16:16:00.248 E/JavaBinder(32468): 	at com.tencent.shadow.core.loader.blocs.LoadPluginBloc$loadPlugin$buildApplication$1.call(Unknown Source:0)
04-06 16:16:00.248 E/JavaBinder(32468): 	... 4 more
04-06 16:16:00.248 E/JavaBinder(32468): Caused by: java.lang.ClassCastException: Cannot cast androidx.core.app.CoreComponentFactory to com.tencent.shadow.core.runtime.ShadowAppComponentFactory
04-06 16:16:00.248 E/JavaBinder(32468): 	at java.lang.Class.cast(Class.java:2441)
04-06 16:16:00.248 E/JavaBinder(32468): 	at com.tencent.shadow.core.loader.blocs.LoadPluginBloc$loadPlugin$buildAppComponentFactory$1.call(Unknown Source:36)
04-06 16:16:00.248 E/JavaBinder(32468): 	at com.tencent.shadow.core.loader.blocs.LoadPluginBloc$loadPlugin$buildAppComponentFactory$1.call(Unknown Source:0)
04-06 16:16:00.248 E/JavaBinder(32468): 	... 4 more
04-06 16:16:00.265 D/ActivityManager( 1335): protection state false
```

报错的代码位于`com.tencent.shadow.core.loader.blocs.LoadPluginBloc`中：

```kotlin
    val clazz = pluginClassLoader.loadClass(pluginInfo.appComponentFactory)
ShadowAppComponentFactory::class.java.cast(clazz.newInstance())
```

[#474](https://github.com/Tencent/Shadow/issues/474)
[#187](https://github.com/Tencent/Shadow/issues/187)
[#237](https://github.com/Tencent/Shadow/issues/237)

> 就是runtime.apk里打包了androidx.core.app.CoreComponentFactory [#474(comment)](https://github.com/Tencent/Shadow/issues/474#issuecomment-863008657)
> 应该是你的runtime的这个依赖把androidx.core.app.CoreComponentFactory打包进去了。这个runtime是为了动态加载activity-container的，所以它是PathClassLoader的parent，这里面是不能有其他类的。只需要把这个依赖去掉就可以了。[#529 (comment)](https://github.com/Tencent/Shadow/issues/529#issuecomment-862977957)

检查Runtime或者Loader生成的APK中`androidx.core.app.CoreComponentFactory`类：

```java
public class AppComponentFactory extends android.app.AppComponentFactory {
}
```

它并没有被转换成`com.tencent.shadow.core.runtime.ShadowAppComponentFactory`

这就好理解了： 启动插件时，先加载PluginManager,再由PluginManager去加载Plugin.zip文件中的信息，先加载loader和runtime，再加载各个插件apk文件。

1. 如果Runtime或者Loader中打包了Androidx中的信息，这里主要是报错的`androidx.core.app.CoreComponentFactory`这个类。
2. 插件在加载时，会先加载Loader和Runtime的APK信息，那么里面的class就会被加载进来。那么类`CoreComponentFactory`也被加载进来，但是这个类又没有被转换。
3. 再加载Plugin的APK时，`CoreComponentFactory`
   类已经被加载了，ClassLoader是不会再次加载这个类的。但是这个类又是明确需要类转换成`ShadowAppComponentFactory`
   的，这就发送了类转换错误了。

修改方式：在`Loader`和`Runtime`的模块中，移除androidx的依赖。

因为这两个模块的功能单一，所以将不必要的依赖也都移除了。