package com.better.app.hellocompose.splash

import android.content.Intent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ComposeUnderHookScopeActivity


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ComposeUnderHook() {
    val context = LocalContext.current
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Button(onClick = {
            context.startActivity(Intent(context, ComposeUnderHookScopeActivity::class.java))
        }) {
            Text(text = "重组范围", color = Color.White, fontSize = 16.sp)
        }
    }
}