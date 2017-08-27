package better.app.chinawisdom.ui

import android.os.Bundle
import better.app.chinawisdom.R
import better.app.chinawisdom.data.db.DbInitListener
import better.app.chinawisdom.data.db.DbManager
import better.app.chinawisdom.ui.base.BaseActivity
import better.app.chinawisdom.ui.main.MainActivity
import better.app.chinawisdom.util.ForWordUtils
import better.app.chinawisdom.util.ViewUtils
import better.app.chinawisdom.util.log
import kotlinx.android.synthetic.main.activity_splash.*
import kotlin.properties.Delegates

class SplashActivity : BaseActivity() {

    val SPLASH_TIME = 1500
    var startTime: Long by Delegates.notNull()
    val mHandler = android.os.Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startTime = System.currentTimeMillis()
        DbManager().initBook(object : DbInitListener {
            override fun initFail() {
                ViewUtils.visibleView(splash_loading_tip)
            }

            override fun initOk() {
                val time = System.currentTimeMillis() - startTime
                log("___$time")
                if (time >= SPLASH_TIME) {
                    toMain()
                } else {
                    mHandler.postDelayed({
                        toMain()
                    }, SPLASH_TIME - time)
                }
            }
        })
    }

    private fun toMain() {
        ForWordUtils.to(mActivity, MainActivity::class.java)
        finish()
    }
}
