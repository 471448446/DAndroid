package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

class UiLazyRowActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface {
                    UiLazyRowView()
                }
            }
        }
    }
}

@Preview
@Composable
fun UiLazyRowView(
    list: List<String> = ('A'..'Z').map {
        "$it"
    }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "下面是一个LazyRow", fontSize = 20.sp, color = Color.Red)
        LazyRow(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
            itemsIndexed(list) { index, name ->
                Card(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(10.dp)
                        .height(100.dp)
                ) {
                    Text(
                        text = "index:$index,$name",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                }
            }
        }
        Text(
            text = "下面是一个LazyRow，使用了contentPadding，滑动的时候左右两边没有占位的部分",
            fontSize = 20.sp,
            color = Color.Red
        )
        LazyRow(contentPadding = PaddingValues(start = 10.dp, end = 10.dp)) {
            itemsIndexed(list) { index, name ->
                Card(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(10.dp)
                        .height(100.dp)
                ) {
                    Text(
                        text = "index:$index,$name",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                }
            }
        }
        Text(
            text = "下面是一个LazyRow，Arrangement.spacedBy(),设置item之间的间距",
            fontSize = 20.sp,
            color = Color.Red
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(list) { index, name ->
                Card(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                ) {
                    Text(
                        text = "index:$index,$name",
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