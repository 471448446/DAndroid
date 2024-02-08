package com.better.app.hellocompose

import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

/**
 * m2中的androidx.compose.material.BottomNavigation在
 * m3中叫androidx.compose.material3.NavigationBar
 */
class UiBottomBarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface {
                    UiBottomBarView()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UiBottomBarView() {
    val bottomItems = listOf(
        BottomBarBean(
            "主页", Icons.Outlined.Home, Icons.Filled.Home, Color.Gray
        ),
        BottomBarBean(
            "广场", Icons.Outlined.ShoppingCart, Icons.Filled.ShoppingCart, Color.Blue
        ),
        BottomBarBean(
            "我的", Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle, Color.Cyan
        ),
    )
    var navigationSelectedItem by remember {
        mutableStateOf(0)
    }
    var navigationItem by remember {
        mutableStateOf(bottomItems.first())
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEachIndexed { index, bottomBarBean ->
                    val selected = index == navigationSelectedItem
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navigationSelectedItem = index
                            navigationItem = bottomBarBean
                        },
                        icon = {
                            Icon(
                                imageVector = bottomBarBean.icon(selected),
                                bottomBarBean.name
                            )
                        },
                        label = {
                            Text(
                                text = bottomBarBean.name,
                                color = bottomBarBean.labelColor(selected)
                            )
                        })
                }
            }
        },
        contentWindowInsets = WindowInsets.navigationBars
    ) { contentPadding ->
        Log.d("Better", "contentPadding:$contentPadding")
        // 这个content默认是从全屏开始布局，所以这里指定了WindowInsets.navigationBars
        //contentPadding:PaddingValues(start=0.0.dp, top=0.0.dp, end=0.0.dp, bottom=80.0.dp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(navigationItem.background)
        ) {
            Text(
                text = "Hello $navigationSelectedItem",
                color = Color.Red,
                fontSize = 30.sp,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            )
            Text(
                text = "使用NavigationBar表示tab",
                color = Color.Red,
                fontSize = 20.sp,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(20.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

data class BottomBarBean(
    val name: String,
    val icon0: ImageVector,
    val icon1: ImageVector,
    val background: Color
) {
    fun labelColor(select: Boolean) = if (select) {
        Color.Red
    } else {
        Color.Black
    }

    fun icon(select: Boolean) = if (select) icon1 else icon0
}