package com.better.app.hellocompose

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

class GeneralWidgetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                GeneralWidgetView()
            }
        }
    }
}

@Composable
@Preview
fun GeneralWidgetView() {
    val activity = LocalContext.current as? Activity
    Column(
        modifier = Modifier
            .background(color = Color.White)
            // 宽高都最大
            .fillMaxSize()
            // 内部控件挨个间距
            .padding(5.dp),
        // 水平剧中
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "文本", fontSize = 30.sp, color = Color.Red)
        Button(
            onClick = { Toast.makeText(activity, "点击", Toast.LENGTH_SHORT).show() },
            // 修改对其方式
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(text = "按钮")
        }
        val inputState = remember {
            mutableStateOf("")
        }
        TextField(
            value = inputState.value,
            onValueChange = {
                inputState.value = it
            },
            modifier = Modifier.width(IntrinsicSize.Max),
            placeholder = {
                Text(text = "提示文字")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Gray
            )
        )
        Image(painter = painterResource(id = R.mipmap.wechat), contentDescription = "")
        CircularProgressIndicator()
        LinearProgressIndicator(progress = 0.5f)
    }
}