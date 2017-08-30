package better.app.chinawisdom.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import better.app.chinawisdom.R
import better.app.chinawisdom.util.ForWordUtils
import kotlinx.android.synthetic.main.activity_read_activity.*

class ReadActivity : Activity() {
    companion object {
        fun start(activity: Activity, name: String, path: String) {
            val b = Bundle()
            b.putString(Intent.EXTRA_KEY_EVENT, path)
            b.putString(Intent.EXTRA_ALARM_COUNT, name)
            ForWordUtils.to(activity, ReadActivity::class.java, b)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_activity)
        if (null != intent && null != intent.extras) {
            read_readView.showBook(intent.extras.getString(Intent.EXTRA_ALARM_COUNT), intent.extras.getString(Intent.EXTRA_KEY_EVENT))
        }
//        read_readView.showBook("第九卷　　　　　【汉纪一】", "book/baihuazizhitongjian/9_zizhitongjianbaihuaban_资治通鉴第九卷.txt")
    }

    override fun onDestroy() {
        super.onDestroy()
        read_readView.saveBookInfo()
    }
}
