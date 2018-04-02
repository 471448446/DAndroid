package better.learn.eventbus

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import better.learn.eventbus.event.MessageEvent
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)

        main_normal.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
        EventBus.getDefault().postSticky(String())
        main_sticky.setOnClickListener {
            startActivity(Intent(this, StickyActivity::class.java))
        }
    }

//    override fun onStop() {
//        super.onStop()
//        EventBus.getDefault().unregister(this)
//    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    fun onMessageEvent(event: MessageEvent) {
        Toast.makeText(this@MainActivity, "收到普通调用", Toast.LENGTH_SHORT).show()
    }
}
