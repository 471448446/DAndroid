package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

class UiLazyItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface {
                    UiLazyItemView()
                }
            }
        }
    }
}

@Preview
@Composable
fun UiLazyItemView(
    list: List<String> = ('A'..'H').map {
        "$it"
    }
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "items可以聚合其他的列表元素，类是RecyclerView中的ConcatAdapter",
            fontSize = 20.sp,
            color = Color.Red
        )
        LazyColumn() {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher),
                        contentDescription = "ic_launcher",
                        modifier = Modifier.wrapContentSize(Alignment.Center)
                    )
                }
            }
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
            item {
                Image(
                    painter = painterResource(id = R.mipmap.wechat),
                    contentDescription = "ic_launcher"
                )
            }
        }
    }
}