package better.learn.eventbus

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import better.learn.eventbus.event.MessageEvent
import kotlinx.android.synthetic.main.activity_second.*
import org.greenrobot.eventbus.EventBus

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        second_event.setOnClickListener {
            EventBus.getDefault().post(MessageEvent())
        }
    }
}
