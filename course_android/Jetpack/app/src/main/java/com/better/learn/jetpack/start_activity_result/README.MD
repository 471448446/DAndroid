`Activity.startActivityForResult()`的结果只能在Activity中`onActivityResult()`实现，既只能在Activity中处理结果数据。
而新的方式通过`ActivityResultLauncher`来实现，而它只需提供`ActivityResultRegistry`来注册就行，并提供了回调`ActivityResultCallback`来接收数据。也就是说结果数据处理可以不放在Activity中,更灵活。

**目标：简化为注册一个回调来接收结果**

回调是用来接收数据，对应的就是`ActivityResultCallback<O>`（结果回调），类型`O`就是最后期望得到的数据类型。
```
public interface ActivityResultCallback<O> {
    void onActivityResult(@SuppressLint("UnknownNullness") O result);
}
```
启动一个Activity并获取它的结果过程，可看作是向一个Activity输入特定的数据，然后获取特定结果。当然启动Activity输入数据还是通过Intent来完成，回传数据也是通过`onActivityResult()`来完成，基本情况是不变的。
只是将这个过程抽象到具体的方法中。在`ActivityResultContract<I, O>`（启动约定），`I`是输入的数据类型，`O`是返回的结果类型。
1. `ActivityResultContract.createIntent(Context context,I input)`是准备启动的`Intent`，用以表示输入的数据`I`。
2. `ActivityResultContract.parseResult(int resultCode,Intent intent)`是用来解析结果数据，用以表示希望获取的数据。根据结果数据转换为想要的结果数据类型`O`。

第一步输入数据是怎么完成的。有了启动约定和结果回调，是可以实现“一个回调来接收结果”的目标，但是并没有“注册”。但是得有地方触发”输入数据“和”解析数据“才行。
1. 这里使用`ActivityResultCaller`类来申明，输入的数据以及希望接收的数据，完成一个注册逻辑。主要就是通过`registerForActivityResult()`方法提供注册功能。
2. 用`ActivityResultLauncher`来触发启动Activity的过程。主要就是通过`launch()`方法来启动Activity。它会使用启动约定中得输入类型`I`来构造特定得`Intent`。
3. 最后得有一个东西`ActivityResultRegistry`，来统一管理注册和回调逻辑。注册方法返回一个`ActivityResultLauncher`,在合适的数据调用这个`launch()`就行。而注册得内容"启动约定"和"结果回调"是保存在`ActivityResultRegistry`中。所以`ActivityResultCaller`只是一个申明，表示我支持注册功能。

第二步获取结果数据是怎么完成的，因为获取启动Activity结果的底层功能逻辑是没有变化的，还是在`Activity.onActivityResult()`处处理。所以在收到另外一个Activity的结果数据时，调用`ActivityResultContract.parseResult()`解析数据会得到希望的结果数据`O`。再通过回调`ActivityResultCallback.onActivityResult()`完成获取希望的结果数据类型。
比如想要整个返回数据，对应的就是`ActivityResult`类，它保存了结果数据`resultCode`和`resultIntent`。




