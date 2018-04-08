package better.emptyloading

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import better.android.library.empty.LoadingProxy
import kotlinx.android.synthetic.main.activity_simple_list.*

/**
 *
 * if (mTarget == null) {
 * ensureTarget();
 * }
 * if (mTarget == null) {
 * return;
 * }
 * 刷新View并不会再次获取，只要从新获取就好了。
 * 1、用原来的RefreshGroup来替换被修改后的，但是又不能去复制一个View,view 是从xml加载的，不能像对象那样简单的复制。
 * 2、又不能通过反射去修改 RefreshGroup,以后增加一个刷新空间就要去修改一次，违反了开闭原则。
 */
class SimpleListActivity : AppCompatActivity() {

    private var loadingProxy: LoadingProxy? = null

    private val mHandler = android.os.Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_list)

        list_list.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,
                resources.getStringArray(R.array.stringList))


        list_swip.setOnRefreshListener {
            list_swip.isRefreshing = false
        }

        loadingProxy = LoadingProxy(list_list)

        loadingProxy!!.setOnRetryClickListener {
            loadingProxy!!.displayLoading("再次加载")
            mHandler.postDelayed({
                loadingProxy!!.disappear()

                mHandler.postDelayed({
                    for (i in 0..list_swip.childCount) {
                        if (null != list_swip.getChildAt(i)) {
                            Log.d("Better", "__check___" + list_swip.getChildAt(i).javaClass.simpleName + "；" +
                                    list_swip.getChildAt(i).height + ";" + list_swip.getChildAt(i).isShown)
                        }
                    }
                }, 1000)
            }, 1000)
        }

//        loadingProxy!!.displayRetry("服务器异常")

    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
