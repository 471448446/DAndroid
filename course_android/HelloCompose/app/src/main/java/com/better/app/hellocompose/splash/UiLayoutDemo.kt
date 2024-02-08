package com.better.app.hellocompose.splash

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.UiBottomBarActivity
import com.better.app.hellocompose.UiLazyColumActivity
import com.better.app.hellocompose.UiNavigationActivity

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UiLayoutDemo() {
    val activity = LocalContext.current as? Activity
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .border(1.dp, color = Color.Yellow, shape = CircleShape)
            .clip(CircleShape)
            .background(Color.Gray)
            .padding(2.dp)
    ) {
        Button(onClick = {
            activity?.startActivity(Intent(activity, UiBottomBarActivity::class.java))
        }) {
            Text(text = "BottomBar", color = Color.White, fontSize = 16.sp)
        }
        Button(onClick = {
            activity?.startActivity(Intent(activity, UiNavigationActivity::class.java))
        }) {
            Text(text = "Navigation", color = Color.White, fontSize = 16.sp)
        }
    }
}