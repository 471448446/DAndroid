package com.better.app.hellocompose

import android.content.pm.ActivityInfo
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

class UiRowActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val verticalArrangementList: List<Arrangement.Horizontal> = listOf(
            Arrangement.Start,
            Arrangement.Center,
            Arrangement.End,
            Arrangement.SpaceBetween,
            Arrangement.SpaceAround,
            Arrangement.SpaceEvenly,
        )
        val textLabel = listOf("A", "B", "C")
        setContent {
            HelloComposeTheme {
                UiRowView(verticalArrangementList, textLabel)
            }
        }
    }
}

@Composable
@Preview
fun PreviewUiRowView() {
    val verticalArrangementList: List<Arrangement.Horizontal> = listOf(
        Arrangement.Start,
        Arrangement.Center,
        Arrangement.End,
        Arrangement.SpaceBetween,
        Arrangement.SpaceAround,
        Arrangement.SpaceEvenly,
    )
    val textLabel = listOf("A", "B", "C")
    UiRowView(verticalArrangementList = verticalArrangementList, textLabel = textLabel)
}

@Composable
fun UiRowView(verticalArrangementList: List<Arrangement.Horizontal>, textLabel: List<String>) {
    Column(
        modifier = Modifier
            // 先设置背景白色
            .background(Color.White)
            // 尽可能填充屏幕宽高
            .fillMaxSize()
    ) {
        Text(
            text = "演示了Row水平方向上的对齐方式", fontSize = 20.sp, color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            verticalArrangementList.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        // 因为宽度是固定的，所以Colum可以设置宽match_parent，
                        // 这样避免了一个Colum占据屏幕宽
                        .fillMaxWidth()
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArrangementTitleText(item)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
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
                        horizontalArrangement = item,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        textLabel.forEach { label ->
                            Text(
                                text = label,
                                fontSize = 18.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 2.dp, vertical = 5.dp)
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(size = 3.dp)
                                    )
                                    // padding是元素周围留白
                                    .padding(horizontal = 10.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }

    }

}

@Composable
private fun ArrangementTitleText(item: Arrangement.Horizontal) {
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
}