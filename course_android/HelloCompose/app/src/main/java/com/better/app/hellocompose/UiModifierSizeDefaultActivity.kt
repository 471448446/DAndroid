package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

class UiModifierSizeDefaultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    UiModifierSizeView()
                }
            }
        }
    }
}

@Composable
@Preview
fun UiModifierSizeView() {
    Text(text = "此时没有设置大小,默认会用父级的大小")
    // 此时没有设置大小,默认会用父级的大小
    Image(painter = painterResource(id = R.mipmap.wechat), contentDescription = "")
}