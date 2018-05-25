package better.learn.animation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import better.library.utils.ForWordUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_animLayoutAnimation.setOnClickListener {
            ForWordUtil.to(this@MainActivity, LayoutAnimationActivity::class.java)
        }

        main_animGridLayoutAnimation.setOnClickListener {
            ForWordUtil.to(this@MainActivity, GridLayoutAnimationActivity::class.java)
        }
    }
}
