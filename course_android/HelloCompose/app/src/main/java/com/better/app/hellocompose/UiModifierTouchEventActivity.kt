package com.better.app.hellocompose

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme
import kotlin.math.roundToInt

/**
 * pointerInput()支持输入事件的监听，
 * draggable()只能水平或者锤子方向滚动
 */
class UiModifierTouchEventActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    UiModifierTouchEventView()
                }
            }
        }
    }
}

@Composable
@Preview
fun UiModifierTouchEventView() {
    val activity = LocalContext.current as? Activity
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "pointerInput监听事件")
        Box(
            modifier = Modifier
                .requiredSize(200.dp)
                .background(color = Color.Blue)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            Log.d(
                                "Modifier", "event: ${
                                    event.changes.joinToString {
                                        "${it.type},${it.position}\n"
                                    }
                                }"
                            )
                        }
                    }
                }
        )
        Text(text = "pointerInput监听点击、双击、长按事件")
        Box(
            modifier = Modifier
                .requiredSize(200.dp)
                .background(color = Color.Red)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            Toast
                                .makeText(activity, "双击$it", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onLongPress = {
                            Toast
                                .makeText(activity, "长按$it", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onPress = {
//                            Toast
//                                .makeText(activity, "按$it", Toast.LENGTH_SHORT)
//                                .show()
                        }
                    ) {
                        Toast
                            .makeText(activity, "点击", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            Log.d("Modifier", "onDragStart")
                        },
                        onDragEnd = {
                            Log.d("Modifier", "onDragEnd")
                        },
                        onDragCancel = {
                            Log.d("Modifier", "onDragCancel")
                        }
                    ) { change, dragAmount ->
                        Log.d("Modifier", "Dragging!!!")
                    }
                }
        )
        Text(text = "使用clickable()开启点击")
        Box(
            modifier = Modifier
                .requiredSize(200.dp)
                .background(color = Color.Green)
                .clickable {
                    Toast
                        .makeText(activity, "点击", Toast.LENGTH_SHORT)
                        .show()
                }
        )
        Text(text = "滚动")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // 设置可以水平滚动
                .horizontalScroll(rememberScrollState())
        ) {
            repeat(20) {
                Text(text = "index: $it")
            }
        }
        val dragHorizontal = remember {
            mutableStateOf(0f)
        }
        Text(text = "水平滚动  ${dragHorizontal.value}")
        Box(
            modifier = Modifier
                // 真丝滑
                .offset { IntOffset(dragHorizontal.value.roundToInt(), 0) }
                .requiredSize(200.dp)
                .background(color = Color.Blue)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { offset ->
                        dragHorizontal.value += offset
                    },
                )
        )
        val dragHorizontalX = remember {
            mutableStateOf(0f)
        }
        val dragHorizontalY = remember {
            mutableStateOf(0f)
        }
        Text(text = "滚动 任意方向(${dragHorizontalX.value.roundToInt()},${dragHorizontalY.value.roundToInt()})")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(300.dp)
                .background(color = Color.Gray)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset {
                        IntOffset(
                            dragHorizontalX.value.roundToInt(),
                            dragHorizontalY.value.roundToInt()
                        )
                    }
                    .requiredSize(50.dp)
                    .background(color = Color.Blue)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            // 消耗事件
                            change.consumePositionChange()
                            dragHorizontalX.value += dragAmount.x
                            dragHorizontalY.value += dragAmount.y
                        }
                    }
            )
        }
    }
}