package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

class UiBoxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                UiBoxView()
            }
        }
    }
}

@Composable
@Preview
fun UiBoxView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Box布局", fontSize = 20.sp, color = Color.Black)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            listOf(
                Alignment.TopStart, Alignment.TopEnd, Alignment.Center, Alignment.BottomStart,
                Alignment.BottomEnd
            ).forEach {
                Image(
                    painter = painterResource(id = R.mipmap.wechat),
                    contentDescription = "",
                    modifier = Modifier.align(it)
                )
            }
        }
    }
}