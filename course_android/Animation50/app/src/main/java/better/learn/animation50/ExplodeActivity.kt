package better.learn.animation50

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.view.Window

class ExplodeActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

        setContentView(R.layout.activity_explode)

        window.enterTransition = Explode().setDuration(1000)
        window.exitTransition = Explode()

    }
}
