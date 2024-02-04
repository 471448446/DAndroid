package com.better.app.hellocompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

/**
 * https://developer.android.com/codelabs/jetpack-compose-layouts?hl=zh-cn#9
 */
class ConstraintLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConstraintLayoutActivityContent()
        }
    }
}

@Composable
@Preview
fun ConstraintLayoutActivityContent() {
    HelloComposeTheme {
        Surface {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp)
            ) {
                // 先创建需要的约束
                val (userAvatar, userName, userBio, userMore) = createRefs()
                // 然后在应用约束
                val context = LocalContext.current
                Image(
                    painter = painterResource(id = R.mipmap.wechat),
                    contentDescription = "",
                    modifier = Modifier
                        .constrainAs(userAvatar) {
                            // 设置居中
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        }
                        .width(80.dp)
                        .height(80.dp)
                        .clip(CircleShape)
                )
                // 这里没有用 by 关键字，属性委托的方式来申明
                val click = remember {
                    mutableStateOf(0)
                }
                TextButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "${click.value}",
                            Toast.LENGTH_SHORT
                        ).show()
                        click.value++
                    },
                    modifier = Modifier
                        .constrainAs(userName) {
                            top.linkTo(userAvatar.top)
                            start.linkTo(userAvatar.end, margin = 8.dp)
                            bottom.linkTo(userBio.top)
                        }
                        .padding(0.dp)
                        .border(1.dp, Color.Blue),
                    // 文字和Button有个间距，看起来无法移除这个padding，垃圾
                    contentPadding = PaddingValues(),
                ) {
                    Text(text = "Better", fontSize = 16.sp)
                }
                Text(
                    text = "What worries you masters you.What worries you masters you.",
                    modifier = Modifier
                        .constrainAs(userBio) {
                            top.linkTo(userName.bottom)
                            start.linkTo(userName.start)
                            end.linkTo(userMore.start)
                            bottom.linkTo(userAvatar.bottom)
                            // 这行代码就是约束的宽，应该是什么样子，相当于，ConstraintLayout中的"android:layout_width=0dp"
                            width = Dimension.fillToConstraints
                        }
                        .border(1.dp, Color.Blue),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp
                )
                Button(onClick = { }, modifier = Modifier.constrainAs(userMore) {
                    // 设置居中
                    centerVerticallyTo(parent)
                    end.linkTo(parent.end, 8.dp)
                }) {
                    Text(text = "more")
                }
            }
        }
    }
}

