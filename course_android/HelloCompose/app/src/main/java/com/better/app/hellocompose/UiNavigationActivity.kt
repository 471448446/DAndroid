package com.better.app.hellocompose

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.better.app.hellocompose.ui.theme.HelloComposeTheme

/**
 * Navigation使用
 * https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation?hl=zh-cn#0
 */
class UiNavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeTheme {
                Surface {
                    UiNavigationView()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UiNavigationView(
    navController: NavHostController = rememberNavController()
) {
    val activity = (LocalContext.current as? Activity)
    // 当前页面
    val backStackEntry by navController.currentBackStackEntryAsState()
    // 获取当前页面
    val currentScreen = NavigationPath.valueOf(
        backStackEntry?.destination?.route ?: NavigationPath.Start.name
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (NavigationPath.Start == currentScreen) {
                            "Navigation 演示"
                        } else {
                            currentScreen.name
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (NavigationPath.Start != currentScreen) {
                            navController.popBackStack()
                        } else {
                            activity!!.finish()
                            Toast.makeText(activity, "finish", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                })
        }
    ) { innerPadding ->
        // 1. 先申明使用Navigation导航
        NavHost(
            navController = navController,
            startDestination = NavigationPath.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 2. 定义每个页面的信息
            composable(NavigationPath.Start.name) {
                NavigationStart {
                    navController.navigate(NavigationPath.Next.name)
                }
            }
            composable(NavigationPath.Next.name) {
                NavigationNext {
                    navController.popBackStack()
                }
            }
        }
    }
}

/**
 * 这里定义所有的页面集合
 */
enum class NavigationPath(name: String) {
    Start("start"), Next("next")
}

/**
 * 开始页面
 * 这里将动作都暴露到外层。比如页面的切换
 */
@Composable
fun NavigationStart(
    switchNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "页面一")
        Button(onClick = {
            switchNext()
        }) {
            Text(text = "下一个页面")
        }
    }
}

@Composable
fun NavigationNext(
    switchNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "页面二")
        Button(onClick = {
            switchNext()
        }) {
            Text(text = "返回上个页面")
        }
    }
}

