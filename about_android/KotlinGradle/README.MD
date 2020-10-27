[Migrate to Gradle Kotlin DSL](https://blog.mindorks.com/migrate-to-gradle-kotlin-dsl)
[Migrating build logic from Groovy to Kotlin](https://docs.gradle.org/nightly/userguide/migrating_from_groovy_to_kotlin_dsl.html)，这篇中有步骤

好处是可以有提示，坏处是，在编译的时候可能会变慢一点，比如第一次的时候
利用kotlin的DSL语法来访问变量，省略`this`关键字
1. 将`groovy`编写的`build.gradle`转换为`kotlin`编写的`build.gradle.kts`文件。直接重命名
2. 注意plugin的写法
3. 将`groovy`中`‘’`字符串转换为`kotlin`中的`""`，变量统一使用字段或者函数的方式访问
4. 自定义变量步骤,生成`buildSrc`目录，创建`build.gradle.kts`文件，生成`src/main/java`，新建文件保存信息。
