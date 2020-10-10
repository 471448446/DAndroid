`ClassVisitor` 当访问字节码时，会调用该类
`MethodVisitor` 当访问方法的时候，会调用该类
`ClassReader` 字节码读取帮助类
`ClassWriter` 字节码写入帮助类
`Transform` 在将`.java` 源文件生成`.class`文件时，会被调用

如何使用TransForm Api
* 继承`TransForm`类，并在配置文件`properties`中申明实现者

如何向class文件中插入字节码
* 遍历工程中的所有`.class`文件，使用`ClassReader`读取字节码，使用`ClassVisitor`操作解码，使用`ClassWriter`写入字节码，覆盖掉原来的字节码。这样就插入了字节码

参考：
[Android 编译插桩之--ASM入门](https://blog.csdn.net/u010976213/article/details/105395590)
