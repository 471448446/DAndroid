package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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

class UiLazyListStateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface {
                    UiLazyListStateView()
                }
            }
        }
    }
}

@Preview
@Composable
fun UiLazyListStateView(
    list: List<String> = ('A'..'Z').map {
        "$it"
    }
) {
    val state = rememberLazyListState()
    Box {

        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "使用LazyListState获取列表的状态", fontSize = 20.sp, color = Color.Red)
            LazyColumn(state = state) {
                items(list) { name ->
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
        val visible = state.firstVisibleItemIndex == 0
        if (visible){
            FloatingActionButton(
                onClick = { },
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(Icons.Filled.Add, "Add Button")
            }
        }

    }
}