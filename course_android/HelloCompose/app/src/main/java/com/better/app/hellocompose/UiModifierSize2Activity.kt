package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

class UiModifierSize2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    UiModifierSize2View()
                }
            }
        }
    }
}

@Composable
fun UiModifierSize2View() {
    // 此时没有设置大小,默认会用父级的大小
    Image(
        painter = painterResource(id = R.mipmap.wechat),
        contentDescription = "",
        // 这里使用modifier修改了大小
        modifier = Modifier.wrapContentSize()
    )
    // 使用Modifier修饰位置，边框
    Image(
        painter = painterResource(id = R.mipmap.wechat),
        contentDescription = "",
        // 这里使用modifier修改了大小
        modifier = Modifier
            .wrapContentSize(align = Alignment.BottomStart)
            .border(5.dp, Color.Magenta, CircleShape)
            .clip(CircleShape)
    )
    // 使用Modifier旋转
    Image(
        painter = painterResource(id = R.mipmap.wechat),
        contentDescription = "",
        // 这里使用modifier修改了大小
        modifier = Modifier
            .wrapContentSize(align = Alignment.BottomEnd)
            .border(5.dp, Color.Magenta, CircleShape)
            .clip(CircleShape)
            .rotate(90f)
    )
}