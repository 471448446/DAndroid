[Activity Result API](https://developer.android.com/training/basics/intents/result?hl=zh-cn#launch)
```
androidx.activity:activity:1.2.4
```

传统的启动`Activity`获取结果，步骤是怎样：
1. 首先得定义启动得`Intent`数据
2. 确定当前得启动环境，是在`Activity`还是在`Fragment`。因为`Activity.startActivityForResult()`和`Fragment.startActivityForResult()`决定了，结果数据是在`Activity`中还是在`Fragment`中处理
3. 启动`Activity.startActivityForResult()`
4. 目标页面操作完成后，在`onActivityResult()`中处理数据

传统的启动得方式，是有一些问题的
* 需要定义变量区分每一个启动的返回结果，要么是`requestCode`，要么通过`resultCode`。
* 得区分是启动环境，是在`Activity`，还是`Fragment`。
* 同时得在`onActivityResult()`处来处理启动得结果，又是一堆switch。得通过`requestCode`判断是谁启动得。得通过`resultCode`判断结果是否成功。如果有返回值再通过`intent`来取数据。
* 对于一些特定，已经固定的启动Activity方式，需要每次编写。比如启动相机拍照，在Intent中要传入，拍照图片的文件地址。格式是固定得，但是每次拍照，都要这么写，可能比较繁琐。当然这种定义一个全局的方法也行。不过如果换一个人实现这个功能，可能写法就又不一样了。

而新的`Activity Result API`方式，它只需提供一个`ActivityResultContract`来输入数据，以提供怎样启动`Activity`。并提供了回调`ActivityResultCallback`来接收最终想要数据`。这样的好处是
* 可以自定义启动的参数数据类型，通过一个泛型参数`I`来确定
* 可以自定义想要的结果数据类型，通过一个泛型参数`O`来确定
* 隐藏了`requestCode`逻辑，调用者并不需要关注这个了
* 隐藏了处理结果逻辑，调用方只需要通过回调，自行处理返回的结果数据就行。
* 结果数据处理可以不在Activity中处理，更灵活。
* 简化了调用逻辑
    * 对于启动，不需要区分上下文是`Activity`还是`Fragment`，只需要调用`ActivityResultLauncher.launch()`传入输入数据就完事了。
    * 对于结果数据，直接在回调中取就完事了

下面从实现的效果，来反向理解它的设计
**目标：简化为注册一个回调来接收结果**

首先要实现这个目标，得定义一个回调用来接收数据，对应的就是`ActivityResultCallback<O>`（结果回调），类型`O`就是最后期望得到的数据类型。
```
public interface ActivityResultCallback<O> {
    void onActivityResult(@SuppressLint("UnknownNullness") O result);
}
```
然后得想办法支持自定义得输入类型。
启动一个Activity并获取它的结果过程，可看作是向一个Activity输入特定的数据，然后获取特定结果。当然启动Activity输入数据还是通过Intent来完成，回传数据也是通过`onActivityResult()`来完成，基本情况是不变的。
将这个过程抽象到具体的方法中。在`ActivityResultContract<I, O>`（启动约定），`I`是输入的数据类型，`O`是返回的结果类型。
1. `ActivityResultContract.createIntent(Context context,I input)`是准备启动的`Intent`，用以表示输入的数据`I`。
2. `ActivityResultContract.parseResult(int resultCode,Intent intent)`是用来解析结果数据，用以表示希望获取的数据。根据结果数据转换为想要的结果数据类型`O`。

到这里有了“启动约定”和“结果回调”，其实可以完成目标了，调步骤如下
1. 申明一个`ActivityResultContract`的实现者，
    * 实现`createIntent()`，获取输入的`I`数据，怎样来组装`Intent`
    * 实现`parseResult()`，将结果数据转化为希望的结果数据`O`
2. 调用`ActivityResultContract.createIntent()`，获取`Intent`。
3. 再通过`Activity.startActivityForResult()`启动目标Activity。
4. 最后获取结果数据。因为获取启动Activity结果的底层功能逻辑是没有变化的，还是在`Activity.onActivityResult()`处处理。所以调用`parseResult()`处理数据，最后回调接口得到最终的数据。

流程就是这样，只不过每次都要这样写几步类似的代码，显得有点重复，需要在简化一下模板代码。
1. 首先对于输入数据部分。使用`ActivityResultCaller`申明支持注册（可以支持这么一个功能，所以它是一个接口）。提供了`registerForActivityResult()`来注册逻辑。
    * 注册的内容"启动约定"和"结果回调"是委托给`ActivityResultRegistry`来实现。
    * 同时注册方法返回一个`ActivityResultLauncher`，这样注册就和启动产生了关联。
2. 其次对于启动`Activity`部分。使用`ActivityResultLauncher`申明支持启动。调用方通过`launch()`方法来启动Activity，它会使用“启动约定”中的输入类型`I`来构造特定的`Intent`，组装数据外界就不用关心了。如何启动也不用关心了。
3. 最后再提供了一个工具类`ActivityResultRegistry`，来统一管理注册和处理结果逻辑。对于外界的调用而言，在启动Activity的时候，只关心输入什么数据`I`。以及在处理结果时，只关心最终的目标数据`O`就行。工作流程如下
    1. `register()`完成真实的注册逻辑。同时通过方法返回`ActivityResultLauncher`以便，调用来触发启动Activity的过程。
    2. 处理结果数据。调用`ActivityResultContract.parseResult()`得到结果数据`O`。通过`ActivityResultCallback`回调显示数据。

这里预先定义了很多默认的`ActivityResultContract`实现者，来组装`Intent`数据，以及将结果数据转化为目标数据。当然也可以自定义。
* 比如想要整个返回数据。可以使用`ActivityResultContracts.StartActivityForResult`。它定义如下：`public static final class StartActivityForResult extends ActivityResultContract<Intent, ActivityResult> `。输入一个Intent，获取整个返回结果。`ActivityResult`类保存了结果数据`resultCode`和`resultIntent`。
* 启动相机拍照。`public static class TakePicture extends ActivityResultContract<Uri, Boolean>`。传入保存的文件地址，通过`Boolean`值判断成功与否。

前面没有出现`requestCode`相关的逻辑，其实这一步不需要外部来参与了。在`ActivityResultRegistry`内部准备启动数据的时候就完成了。每次启动前先准备一个随机`int`值。
```
    private int registerKey(String key) {
        Integer existing = mKeyToRc.get(key);
        if (existing != null) {
            return existing;
        }
        int rc = generateRandomNumber();
        bindRcKey(rc, key);
        return rc;
    }
```

还有一个是前面一直在注册信息，并没取消注册。如果不取消注册，可能会发生内存泄漏。其实这一步，也可以不让外部来参与。在`ActivityResultRegistry`中使用了Lifecycle相关逻辑。注册的时候，向LifecycleOwner添加了一个观察者。在页面销毁的时候，自动`unregister()`掉了注册的信息。妙。
```
LifecycleEventObserver observer = new LifecycleEventObserver() {
            @Override
            public void onStateChanged(
                    @NonNull LifecycleOwner lifecycleOwner,
                    @NonNull Lifecycle.Event event) {
                if (Lifecycle.Event.ON_START.equals(event)) {
                    mKeyToCallback.put(key, new CallbackAndContract<>(callback, contract));
                    if (mParsedPendingResults.containsKey(key)) {
                        @SuppressWarnings("unchecked")
                        final O parsedPendingResult = (O) mParsedPendingResults.get(key);
                        mParsedPendingResults.remove(key);
                        callback.onActivityResult(parsedPendingResult);
                    }
                    final ActivityResult pendingResult = mPendingResults.getParcelable(key);
                    if (pendingResult != null) {
                        mPendingResults.remove(key);
                        callback.onActivityResult(contract.parseResult(
                                pendingResult.getResultCode(),
                                pendingResult.getData()));
                    }
                } else if (Lifecycle.Event.ON_STOP.equals(event)) {
                    mKeyToCallback.remove(key);
                } else if (Lifecycle.Event.ON_DESTROY.equals(event)) {
                    //取消注册信息
                    unregister(key);
                }
            }
        };
        lifecycleContainer.addObserver(observer);
```
对应的在`ActivityResultRegistry`中还提供了一个没有`Lifecycle`的注册方法，这就需要在页面销毁的时候，手动取消注册。
```
public final <I, O> ActivityResultLauncher<I> register(
            @NonNull final String key,
            @NonNull final ActivityResultContract<I, O> contract,
            @NonNull final ActivityResultCallback<O> callback)
```

> 在启动 activity 以获取结果时，可能会出现您的进程和 activity 因内存不足而被销毁的情况；如果是使用相机等内存密集型操作，几乎可以确定会出现这种情况。

`Result API`也处理了页面中途被销毁的情况。`ActivityResultRegistry`提供了`onSaveInstanceState()`和`onRestoreInstanceState()`，在`Activity`被销毁和复原时会调用响应的方法保存和恢复数据。

`ActivityResultRegistry`支持的功能有注册信息，分发结果数据，保存或恢复注册信息，启动Activity。虽然是一个综合的管家类，但是它通过抽象方法`public abstract <I, O> void onLaunch()`将如何启动`Activity`的逻辑暴露出去了，有实现者来完成（责任尽量少）。