package com.better.app.hellocompose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
        Column(
            modifier = Modifier.padding(5.dp),
            // 每行的间距
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            TopAppBar(
                title = {
                    Text(text = "Navigation", fontSize = 20.sp)
                },
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.mipmap.wechat),
                        contentDescription = "",
                        Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                    )
                })
            // 这里因为只在Activity中使用，所以这里强转成了Activity。这里使用 as? 是因为预览不过
            val context = LocalContext.current as? Activity
            Row {
                Button(
                    onClick = {
                        context?.startActivity(Intent(context, MainActivity::class.java))
                        context?.finish()
                    },
                    modifier = Modifier
                        .padding(top = 4.dp) // 第一个padding是margin作用
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colors.error, CircleShape)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colors.error, CircleShape)
                        .background(MaterialTheme.colors.background)
                ) {
                    //content 是一个@Composable标记的，所以Button是可以支持其它函数的调用
                    Text(text = "列表演示", color = Color.White, fontSize = 16.sp)
                }
                Button(onClick = {
                    context?.startActivity(Intent(context, ConstraintLayoutActivity::class.java))
                }, modifier = Modifier.padding(8.dp)) {
                    Text(text = "ConstraintLayout", color = Color.White, fontSize = 16.sp)
                }
            }
            Button(onClick = {
                context?.startActivity(Intent(context, WatchStateActivity::class.java))
            }, modifier = Modifier.padding(8.dp)) {
                Text(text = "WatchState", color = Color.White, fontSize = 16.sp)
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