package better.learn.view.custom.canvas.compose.path

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import better.learn.view.R

// 绘制一个中间镂空的效果
class HollowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hollow)
    }
}