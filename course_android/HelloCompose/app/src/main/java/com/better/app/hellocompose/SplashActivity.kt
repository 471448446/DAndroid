package com.better.app.hellocompose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Splash()
            }
        }
//        lifecycleScope.launchWhenStarted {
//            delay(1000)
//            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//            finish()
//        }
    }
}

@Composable
fun Splash() {
    androidx.compose.material.Surface(
        color = Color.White
    ) {
        // 这里因为只在Activity中使用，所以这里强转成了Activity
        val context = LocalContext.current as Activity
        Column(modifier = Modifier.padding(5.dp)) {
            Button(
                onClick = {
                    context.startActivity(Intent(context, MainActivity::class.java))
                    context.finish()
                }) {
                //content 是一个@Composable标记的，所以Button是可以支持其它函数的调用
                Text(text = "列表演示", color = Color.White, fontSize = 16.sp)
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Hello World!!!", color = Color.Green, fontSize = 20.sp)
                Text(text = "Welcome", color = Color.Red)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SplashPreview() {
    HelloComposeTheme {
        Splash()
    }
}