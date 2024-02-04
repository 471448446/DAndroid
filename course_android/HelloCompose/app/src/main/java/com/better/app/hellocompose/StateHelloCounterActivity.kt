package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 演示点击计数
 * state用户状态发生变化时，触发函数重组
 * remember用于，重组发生时不丢失状态
 * 简化状态：使用kotlin的委托模式
 * 恢复状态：使用 rememberSaveable()
 */
class StateHelloCounterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface {
                    StateHelloCounterView()
                }
            }
        }
    }
}

@Composable
@Preview
fun StateHelloCounterView(viewModel: HelloCountViewModel = viewModel()) {
    Text(text = "下面三个按钮，分别演示了计数器的使用", fontSize = 20.sp, color = Color.Red)
    val counter = remember {
        mutableStateOf(0)
    }

    /**
     * 是借助Kotlin的委托语法对来State的用法进一步精简
     * 注意这里导入了：
     * import androidx.compose.runtime.getValue
     * import androidx.compose.runtime.setValue
     */
    var count2 by remember {
        mutableStateOf(0)
    }

    /**
     * 横竖屏切换后，也能记住状态
     */
    var count3 by rememberSaveable {
        mutableStateOf(0)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
            //Cannot access 'RowScopeInstance': it is internal in 'androidx.compose.foundation.layout
//            .weight(1f, true)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f, true)
            ) {
                Text(text = "使用state保存计算")
                Text(
                    text = "${counter.value}",
                    fontSize = 30.sp,
                    color = Color.Red,
                )
                Button(onClick = { counter.value++ }) {
                    Text(text = "Click")
                }
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f, true)
            ) {
                Text(text = "写法上的变化")
                Text(
                    text = "$count2",
                    fontSize = 30.sp,
                    color = Color.Red,
                )
                Button(onClick = { count2++ }) {
                    Text(text = "Click")
                }
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f, true)
            ) {
                Text(text = "rememberSaveable")
                Text(
                    text = "$count3",
                    fontSize = 30.sp,
                    color = Color.Red,
                )
                Button(onClick = { count3++ }) {
                    Text(text = "Click")
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            val columModifier = Modifier
                .weight(1f)
                .padding(10.dp)
            val horAlignment = Alignment.CenterHorizontally
            StateHoistingCounter(
                columModifier,
                horAlignment,
                "使用state保存计算",
                counter.value
            ) { counter.value++ }
            StateHoistingCounter(
                columModifier,
                horAlignment,
                "写法变化",
                count2
            ) { count2++ }
            StateHoistingCounter(
                columModifier,
                horAlignment,
                "rememberSaveable",
                count3
            ) { count3++ }
        }
        Text(text = "使用viewModel.LiveData保存状态")
        Column {
            StateHoistingCounter(
                modifierColum = Modifier
                    .padding(10.dp),
                horAlignment = Alignment.CenterHorizontally,
                title = "使用", count = viewModel.countLiveData.observeAsState(0).value,
            ) {
                viewModel.increaseCountLiveDat()
            }
        }
        Text(text = "使用viewModel.Flow保存状态")
        Column {
            StateHoistingCounter(
                modifierColum = Modifier
                    .weight(1f)
                    .padding(10.dp),
                horAlignment = Alignment.CenterHorizontally,
                title = "使用", count = viewModel.countFlow.collectAsState().value,
            ) {
                viewModel.increaseCountFlow()
            }
        }
    }
}

/**
 * 复用Compose函数
 * 如果要复用Compose函数，那么函数中尽量减少state，
 * Compose提供了一种编程模式，叫State hoisting，中文译作状态提升。
 * 而实现状态提升最核心的步骤只有两个。
 * 第一就是将原来声明State对象的写法改成用参数传递的写法，就像上面的示例一样。
 * 第二就是将写入State数据的地方改成用回调的方式来通知到上一层。
 *
 * 通常意义上来讲，像这种状态向下传递、事件向上传递的编程模式，我们称之为单向数据流模式（Unidirectional Data Flow）。
 * 而状态提升就是这种单向数据流模式在Compose中的具体应用。
 */
@Composable
fun StateHoistingCounter(
    modifierColum: Modifier,
    horAlignment: Alignment.Horizontal,
    title: String,
    count: Int, click: () -> Unit
) {
    Column(
        modifier = modifierColum,
        horizontalAlignment = horAlignment
    ) {
        Text(text = title)
        Text(
            text = "$count",
            fontSize = 30.sp,
            color = Color.Red,
        )
        Button(onClick = { click.invoke() }) {
            Text(text = "Click")
        }
    }
}

class HelloCountViewModel : ViewModel() {
    /**
     * 使用livedata
     */
    private val _countLiveData = MutableLiveData<Int>()
    val countLiveData: LiveData<Int> = _countLiveData

    /**
     * 使用flow
     */
    private val _flowCounter = MutableStateFlow(0)
    val countFlow: StateFlow<Int> = _flowCounter.asStateFlow()
    fun increaseCountLiveDat() {
        _countLiveData.value = (_countLiveData.value ?: 0).plus(1)
    }

    fun increaseCountFlow() {
        _flowCounter.value += 1
    }
}