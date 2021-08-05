[datastore](https://developer.android.com/topic/libraries/architecture/datastore)
简单的小型数据集的磁盘存储方法，用以替代`SharedPreferences`。键值对的新的实现方式。
DataStore 提供两种不同的实现：`Preferences DataStore` 和 `Proto DataStore`。
1. `Preferences DataStore` 使用键存储和访问数据。此实现不需要预定义的架构，也不确保类型安全。
2. `Proto DataStore` 将数据作为自定义数据类型的实例进行存储。此实现要求您使用协议缓冲区来定义架构，但可以确保类型安全。

# Preferences DataStore使用方式
先申明DataStore，然后在根据Key操作值。
对于Key的表示，在SharedPreference中是直接用一个String对象，限在需要使用`Preferences.Key`对象来表示。并且提供了便捷方法来构建不同类型的Key。比如：
* `intPreferencesKey()`表示构建值是`Int`类型的Key
* `stringPreferencesKey()`表示构建值是`String`类型的Key