package com.better.app.hellocompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

/**
 * 重组范围
 * https://jetpackcompose.cn/docs/principle/recompositionScope/
 */
class ComposeUnderHookScopeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                ComposeUnderHookView()
            }
        }
    }
}

@Composable
@Preview
fun ComposeUnderHookView() {
    /**
     * 点击发生时，text发生变化，获取值的地方，只有Button里面的Text地方，所以只有Button里面发生了变化
     */
    val TAG = "ComposeUnderHook"

    var text by remember { mutableStateOf("A") }
    Log.d(TAG, "Foo")
    Button(onClick = {
        text = "$text $text"
    }.also { Log.d(TAG, "Button") }) {
        Log.d(TAG, "Button content lambda")
        Text(text).also { Log.d(TAG, "Text") }
    }
}