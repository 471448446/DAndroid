package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme
import java.lang.Math.random

/**
 * doc: https://foso.github.io/Jetpack-Compose-Playground/
 * official tutorial:
 * https://developer.android.com/jetpack/compose/tutorial
 * https://developer.android.com/courses/pathways/compose
 * https://developer.android.com/jetpack/compose/documentation
 * other:
 * https://proandroiddev.com/detect-text-overflow-in-jetpack-compose-56c0b83da5a5
 */
// 这哈Activity就真的只能放UI相关的信息了，不然太多逻辑
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Greeting(
                            Message(
                                "Android",
                                "Java"
                            )
                        )
                        Conversation(msg = mockMessage())
                    }
                }
            }
        }
    }
}

data class Message(
    val title: String,
    val author: String
)

fun mockMessage() = (0 until 30).map {
    val randomWold: (Int) -> String = { i ->
        ('a' + i).toString()
    }
    val random = (random() * 100).toInt()
    val s = (0 until random).joinToString { randomWold((random() * 25).toInt()) }
    Message("水浒传:$it", "张飞: $s")
}

@Composable
fun Conversation(msg: List<Message>) {
    LazyColumn {
        items(msg) { message ->
            Greeting(name = message)
        }
    }
}

@Composable
fun Greeting(name: Message) {
//    Text(text = "Hello $name!")
    // Modifier.width(IntrinsicSize.Max) 无效
    // Modifier.fillMaxWidth()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(id = R.mipmap.wechat),
            "",
            Modifier
                .size(Dp(50f))
                .padding(5.dp)
                .clip(RoundedCornerShape(Dp(50f)))
                .border(1.5.dp, Color(0xFFBB86FC), CircleShape)
        )
        // 水平间距
        Spacer(modifier = Modifier.width(8.dp))
        // remember 是记录状态，不然每次进入这个方法，都会赋默认值
        // rememberSaveable也是记录状态，不过在屏幕旋转时会恢复状态，而remember会被丢弃掉
        var isExpand by rememberSaveable {
            // mutableStateOf 只是值变化的时候，触发重绘
            mutableStateOf(false)
        }
        // 动画
        val extraPadding by animateDpAsState(
            if (isExpand) 48.dp else 0.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable {
                isExpand = !isExpand
            }) {
            Text(
                text = "title: ${name.title}",
                fontSize = 18.sp,
                //androidx.compose.ui.graphics.Color
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(2.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
//                elevation = 1.dp,
                shadowElevation = 1.dp,
                color = if (isExpand) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            ) {
                Text(
                    modifier = Modifier.padding(
                        start = 5.dp,
                        end = 5.dp,
                        bottom = extraPadding.coerceAtLeast(0.dp)
                    ),
                    text = "author: ${name.author}",
//                    style = MaterialTheme.typography.subtitle1,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = if (isExpand) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun DefaultPreview() {
    HelloComposeTheme {
        Greeting(Message("诛仙", "萧鼎"))
    }
}