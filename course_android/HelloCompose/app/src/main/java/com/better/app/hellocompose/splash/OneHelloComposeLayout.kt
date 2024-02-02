package com.better.app.hellocompose.splash

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ConstraintLayoutActivity
import com.better.app.hellocompose.GeneralWidgetActivity
import com.better.app.hellocompose.UiBoxActivity
import com.better.app.hellocompose.UiColumActivity
import com.better.app.hellocompose.UiRowActivity


/**
 * 体验Compose布局
 * https://guolin.blog.csdn.net/article/details/131622694
 */
@Composable
fun HelloComposeLayout() {
    val context = LocalContext.current as? Activity
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // 滚动
            .horizontalScroll(rememberScrollState()),
    ) {
        Button(onClick = {
            context?.startActivity(Intent(context, GeneralWidgetActivity::class.java))
        }) {
            Text(text = "组件演示", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, UiColumActivity::class.java))
        }) {
            Text(text = "Colum", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, UiRowActivity::class.java))
        }) {
            Text(text = "Row", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, UiBoxActivity::class.java))
        }) {
            Text(text = "Box", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, ConstraintLayoutActivity::class.java))
        }, modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(text = "ConstraintLayout", color = Color.White, fontSize = 16.sp)
        }
    }
}
