[datastore](https://developer.android.com/topic/libraries/architecture/datastore)
[博客：优先使用 Jetpack DataStore 存储数据](https://android-developers.googleblog.com/2020/09/prefer-storing-data-with-jetpack.html)
简单的小型数据集的磁盘存储方法，用以替代`SharedPreferences`。键值对的新的实现方式。
而如果是大型数据，应该使用数据库、文件等方式。
`DataStore`提供两种不同的实现：`Preferences DataStore` 和 `Proto DataStore`。
1. `Preferences DataStore` 使用键存储和访问数据。此实现不需要预定义的架构，也不确保类型安全。
2. `Proto DataStore` 将数据作为自定义数据类型的实例进行存储。此实现要求您使用协议缓冲区来定义架构，但可以确保类型安全。

两者的使用方式类似，都是先申明`DataStore`，然后操作值。
|区别|Preferences|Protobuf|
|--|--|--|
|操作值的方式|需要先定义KEY对象，通过键值对的方式|在`.proto`文件中定义的key被转换为对象方法，通过访问对象的方法|
|类型是否安全|不安全，需要强制转换|安全，方法的类型，编译时已经确定|
|保存数据的方式不同|与 SharedPreferences 一样，Preferences DataStore根据键访问数据，而无需事先定义架构|Proto DataStore使用协议缓冲区（Protobuf）来定义架构。使用协议缓冲区可持久保留强类型数据。与 XML 和其他类似的数据格式相比，协议缓冲区速度更快、规格更小、使用更简单，并且更清楚明了。虽然使用 Proto DataStore 需要学习新的序列化机制，但我们认为 Proto DataStore 有着强大的优势，值得学习。|

更多的区别：[DataStore - 基础知识](https://developer.android.com/codelabs/android-proto-datastore#3)

# Preferences DataStore
对于`Key`的表示变化了，在`SharedPreference`中是直接用一个`String`对象，限在需要使用`Preferences.Key`对象来表示。并且提供了便捷方法来构建不同类型的`Key`。比如：
* `intPreferencesKey()`表示构建值是`Int`类型的`Key`
* `stringPreferencesKey()`表示构建值是`String`类型的`Key`

Step1：引用依赖库
```groovy
dependencies {
    implementation "androidx.datastore:datastore-preferences:1.0.0"
}
```

Step2：定义`DataStore
```kotlin
val Context.dataStorePreference: DataStore<Preferences> by preferencesDataStore(name = "settings")
```

Step1：如何使用
申明key：
```kotlin
val key = intPreferencesKey("click_btn_times")
```

读取数据
```kotlin
val times = dataStorePreference.data.map {
    // 取值，以及默认值。[]取出来的类型不一定安全
    it[key] ?: 0
}.first()
```

写入数据
```kotlin
dataStorePreference.edit {
    it[key] = t1
}
```

# Protobuf DataStore
这个使用起来就麻烦一点
Step1：文档上没有说，其实需要先引用protobuf插件。参考，[Proto DataStore - 概览](https://codelabs.developers.google.com/codelabs/android-proto-datastore)
```groovy
plugins {
    id "com.google.protobuf" version "0.8.12"
}
dependencies {
    implementation "com.google.protobuf:protobuf-javalite:3.10.0"
}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.10.0"
    }
    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        all().each { task ->
            task.builtins {
                java {
                    option 'lite'
                }
            }
        }
    }
}
```

Step2：然后才是引用DataStore的Android依赖。
```groovy
dependencies {
    implementation "androidx.datastore:datastore:1.0.0"
}
```

Step3：编写`.proto`文件
```proto
// 申明使用哪个版本语法proto2或proto3
syntax = "proto3";
// 应用包名
option java_package = "com.better.learn.jetpack";
// 是否支持多文件，暂时不清楚干啥用
option java_multiple_files = true;
// 这个是申明的类型，可以理解为最终数据保存再SettingsPb对象中
message SettingsPb {
  // 顶一个键，可以理解为操作SharedPreference的key
  // 数值1是一个唯一标识，并不是默认值
  int32 click_btn_times = 1;
}
```

Step4：定义datastore。`SettingsPb`的引用可能找不到，需要重新编译工程，生成的类文件位置在`build/generated/source/proto/debug/java/com/better/learn/jetpack`。
```kotlin
object Settings2Serializer : Serializer<SettingsPb> {
    override val defaultValue: SettingsPb
        get() = SettingsPb.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): SettingsPb {
        return try {
            SettingsPb.parseFrom(input)
        } catch (exception: Exception) {
            exception.printStackTrace()
            defaultValue
        }
    }
    override suspend fun writeTo(t: SettingsPb, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.dataStoreProtobuf: DataStore<SettingsPb> by dataStore(
    fileName = "settingspb.pb",
    serializer = Settings2Serializer
)
```

Step5：使用

读取数据：因为类型是已知的所以，不用强制转换了。
```kotlin
val times2 = dataStoreProtobuf.data.map {
                it.clickBtnTimes
            }.first()
```

写入数据：
```kotlin
dataStoreProtobuf.updateData { current ->
    current.toBuilder()
        .setClickBtnTimes(t2)
        .build()
}
```