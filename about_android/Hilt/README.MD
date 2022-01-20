DI

依赖注入基于控制反转原则
有一些库通过自动执行创建和提供依赖项的过程解决此问题。它们归为两类：
* 基于反射的解决方案，可在运行时连接依赖项。
* 静态解决方案，可生成在编译时连接依赖项的代码。

依赖项注入的替代方法是使用服务定位器。服务定位器设计模式还改进了类与具体依赖项的分离。
PS：相当于提供一个全局的Provider类来专门提供所需要的依赖对象。但是这种如果需要改变依赖的对象，就更麻烦了。

Hilt 通过为项目中的每个 Android 类提供容器并自动管理其生命周期，提供了一种在应用中使用 DI（依赖项注入）的标准方法

xml里面声明的需要类，然后绑定。比如点击绑定`android:onClick="@{() -> viewmodel.openTask(task.id)}"`
然后在ViewModel里面通过Livedata向UI层暴露点击事件
```
    viewModel.openTaskEvent.observe(viewLifecycleOwner, EventObserver {
        openTaskDetails(it)
    })
```


错误：
```
Caused by: java.lang.IllegalStateException: Unsupported metadata version. Check that your Kotlin version is >= 1.0
```
[hilt-gradle-setup](https://dagger.dev/hilt/gradle-setup)版本不对，使用2.40.5