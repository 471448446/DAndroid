通过Demo演示Android中如何使用ASM实现编译插桩

知识点：

1. 如何自定义Gradle插件 
2. Transform API的使用 
3. 使用ASM实现插入字节码到.class文件中

`plugin-hello` 如何搭建一个plugin。结果是，打包时，会答应日志。
`plugin-print` 如何插入字节码。结果时运行APP后，进入`MainActivity`的`onCreate()`方法会打印日志。

[查看字节码工具asm-bytecode-outline](https://plugins.jetbrains.com/plugin/5918-asm-bytecode-outline)
[文档：ASM 6 Developer Guide](https://asm.ow2.io/developer-guide.html)