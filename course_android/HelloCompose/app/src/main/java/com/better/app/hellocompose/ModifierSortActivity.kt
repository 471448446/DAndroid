package com.better.app.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

/**
 * modifier的顺序会影响结果
 * Modifier的链式调用模式对于串接的顺序是有要求的，不同的串接顺序可能实现的是不同的效果
 */
class ModifierSortActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface {

                }
            }
        }
    }
}

/**
 * 如果想要给图片增加一个背景色，background()函数一定要在border()和clip()函数之前调用才行，
 * 这样Compose的执行逻辑就是，先为图片指定了一个矩形灰色背景，然后再将图片裁剪成圆形，就出现了上图所示的效果。
 * 如果把background()函数放在border()和clip()函数之后调用，Compose的执行逻辑就会变成，
 * 先把图片裁剪成圆形，然后再在圆形的基础上添加背景色，那么这个背景色也是圆形的，从而就完全看不到了。
 *
 * 在Compose当中根本就没有layout_marging这个属性所对应的概念，因为它是不需要的。结组padding的顺序就能实现margin
 */
@Composable
@Preview
fun ModifierSortView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        Text(
            text = "api的调用顺序会影响Modifier的结果",
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(text = "同样都设置了图片的背景颜色，但是第二个图片展现不出来背景颜色")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.mipmap.wechat),
                contentDescription = "",
                modifier = Modifier
                    // 先指定大小跟随内容
                    .wrapContentSize()
                    .background(Color.Green)
                    .border(5.dp, Color.Red, CircleShape)
                    .clip(CircleShape)
            )
            Image(
                painter = painterResource(id = R.mipmap.wechat),
                contentDescription = "",
                modifier = Modifier
                    .wrapContentSize()
                    .border(5.dp, Color.Red, CircleShape)
                    .clip(CircleShape)
                    .background(Color.Green)
            )
        }

        Text(text = "使用padding增减边距")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.wechat),
                    contentDescription = "",
                    modifier = Modifier
                        // 先指定大小跟随内容
                        .wrapContentSize()
                        .background(Color.Green)
                        .border(5.dp, Color.Red, CircleShape)
                        .padding(10.dp)
                        .clip(CircleShape)
                )
                Text(text = "边距在border里面")
            }
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                // 边距在border外面
                Image(
                    painter = painterResource(id = R.mipmap.wechat),
                    contentDescription = "",
                    modifier = Modifier
                        // 先指定大小跟随内容
                        .wrapContentSize()
                        .background(Color.Green)
                        .padding(10.dp)
                        .border(5.dp, Color.Red, CircleShape)
                        .clip(CircleShape)
                )
                Text(text = "边距在border外面")
            }
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.wechat),
                    contentDescription = "",
                    modifier = Modifier
                        // 先指定大小跟随内容
                        .wrapContentSize()
                        .background(Color.Green)
                        .border(5.dp, Color.Red, CircleShape)
                        .clip(CircleShape)
                        .padding(10.dp)
                )
                Text(text = "边距在裁剪上面")
            }
        }

    }
}