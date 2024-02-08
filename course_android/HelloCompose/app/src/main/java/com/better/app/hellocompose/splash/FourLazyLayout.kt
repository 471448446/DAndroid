package com.better.app.hellocompose.splash

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.UiLazyColumActivity
import com.better.app.hellocompose.UiLazyItemActivity
import com.better.app.hellocompose.UiLazyKeyActivity
import com.better.app.hellocompose.UiLazyListStateActivity
import com.better.app.hellocompose.UiLazyNestListActivity
import com.better.app.hellocompose.UiLazyRowActivity

/**
 * https://guolin.blog.csdn.net/article/details/135038175
 * 边距设置
 * LazyState获取列表的状态
 * 嵌套，目前只能方向不一致，方向一致时，里面的需要固定大小（如果是纵向嵌套滚动，那么内层列表的高度必须是固定的。如果是横向嵌套滚动，那么内层列表的宽度必须是固定的）
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FourLazyLayout() {
    val context = LocalContext.current as? Activity
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Button(onClick = {
            context?.startActivity(Intent(context, UiLazyColumActivity::class.java))
        }) {
            Text(text = "LazyColum", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, UiLazyRowActivity::class.java))
        }) {
            Text(text = "LazyRow", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, UiLazyListStateActivity::class.java))
        }) {
            Text(text = "LazyListState", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, UiLazyNestListActivity::class.java))
        }) {
            Text(text = "滑动方向不一致嵌套", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, UiLazyItemActivity::class.java))
        }) {
            Text(text = "Item聚合其它元素", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            context?.startActivity(Intent(context, UiLazyKeyActivity::class.java))
        }) {
            Text(text = "指定列表Key，减少性能消化", color = Color.White, fontSize = 16.sp)
        }
    }
}