package better.emptyloading

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import better.android.library.empty.LoadingProxy
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mLoadingProxy: LoadingProxy? = null

    private val mHandler = android.os.Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLoadingProxy = LoadingProxy(main_txt)
        mLoadingProxy!!.setOnRetryClickListener({ showContent() })

        main_txt.setOnClickListener {
            loadingError()
        }
        main_list.setOnClickListener {
            startActivity(Intent(this@MainActivity, SimpleListActivity::class.java))
        }
    }

    private fun loadingError() {
        mLoadingProxy!!.displayLoading("请稍后")
        mHandler.postDelayed({ showError() }, 1000)
    }

    private fun showContent() {
        mLoadingProxy!!.displayLoading()

        mHandler.postDelayed({ mLoadingProxy!!.disappear() }, 1000)
    }

    private fun showError() {
        mHandler.postDelayed({ mLoadingProxy!!.displayRetry("加载失败") }, 1000)
    }
}
