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
建议一个文件全局只持有一个datastore对象
线程安全，不阻塞，多进程不安全

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

# Deep into

```kotlin
public interface DataStore<T> {
    public val data: Flow<T>
    public suspend fun updateData(transform: suspend (t: T) -> T): T
}
```
`DataStore`中，只定义了对外提供读写来操作数据的操作。
* 使用`Flow`读数据。flow中保存的就是最新的值。如果有任何异常会通过flow来对外暴露。
* 使用`updateData()`方法，用于更新数据，写入数据。更新数据需要更新保存在内存中flow的值和本地磁盘的数据。方法保证了更新到磁盘的数据和flow中的数据一致，任何一个失败，都不会成功，同时方法抛出异常。

## 创建过程
申明一个`PreferencesDataStore`的方式如下：
```kotlin
val Context.dataStorePreference: DataStore<Preferences> by preferencesDataStore(name = "settings")

public fun preferencesDataStore(
    ...
): ReadOnlyProperty<Context, DataStore<Preferences>> {
    return PreferenceDataStoreSingletonDelegate(name, corruptionHandler, produceMigrations, scope)
}
```
`by` 关键字，是用于kotlin中的属性代理。在`Context`中申明了一个`dataStorePreference`字段，它值是通过`preferencesDataStore`方法返回的，是`PreferenceDataStoreSingletonDelegate`对象。
```kotlin
internal class PreferenceDataStoreSingletonDelegate internal constructor(
    private val name: String,
    private val corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    private val produceMigrations: (Context) -> List<DataMigration<Preferences>>,
    private val scope: CoroutineScope
) : ReadOnlyProperty<Context, DataStore<Preferences>> {
    private val lock = Any()
    @GuardedBy("lock")
    @Volatile
    private var INSTANCE: DataStore<Preferences>? = null
    
    override fun getValue(thisRef: Context, property: KProperty<*>): DataStore<Preferences> {
        return INSTANCE ?: synchronized(lock) {
            if (INSTANCE == null) {
                val applicationContext = thisRef.applicationContext

                INSTANCE = PreferenceDataStoreFactory.create(
                    corruptionHandler = corruptionHandler,
                    migrations = produceMigrations(applicationContext),
                    scope = scope
                ) {
                    applicationContext.preferencesDataStoreFile(name)
                }
            }
            INSTANCE!!
        }
    }
}
```

`PreferenceDataStoreSingletonDelegate`看名称就是想创建一个唯一的对象。通过`synchronized()`确保只创建唯一对象，既单列对象。
* `@GuardedBy("lock")`注解，只是用来表示，`INSTANCE`字段，在使用的时候需要同`synchronized`关联起来。
* `@Volatile`注解，就是java中的volatile的使用方式。
* `synchronized()`方法中调用`DataStoreFactory.create()`创建单列对象`DataStore<Preferences>`。

```kotlin
public object PreferenceDataStoreFactory {
    @JvmOverloads
    public fun create(
        corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
        migrations: List<DataMigration<Preferences>> = listOf(),
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile: () -> File
    ): DataStore<Preferences> {
        val delegate = DataStoreFactory.create(
            serializer = PreferencesSerializer,
            corruptionHandler = corruptionHandler,
            migrations = migrations,
            scope = scope
        ) {
            val file = produceFile()
            check(file.extension == PreferencesSerializer.fileExtension) {
                "File extension for file: $file does not match required extension for" +
                    " Preferences file: ${PreferencesSerializer.fileExtension}"
            }
            file
        }
        return PreferenceDataStore(delegate)
    }
}

internal class PreferenceDataStore(private val delegate: DataStore<Preferences>) :
    DataStore<Preferences> by delegate {
    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences):
        Preferences {
            return delegate.updateData {
                val transformed = transform(it)
                // Freeze the preferences since any future mutations will break DataStore. If a user
                // tunnels the value out of DataStore and mutates it, this could be problematic.
                // This is a safe cast, since MutablePreferences is the only implementation of
                // Preferences.
                (transformed as MutablePreferences).freeze()
                transformed
            }
        }
}
```
进一步可以看到是`DataStoreFactory.create()`创建了`DataStore`，然后创建了一个代理对象来包裹真实的对象，实现了`updateData()`方法。**代理的写法可以关注下**。
因为从后面看到`delegate`对象就是`SingleProcessDataStore`对象，它已经完整的实现了如何更新数据的方法`updateData()`，所以直接调用它来完成是比较合适的。如果通过继承`SingleProcessDataStore`来实现，就必然会重写`updateData()`方法，破坏了完整性。

```kotlin
public object DataStoreFactory {
    @JvmOverloads // Generate constructors for default params for java users.
    public fun <T> create(
        serializer: Serializer<T>,
        corruptionHandler: ReplaceFileCorruptionHandler<T>? = null,
        migrations: List<DataMigration<T>> = listOf(),
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile: () -> File
    ): DataStore<T> =
        SingleProcessDataStore(
            produceFile = produceFile,
            serializer = serializer,
            corruptionHandler = corruptionHandler ?: NoOpCorruptionHandler(),
            initTasksList = listOf(DataMigrationInitializer.getInitializer(migrations)),
            scope = scope
        )
}
```
最后真实的创建的对象是`SingleProcessDataStore`对象，这就整个`DataStore`的真实数据对象。
这里需要注意的是`Serializer`对象，负责具体的数据映射。

* 如何从本地文件转换为真实数据（泛型T所代表的值）
* 如何将真实数据（泛型T所代表的值）保存数据到本地文件。

到此，已经创建`DataStore`的实现类，因为DataStore建议全局持有一个文件的实例DataStore，所以使用了`PreferenceDataStoreSingletonDelegate`来确保单列。

* 这里并没有使用关键字`object`来申明单列，这种方式申明的单列是类加载的时候就被创建。显然不适合这里根据调用情况来确定，在用到的时候才创建的特殊情况。

`DataStore`是和磁盘文件关联的，在Android中获取磁盘文件多与`Context`现关。比如`Context.getFilesDir()`获取私有的文件目录。 所以这通过kotlin中的扩展方法将`DataStore`的实例放在了`Context`的属性中。这样Context类就多了一个扩展字段，逻辑上更清晰。

## 获取值
如何通过`data`字段获取值，或者说`data`流中的值是怎么来的。
从前面可以看到，最后创建的真实DataStore是`SingleProcessDataStore`对象。这个类的代码比较多，看下关心的设计点。

第一个是：一个文件对应一个DataStore，代码上是怎样要求的。创建datastore的时候会传入本地的文件。这里通过将文件放入到静态的变量中记录起来。如果发现已经存在了，会抛出异常。
关键代码如下：

```kotlin
    private val file: File by lazy {
    	// 外界传入的文件
        val file = produceFile()

        file.absolutePath.let {
            synchronized(activeFilesLock) {
                check(!activeFiles.contains(it)) {
                    "There are multiple DataStores active for the same file: $file. You should " +
                        "either maintain your DataStore as a singleton or confirm that there is " +
                        "no two DataStore's active on the same file (by confirming that the scope" +
                        " is cancelled)."
                }
                activeFiles.add(it)
            }
        }

        file
    }
    internal companion object {
        @GuardedBy("activeFilesLock")
        internal val activeFiles = mutableSetOf<String>()

        internal val activeFilesLock = Any()
    }
```

外界获取数据的`data`是怎样发送数据的
```kotlin
    //重写data
    override val data: Flow<T> = flow {
        // 1.取出当前的数据，这里就是想取出泛型T的数据
        val currentDownStreamFlowState = downstreamFlow.value
        // 2. 是否需要读取值
        if (currentDownStreamFlowState !is Data) {
            // 若不是Data类型，说明当前没有数据，立马尝试读取数据 。Data从后面看到，它是用来包裹泛型T的数据对象。   
            actor.offer(Message.Read(currentDownStreamFlowState))
        }
        //3. 发送值
        // 一次发送downstreamFlow中所有值，只不过这里先进行了过滤。以保持发送最新的数据（T）
        // 这里因为是StateFLow，所以只有一个值
        emitAll(
            downstreamFlow.dropWhile {
                if (currentDownStreamFlowState is Data<T> ||
                    currentDownStreamFlowState is Final<T>
                ) {
                    // 不抛弃 DATA或者Final数据。因为这是想要的数据
                    false
                } else {
                    // 否正，可能是异常数据或者为初始化的数据。这种情况，需要抛弃重复的数据。因为前面调用了actor.offer请求数据，所以
                    // 这里或获取最新的数据。
                    it === currentDownStreamFlowState
                }
            }.map {
                // 过滤。如果不是DATA类型的数据，向外界抛出异常
                when (it) {
                    is ReadException<T> -> throw it.readException
                    is Final<T> -> throw it.finalException
                    is Data<T> -> it.value
                    is UnInitialized -> error(
                        "This is a bug in DataStore. Please file a bug at: " +
                            "https://issuetracker.google.com/issues/new?" +
                            "component=907884&template=1466542"
                    )
                }
            }
        )
    }
```
外界是通过`DataStore`的属性`data`流来获取值。`data`中就是目标数据。因为读取数据是一个耗时操作，所以这里用流表示，可以异步获取的值。
这里对`data`字段进行了实现，构建了一个流对象`flow{}`。
`downstreamFlow`是一个`StateFlow`，是一个状态机的实现，里面保存了最新的状态数据。这里保存的转换泛型T数据的结果数据，比如异常数据`ReadException`，成功`Data<T>`。
```kotlin
private val downstreamFlow = MutableStateFlow(UnInitialized as State<T>)

private sealed class State<T>

private object UnInitialized : State<Any>()

private class Data<T>(val value: T, val hashCode: Int) : State<T>() {
    fun checkHashCode() {
        check(value.hashCode() == hashCode) {
            "Data in DataStore was mutated but DataStore is only compatible with Immutable types."
        }
    }
}
private class ReadException<T>(val readException: Throwable) : State<T>()

private class Final<T>(val finalException: Throwable) : State<T>()
```

第1步：这里通过`downstreamFlow.value`来获取值。
第2步: 如果没有目标数据泛型T，那么触发构建数据。这里用到了`actor.offer()`。
第3步：如果`downstreamFlow`没有数据，这里会被挂起。第2步操作触发的读取数据操作，只要向`downstreamFlow`存放的数据，都会解除这里的挂起操作。进而将数据传递到`data`流中。

```kotlin
internal class SimpleActor<T>(
    private val scope: CoroutineScope,

    onComplete: (Throwable?) -> Unit,

    onUndeliveredElement: (T, Throwable?) -> Unit,

    private val consumeMessage: suspend (T) -> Unit
) {
    private val messageQueue = Channel<T>(capacity = UNLIMITED)

    /**
     * Count of the number of remaining messages to process. When the messageQueue is closed,
     * this is no longer used.
     */
    private val remainingMessages = AtomicInteger(0)

    init {
        // If the scope doesn't have a job, it won't be cancelled, so we don't need to register a
        // callback.
        scope.coroutineContext[Job]?.invokeOnCompletion { ex ->
            onComplete(ex)

            messageQueue.close(ex)

            while (true) {
                messageQueue.tryReceive().getOrNull()?.let { msg ->
                    onUndeliveredElement(msg, ex)
                } ?: break
            }
        }
    }
    
    fun offer(msg: T) {
        // should never return false bc the channel capacity is unlimited
        check(
            messageQueue.trySend(msg)
                .onClosed { throw it ?: ClosedSendChannelException("Channel was closed normally") }
                .isSuccess
        )

        // If the number of remaining messages was 0, there is no active consumer, since it quits
        // consuming once remaining messages hits 0. We must kick off a new consumer.
        if (remainingMessages.getAndIncrement() == 0) {
            scope.launch {
                // We shouldn't have started a new consumer unless there are remaining messages...
                check(remainingMessages.get() > 0)

                do {
                    scope.ensureActive()

                    consumeMessage(messageQueue.receive())
                } while (remainingMessages.decrementAndGet() != 0)
            }
        }
    }
}
```
`SimpleActor`中的代码很少，`offer()`向`Channel`发送消息，在新起的协程中来接收消息。新起的这个协程，从`scope`来看是指定的`Dispatchers.IO `，**这样不管发送端是在什么线程中，消耗数据是在后台线程，不阻塞UI线程**。
同时这里用`AtomicInteger`保证了不同线程中的协程的正确性，确保了一次只会起一个协程来消耗数据。
从构造函数中可以看到`consumeMessage()`的定义如下：
```kotlin
{ msg ->
        when (msg) {
            is Message.Read -> {
                handleRead(msg)
            }
            is Message.Update -> {
                handleUpdate(msg)
            }
        }
    }
```
由`actor.offer()`发起的消息都会走到`consumeMessage`这里来
```kotlin
    private suspend fun handleRead(read: Message.Read<T>) {
        when (val currentState = downstreamFlow.value) {
            is Data -> {
                // We already have data so just return...
            }
            is ReadException -> {
                // 如果现在是异常，再次读取
                if (currentState === read.lastState) {
                    readAndInitOrPropagateFailure()
                }

                // Someone else beat us but also failed. The collector has already
                // been signalled so we don't need to do anything.
            }
            UnInitialized -> {
                readAndInitOrPropagateFailure()
            }
            is Final -> error("Can't read in final state.") // won't happen
        }
    }
```
如果当前状态是异常或未初始，都触发读取数据操作。
```kotlin
    private suspend fun readAndInitOrPropagateFailure() {
        try {
            readAndInit()
        } catch (throwable: Throwable) {
            downstreamFlow.value = ReadException(throwable)
        }
    }
```
`readAndInit()`读取数据，如果读取失败，标记当前状态未失败状态。
```kotlin

    private suspend fun readAndInit() {
        // This should only be called if we don't already have cached data.
        check(downstreamFlow.value == UnInitialized || downstreamFlow.value is ReadException)
        ...
        var initData = readDataOrHandleCorruption()
        ...
        downstreamFlow.value = Data(initData, initData.hashCode())
    }

    private suspend fun readDataOrHandleCorruption(): T {
        try {
            return readData()
        } catch (ex: CorruptionException) {
            ...
        }
    }

    private suspend fun readData(): T {
        try {
            FileInputStream(file).use { stream ->
                return serializer.readFrom(stream)
            }
        } catch (ex: FileNotFoundException) {
            if (file.exists()) {
                throw ex
            }
            return serializer.defaultValue
        }
    }
```
从本地文件中读取数据，调用传入`Serializer<T>`来完成将本地数据，转为为对象数据，既泛型T代表的值。

这样只要通过`data`字段获取流的值，比如`data.first()`,都会触发`flow{}`里面的构建操作，构建的时候从`StateFlow`中取值，并传递出去。
如果`StateFlow`没有值，通过向`Channel`发送一条消息，在`IO`线程中从本地文件读取数据，并转换为目标数据。读取成功，会向`data`中存入数据。如读取数据由任何异常，都会通过`data`流对外抛出异常。这样就**保证了读取数据这种耗时操作是在后台线程中运行**。
这里并不能用`data.collect()`，因为`downstreamFlow`是一个`StateFlow`，收集一个`StateFlow`会阻塞当前的协程（写个demo）。

## 更新值
上一步知道了`data`中的值是如何来的。哪如何更新里面的值呢。或者说如何改变里面的值。

```kotlin
    override suspend fun updateData(transform: suspend (t: T) -> T): T {
        val ack = CompletableDeferred<T>()
        val currentDownStreamFlowState = downstreamFlow.value
        // 当前协程的coroutineContext
        val updateMsg =
            Message.Update(transform, ack, currentDownStreamFlowState, coroutineContext)

        actor.offer(updateMsg)

        return ack.await()
    }
```
通过`actor.offer()`发起一个更新操作。

然后使用`CompletableDeferred`挂起当前协程，以获取更新后的数据。`await()`完后，要么获取到数据，要么获取到异常。`CompletableDeferred`在通过`async()`也是用过的。

offer发送的消息最终会来到`consumeMessage()`回调中。

这里需要注意获取了当前协程的Context。`coroutineContext`，转换数据的操作就发生在这个Context中。

```kotlin
    private suspend fun handleUpdate(update: Message.Update<T>) {
        // All branches of this *must* complete ack either successfully or exceptionally.
        // We must *not* throw an exception, just propagate it to the ack.
        update.ack.completeWith(
            runCatching {
                // 获取当前的状态
                when (val currentState = downstreamFlow.value) {
                    is Data -> {
                        // 已经有数据了，直接更新数据
                        transformAndWrite(update.transform, update.callerContext)
                    }
                    is ReadException, is UnInitialized -> {
                        //update.lastState是发起请求时的状态，这里跟发起请求是的状态进行对比
                        if (currentState === update.lastState) {
                            // 异常或者未初始化，需要先重试或读取一次数据。这步若读取失败，会抛出异常。
                            readAndInitOrPropagateAndThrowFailure()

                            // 成功读取了数据，更新数据
                            transformAndWrite(update.transform, update.callerContext)
                        } else {
                            // 这里不会是异常，因为只要要状态变化，就不是未初始化状态了。
                            throw (currentState as ReadException).readException
                        }
                    }

                    is Final -> throw currentState.finalException // won't happen
                }
            }
        )
    }
```

更新数据如下：

```kotlin
    // downstreamFlow.value must be successfully set to data before calling this
private suspend fun transformAndWrite(
    transform: suspend (t: T) -> T,
    callerContext: CoroutineContext
): T {
    // value is not null or an exception because we must have the value set by now so this cast
    // is safe.
    val curDataAndHash = downstreamFlow.value as Data<T>
    curDataAndHash.checkHashCode()

    val curData = curDataAndHash.value
    // 切换到调用放的协程Context
    val newData = withContext(callerContext) { transform(curData) }

    // Check that curData has not changed...
    curDataAndHash.checkHashCode()

    return if (curData == newData) {
        curData
    } else {
        writeData(newData)
        downstreamFlow.value = Data(newData, newData.hashCode())
        newData
    }
}
```
调用参数`transform(curData)`来将当前的数据（泛型T）更新为新的数据，这一步是发生在调用方的上下文，**具体怎么转换由外界实现，这里只关心转换的结果**。
对于新生成的转换数据，
1. 有可能什么都没有修改，既然没有修改，就不用保存
2. 也由可能修改了。尝试保存，更新本地文件中的值`writeData()`，如果成功（没有抛异常），再更新内存中的值`downstreamFlow`。

```kotlin
    internal suspend fun writeData(newData: T) {
        file.createParentDirectories()

        val scratchFile = File(file.absolutePath + SCRATCH_SUFFIX)
        try {
            FileOutputStream(scratchFile).use { stream ->
                serializer.writeTo(newData, UncloseableOutputStream(stream))
                stream.fd.sync()
                // TODO(b/151635324): fsync the directory, otherwise a badly timed crash could
                //  result in reverting to a previous state.
            }

            if (!scratchFile.renameTo(file)) {
                throw IOException(
                    "Unable to rename $scratchFile." +
                        "This likely means that there are multiple instances of DataStore " +
                        "for this file. Ensure that you are only creating a single instance of " +
                        "datastore for this file."
                )
            }
        } catch (ex: IOException) {
            if (scratchFile.exists()) {
                scratchFile.delete() // Swallow failure to delete
            }
            throw ex
        }
    }
```
写入数据也是由`serializer`来完成向文件的写入。具体怎么写，并不关心，反正暴露一个文件流给外界，由外界来完成怎样将数据写入到本地文件，只关心是否写入成功（没有抛异常）。

同时为了避免外界操作完文件流后直接关闭了流，导致`fd.sync()`异常，这里给外界传递的文件流，是不能被关闭的`UncloseableOutputStream`。

所以`update()`方法提供了更新数据的功能，保证了在后台线程更新数据，保证了本地和内存数据一起更新。具体怎样将目标数据（泛型T）转换为（序列化）本地的数据，由外界来决定，这一过程中发生了任何异常，都会通过跑异常的方式暴露给调用方，导致更新失败。
