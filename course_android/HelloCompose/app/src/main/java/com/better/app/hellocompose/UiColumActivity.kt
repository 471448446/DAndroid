package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

class UiColumActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val verticalArrangementList: List<Arrangement.Vertical> = listOf(
            Arrangement.Top,
            Arrangement.Center,
            Arrangement.Bottom,
            Arrangement.SpaceBetween,
            Arrangement.SpaceAround,
            Arrangement.SpaceEvenly,
        )
        val textLabel = listOf("A", "B", "C")
        setContent {
            HelloComposeTheme {
                UiColumView(verticalArrangementList, textLabel)
            }
        }
    }
}

@Composable
@Preview
fun PreviewUiColumView() {
    val verticalArrangementList: List<Arrangement.Vertical> = listOf(
        Arrangement.Top,
        Arrangement.Center,
        Arrangement.Bottom,
        Arrangement.SpaceBetween,
        Arrangement.SpaceAround,
        Arrangement.SpaceEvenly,
    )
    val textLabel = listOf("A", "B", "C")
    UiColumView(verticalArrangementList = verticalArrangementList, textLabel = textLabel)
}

@Composable
fun UiColumView(verticalArrangementList: List<Arrangement.Vertical>, textLabel: List<String>) {
    Column(
        modifier = Modifier
            // 先设置背景白色
            .background(Color.White)
            // 尽可能填充屏幕宽高
            .fillMaxSize()
    ) {
        Text(
            text = "演示了Colum垂直方向上的对齐方式", fontSize = 20.sp, color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier
                // 尽可能填充屏幕宽高
                .fillMaxSize()
        ) {
            verticalArrangementList.forEachIndexed { index, item ->
                Column(
                    modifier = Modifier
                        // 设定每个元素在一行中等分
                        .weight(1f)
                        // 因为宽度是固定的，所以Colum可以设置宽match_parent，
                        // 这样避免了一个Colum占据屏幕宽
                        .fillMaxSize()
                        .padding(2.dp)
                ) {
                    //将 Arrangement#SpaceAround 拆分
                    val nameOneLine = item.toString().split("#")[1]
                    val lastA = nameOneLine.last {
                        it in 'A'..'Z'
                    }
                    // 如果有多个大写字幕，拆分成两行
                    val name = if (nameOneLine.indexOfFirst { it == lastA } != 0) {
                        //将SpaceAround拆分成两行
                        val spit = nameOneLine.indexOfLast { lastA == it }
                        listOf(
                            nameOneLine.substring(0, spit),
                            nameOneLine.substring(spit, nameOneLine.length)
                        ).joinToString("\n")
                    } else {
                        nameOneLine
                    }
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = when (index % 3) {
                                    0 -> Color.Blue
                                    1 -> Color.Black
                                    else -> {
                                        Color.Green
                                    }
                                },
                                shape = RoundedCornerShape(size = 5.dp)
                            ),
                        verticalArrangement = item,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        textLabel.forEach { label ->
                            Text(
                                text = label,
                                fontSize = 18.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                                    .fillMaxWidth()
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(size = 3.dp)
                                    )
                                    // padding是元素周围留白
                                    .padding(horizontal = 5.dp, vertical = 25.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}