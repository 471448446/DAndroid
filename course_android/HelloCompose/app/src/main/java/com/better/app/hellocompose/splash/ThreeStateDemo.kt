package com.better.app.hellocompose.splash

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.StateHelloCounterActivity

/**
 * https://guolin.blog.csdn.net/article/details/133970363
 * 如何刷新UI，使用状态
 */
@Composable
fun StateDemo() {
    val context = LocalContext.current as? Activity
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Button(onClick = {
            context?.startActivity(Intent(context, StateHelloCounterActivity::class.java))
        }) {
            Text(text = "State计数", color = Color.White, fontSize = 16.sp)
        }
    }
}