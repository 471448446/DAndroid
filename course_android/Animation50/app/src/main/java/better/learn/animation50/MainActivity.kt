package better.learn.animation50

import android.annotation.TargetApi
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Pair
import better.learn.animation50.R.id.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txt_explode.setOnClickListener {
            startActivity(Intent(this@MainActivity, ExplodeActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        txt_slide.setOnClickListener {
            startActivity(Intent(this@MainActivity, SlideActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        txt_fade.setOnClickListener {
            startActivity(Intent(this@MainActivity, FadeActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        txt_other.setOnClickListener {
            startActivity(Intent(this@MainActivity, OtherActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
        //        android:transitionName="share"
        txt_share.setOnClickListener {
            startActivity(Intent(this@MainActivity, ShareActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this,
                            Pair(txt_share, "share")).toBundle())
        }
    }
}
