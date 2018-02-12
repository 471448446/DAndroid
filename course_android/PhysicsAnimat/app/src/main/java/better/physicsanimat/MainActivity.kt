package better.physicsanimat

import android.content.Intent
import android.os.Bundle
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, SplashActivity::class.java))
        main_img.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.main_img -> startAinm()
        }
    }

    private fun startAinm() {

        val force = SpringForce(0f)
        force.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        force.stiffness = SpringForce.STIFFNESS_VERY_LOW

        val anim = SpringAnimation(main_img, SpringAnimation.TRANSLATION_Y)
        anim.spring = force

        anim.setStartValue(100f)
        anim.start()

    }


}
