package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.better.app.hellocompose.ui.theme.HelloComposeTheme
import com.better.app.hellocompose.ui.theme.Purple200
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * https://developer.android.com/jetpack/compose/state?hl=zh-cn
 * State 的变化会重新绘制，State所在方法对应的UI
 */
class WatchStateActivity : ComponentActivity() {
    private val model by viewModels<com.better.app.hellocompose.ViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    WatchStateActivityContent(model.liveData)
                    WatchList(liveData = model.listLiveData)
                }
            }
        }
    }
}

class ViewModel : ViewModel() {
    private val liveData_ = MutableLiveData<String>()
    val liveData: LiveData<String> = liveData_
    val listLiveData = MutableLiveData<List<String>>()

    init {
        viewModelScope.launch {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
            while (isActive) {
                liveData_.postValue(simpleDateFormat.format(System.currentTimeMillis()))
                delay(1000)
            }
        }
        viewModelScope.launch {
            val listString = listOf<String>().toMutableList()
            repeat(10) {
                listString.add(it.toString())
                listLiveData.postValue(listString)
                delay(1000)
            }
        }
    }
}

@Composable
fun WatchList(liveData: LiveData<List<String>>) {
    /**
     * 看起来不能这样监听列表
     * 只展示了第一列数据
     */
    val list by liveData.observeAsState(listOf())
    LazyColumn {
        items(list) { bean ->
            WatchItemView(name = bean)
        }
    }
}

@Composable
fun WatchItemView(name: String) {
    Text(text = name)
    Divider(color = Color.Green, thickness = 1.dp)
}

@Composable
fun WatchStateActivityContent(liveData: LiveData<String>) {
    val state by liveData.observeAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "$state", fontSize = 30.sp, color = Purple200)
    }
}

@Composable
@Preview
fun WatchStateActivityContentPreView() {
    HelloComposeTheme {
        Surface {
            WatchStateActivityContent(MutableLiveData<String>().also { it.value = "Hello" })
            WatchItemView(name = "Hello")
        }
    }
}