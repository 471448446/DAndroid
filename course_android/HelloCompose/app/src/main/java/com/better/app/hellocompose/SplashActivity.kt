package com.better.app.hellocompose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import com.better.app.hellocompose.splash.HelloComposeLayout
import com.better.app.hellocompose.splash.ModifierTest
import com.better.app.hellocompose.splash.StateDemo
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

/**
 * View在Compose中组件：
 * https://www.jetpackcompose.app/What-is-the-equivalent-of-DrawerLayout-in-Jetpack-Compose
 * Compose 中的 Material 组件：
 * https://developer.android.google.cn/jetpack/compose/components?hl=zh-cn
 * > 声明式UI的工作流程有点像是刷新网页一样。即我们去描述一个控件时要附带上它的状态。
 * 然后当有任何状态需要发生改变时，只需要像刷新网页一样，让界面上的元素刷新一遍，那么自然状态就能得到更新了。
 */
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
                },
            )
            HelloComposeLayout()
            ModifierTest()
            StateDemo()
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