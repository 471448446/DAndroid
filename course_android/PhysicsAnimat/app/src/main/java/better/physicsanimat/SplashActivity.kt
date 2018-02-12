package better.physicsanimat

import android.animation.Animator
import android.os.Bundle
import android.support.animation.DynamicAnimation
import android.support.animation.FlingAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.animation.DecelerateInterpolator
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        android.os.Handler().postDelayed({ startAnimation() }, 2000)
    }

    private fun startAnimation() {
        val force = SpringForce(70f)
        //设置弹性阻尼，dampingRatio越大，摆动次数越少，当到1的时候完全不摆动
        force.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        //生硬度，stiffness值越小，弹簧越容易摆动，摆动的时间越长
        force.stiffness = SpringForce.STIFFNESS_VERY_LOW

        splashLay.pivotX = 0f
        splashLay.pivotY = 0f
        val animation = SpringAnimation(splashLay, DynamicAnimation.ROTATION)
        animation.spring = force
        animation.setStartValue(10f)
        animation.addEndListener { animation, canceled, value, velocity ->
            Log.d("Better", "finish")
            Log.d("Better", "_" + value)

//            fakeSlide()
            slideDismiss()

        }
        animation.start()
    }

    private fun slideDismiss() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels.toFloat()
        val width = displayMetrics.widthPixels
        splashLay.animate()
                .setStartDelay(1)
                .translationXBy(width.toFloat() / 2)
                .translationYBy(height)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {

                    }

                    override fun onAnimationEnd(p0: Animator?) {
//                        val intent = Intent(applicationContext, MainActivity::class.java)
//                        finish()
//                        startActivity(intent)
//                        overridePendingTransition(0, 0)
                    }

                    override fun onAnimationCancel(p0: Animator?) {

                    }

                    override fun onAnimationStart(p0: Animator?) {

                    }

                })
                .setInterpolator(DecelerateInterpolator(1f))
                .start()
    }

    private fun fakeSlide() {
        val fl = FlingAnimation(splashLay, DynamicAnimation.TRANSLATION_Y)
        //FlingAnimation实例被配置为使用0像素/秒作为其初始速度。 这意味着一旦动画开始就会停止。 为了模拟一个真实的滑动，你必须永远记得调用setStartVelocity()方法并传递一个很大的值
        fl.friction = 0.2f
        //如果没有摩擦力，动画将不会停止。因此，你还必须调用 setFriction() 方法并传递给它一个小的数值
        fl.setStartVelocity(300f)
        fl.start()
    }
}
