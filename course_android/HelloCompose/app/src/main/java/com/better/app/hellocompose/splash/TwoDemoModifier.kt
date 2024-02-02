package com.better.app.hellocompose.splash

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
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
import com.better.app.hellocompose.ModifierSortActivity
import com.better.app.hellocompose.UiModifierSize2Activity
import com.better.app.hellocompose.UiModifierSizeDefaultActivity
import com.better.app.hellocompose.UiModifierTouchEventActivity

/**
 * Modifier主要负责以下4个大类的功能：
 *
 * 修改Compose控件的尺寸、布局、行为和样式。
 * 为Compose控件增加额外的信息，如无障碍标签。
 * 处理用户的输入
 * 添加上层交互功能，如让控件变得可点击、可滚动、可拖拽。
 * https://guolin.blog.csdn.net/article/details/132253342
 */
@Composable
fun ModifierTest() {
    Column {
        ModifierSizeTest()
        ModifierInput()
    }
}

/**
 * 演示了改Compose控件的尺寸、布局、行为和样式
 */
@Composable
private fun ModifierSizeTest() {
    val context = LocalContext.current as? Activity
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Button(onClick = {
            context?.startActivity(Intent(context, UiModifierSizeDefaultActivity::class.java))
        }) {
            Text(text = "Modifier默认大小", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, UiModifierSize2Activity::class.java))
        }) {
            Text(text = "Modifier修改大小", color = Color.White, fontSize = 16.sp)
        }
    }
}

/**
 * 监听触摸事件的输入、让控件变得可点击、可滚动、可拖拽
 */
@Composable
private fun ModifierInput() {
    val context = LocalContext.current as? Activity
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Button(onClick = {
            context?.startActivity(Intent(context, UiModifierTouchEventActivity::class.java))
        }) {
            Text(text = "Modifier触摸事件", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, ModifierSortActivity::class.java))
        }) {
            Text(text = "Modifier修饰符顺序", color = Color.White, fontSize = 16.sp)
        }
    }
}

