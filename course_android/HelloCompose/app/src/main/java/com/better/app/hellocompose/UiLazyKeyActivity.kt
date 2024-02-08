package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

/**
 * 如果不指定Key，那么列表是跟进位置进行重组，指定了Key就只根据id进程重组。如果列表元素会变化，需要指定key。
 */
class UiLazyKeyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface {
                    UiLazyKeyView()
                }
            }
        }
    }
}

@Preview
@Composable
fun UiLazyKeyView(
    list: List<String> = ('A'..'Z').map {
        "$it"
    }
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "下面是一个LazyColum，指定了列表元素的key。添加了key参数之后，Compose编译器就有了一个唯一标识符来精准定位到每个Composable函数，而不是像之前那样只能基于Composable函数的位置来定位了。",
            fontSize = 20.sp,
            color = Color.Red
        )
        LazyColumn() {
            items(list, key = { it }) { name ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp)
                        .height(100.dp)
                ) {
                    Text(
                        text = name,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}