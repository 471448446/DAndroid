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