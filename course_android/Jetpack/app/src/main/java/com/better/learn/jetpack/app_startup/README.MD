[app-startup](https://developer.android.com/topic/libraries/app-startup)
```
androidx.startup:startup-runtime:1.1.0
```
用于初始化任务。
常规的方式将所有的初始化逻辑放在`Application`中，如果初始化有依赖性，代码放在前面一点就行。这种方式会有很多的初始化代码集中再一起，造成`Application`类比较臃肿。
后来又衍生出写一个单独的`Provider`来专门初始化一个业务逻辑（这是利用了进程启动时，安装`Provider`的时机时介于`Application.attachBaseContext()`和`Application.onCreate()`之间的特性）。将不同业务的初始化才分开了，很多第三方的SDK都这样搞。这样也有有些问题的：
1. 过多的`Provider`会影响应用的进程初始化，启动变慢。
2. `Provider`的安装是没有顺序，不同`Provider`之间没有关联性。如果`A`的初始化需要依赖`B`，就难搞了。

所以为了解决这个初始化的依赖问题，同时为了规范初始化逻辑，整了个`App Start`组件。
用`Initializer`类，来完成一个初始化动作。
* `create()`方法，用于初始化某个业务逻辑。
* `dependencies`方法，用于申明这个业务罗需要依赖的其他的逻辑。

```
public interface Initializer<T> {
    T create(@NonNull Context context);
    List<Class<? extends Initializer<?>>> dependencies();
}
```
最后在`Manifest`中注册`Provider`。这样当应用启动的时候会安装`Provider`,进而获取申明的初始化类信息，完成初始化。
```
<provider
    android:name="androidx.startup.InitializationProvider"
    android:authorities="${applicationId}.androidx-startup"
    android:exported="false"
    tools:node="merge">
    <meta-data  android:name="com.example.ExampleLoggerInitializer"
          android:value="androidx.startup" />
</provider>
```
如果不使用进程启动后自动初始化功能。只需要将`Provider`移除，在代码中调用`AppInitializer.getInstance(context).initializeComponent(ExampleLoggerInitializer::class.java)`手动初始化就行。
移除方式：
```
<provider
    android:name="androidx.startup.InitializationProvider"
    android:authorities="${applicationId}.androidx-startup"
    tools:node="remove" />
```
对于第三方SDK可以暴露`Initializer`给外部，由外部来决定什么时候初始化。这样初始化某个业务逻辑的实现方式就统一了，更规范了。

实现细节
打开`InitializationProvider`代码。`onCreate()`处获取`meta-data`中的`android:name`所以代表的`Initializer`，通过反射创建`Initializer`对象，如果通过`dependencies()`发现有依赖的`Initializer`，先完成依赖的初始化，最后调用`create()`繁繁完成自己初始化。完成初始化。
因为用到了反射，所以通过`Provider`的这种方式需要注意在`meta-data`中申明的类不要被混淆了，不然因为找不到类名，而无法初始化。
```
    @Override
    public final boolean onCreate() {
        Context context = getContext();
        if (context != null) {
            AppInitializer.getInstance(context).discoverAndInitialize();
        } else {
            throw new StartupException("Context cannot be null");
        }
        return true;
    }
```