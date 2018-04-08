package better.learn.animation50

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.transition.Slide
import android.view.Gravity
import android.view.Window

class SlideActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

        setContentView(R.layout.activity_slide)

        val slideIn: Slide = Slide().setDuration(1000) as Slide
        slideIn.slideEdge = Gravity.RIGHT
        val slideOut: Slide = Slide().setDuration(1000) as Slide
        slideOut.slideEdge = Gravity.BOTTOM

        window.enterTransition = slideIn

//        window.exitTransition = slideOut
    }
}
