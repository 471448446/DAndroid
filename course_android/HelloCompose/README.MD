* [Jetpack Compose 基础知识](https://developer.android.com/codelabs/jetpack-compose-basics#0)
* [Jetpack Compose 中的布局](https://developer.android.com/codelabs/jetpack-compose-layouts#0)

官方资源列表：
[为什么采用 Compose](https://developer.android.com/jetpack/compose/why-adopt)

[compose快速入门](https://developer.android.com/jetpack/compose/setup?hl=zh-cn)

官方Demo：
[GitHub:compose-samples](https://github.com/android/compose-samples)

> Compose 与您所有的现有代码兼容：您可以从 View 调用 Compose 代码，也可以从 Compose 调用 View。大多数常用库（如 Navigation、ViewModel 和 Kotlin 协程）都适用于 Compose，因此您可以随时随地开始采用。

# 版本
[Compose 与 Kotlin 的兼容性对应关系](https://developer.android.com/jetpack/androidx/releases/compose-kotlin?hl=zh-cn)

[Jetpack Compose 路线图](https://developer.android.com/jetpack/androidx/compose-roadmap?hl=zh-cn)：这些是团队目前正在考虑和开发的功能

# 基础知识

[jetpack-compose-basics](https://developer.android.com/codelabs/jetpack-compose-basics)

## 从已有的项目中支持Compose：

[将 Android Studio 与 Jetpack Compose 配合使用 ](https://developer.android.com/jetpack/compose/setup)

## 如何使用 Compose 构建界面

1. 使用 `Compose` 时，`Activities` 仍然是 `Android` 应用的入口点。只不过在`setContent`时会采用 `Compose` 的方式来设置。
2. 使用 `@Composable` 来标记函数，这里函数，就是UI相关。可以被渲染到屏幕上。 使用`Surface`来包裹组件，使用`modifier`来控制组件的外观。
3. `Compose` 中的三个基本标准布局元素是 `Column`、`Row` 和 `Box` 可组合项。

### 如何在可组合函数中管理状态

* 使用`State` 和 `MutableState`这两个接口，它们具有特定的值，每当该值发生变化时，它们就会触发界面更新（重组）。
* 使用`remember`来记录每个元素状态信息（上一步添加的状态），避免每次刷新时，每次都是默认值。
* 屏幕旋转保留状态。使用 `rememberSaveable`，而不使用 `remember`。这会保存每个在配置更改（如旋转）和进程终止后保留下来的状态。

### 如何创建高效列表
`Column` 

* 不会处理滚动操作，某些项是看不到的，因为它们在界面范围外。请添加 `verticalScroll` 修饰符，以在 `Column` 内启用滚动。
* 会渲染所有列表项，甚至包括界面上看不到的项，当列表变大时，这会造成性能问题

`LazyColumn` 只会渲染屏幕上可见的内容，从而在渲染大型列表时提升效率。

* 注意：`LazyColumn` 和 `LazyRow` 相当于 Android View 中的 `RecyclerView`。
* 注意：`LazyColumn` 不会像 `RecyclerView` 一样回收其子级。它会在您滚动它时发出新的可组合项，并保持高效运行，因为与实例化 Android Views
  相比，发出可组合项的成本相对较低。

### 如何添加动画

* 全新的方式

### 如何为应用设置样式和主题

* 代码中定义了主题

## Compose注解

* `@Composable`，标记函数。使用了`@Composable`标记了的函数是可组合函数。可组合函数是带有 `@Composable`
  注解的常规函数。这类函数自身可以调用其他 `@Composable` 函数。
* `@Preview`，标记的无参函数，可以用于预览。您可以在同一个文件中包含多个预览，并为它们指定名称。

## Compose 思想

**重组**
当数据发生变化时会重绘界面。

> Compose 应用通过调用可组合函数将数据转换为界面。如果您的数据发生变化，Compose 会使用新数据重新执行这些函数，从而创建更新后的界面，此过程称为重组。Compose 还会查看各个可组合项需要哪些数据，以便只需重组数据发生了变化的组件，而避免重组未受影响的组件。
> 正如 [Compose 编程思想](https://developer.android.com/jetpack/compose/mental-model#recomposition)一文中所述：
> 可组合函数可以按任意顺序频繁执行，因此您不能以代码的执行顺序或该函数的重组次数为判断依据。

Compose 可以实时在手机上预览，666

通过

1. 所有的组件都用`Modifier`字段，用来控制UI的外观，大小、间距、背景、点击等
   * modifier的修饰符，可以多次应用。比如设置margin、
2. `Column`,`Row`使用`Arrangement.Vertical`，`Alignment.Horizontal` 来控制竖直或水平方向的对其方式

**状态**

* 因为状态改变会触发重组，这个时候如果有UI复用的情况，应该尽可能将状态提到上层，只传递数据到下层。（比如复用计数器中的逻辑）
* 重组并不是整个界面都重新绘制，是只有状态变化的地方才会重绘。

### 重组范围

> 那些地方会发生重组，读取了State值，并且按照范围最小化的代码块，最好是非inline函数内

compose会尽量保证 recomposition 范围尽可能小，从而避免了无效开销。

Compose 在编译期分析出会受到某 `state` 变化影响的代码块，并记录其引用，当此 state 变化时，会根据引用找到这些代码块并标记为 `Invalid` 。在下一渲染帧到来之前 Compose 会触发 recomposition，并在重组过程中执行 invalid 代码块。

Invalid 代码块即编译器找出的下次重组范围。能够被标记为 Invalid 的代码必须是**非 inline 且无返回值**的 `@Composalbe function/lambda`，必须遵循 **重组范围最小化** 原则。

[了解 Compose 的重组作用域](https://jetpackcompose.cn/docs/principle/recompositionScope/)

#### 为何是 非 inline 且无返回值（返回 Unit）

对于 inline 函数，由于在编译期会在调用处中展开，因此无法在下次重组时找到合适的调用入口，只能共享调用方的重组范围。

而对于有返回值的函数，由于返回值的变化会影响调用方，因此无法单独重组，而必须连同调用方一同参与重组，因此它不能作为入口被标记为 invalid。

#### 范围最小化原则

只有会受到 state 变化影响的代码块才会参与到重组，不依赖 state 的代码不参与重组。



# 布局
[Compose布局](https://developer.android.com/codelabs/jetpack-compose-layouts)
注：在 View 系统中，创建自定义布局必须扩展 ViewGroup 并实现测量和布局函数。在 Compose 中，您只需使用 Layout 可组合项编写一个函数即可。

# Other

[How to get activity in compose](https://stackoverflow.com/questions/64675386/how-to-get-activity-in-compose)
[How to add Margin in Jetpack Compose?](https://stackoverflow.com/questions/62939473/how-to-add-margin-in-jetpack-compose)
[How can I specify an exact amount of spacing between children in a Jetpack Compose Column?](https://stackoverflow.com/questions/68200057/how-can-i-specify-an-exact-amount-of-spacing-between-children-in-a-jetpack-compo)

[在 Compose 中从 Material 2 迁移至 Material 3 ](https://developer.android.com/jetpack/compose/designsystems/material2-material3?hl=zh-cn)