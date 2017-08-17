package better.hellokotlin.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import better.hellokotlin.R
import better.hellokotlin.domain.ForcastCommmand
import better.hellokotlin.ui.adapter.ForecastListAdapter
import better.hellokotlin.util.log
import com.antonioleiva.weatherapp.data.server.ForecastResult
import com.antonioleiva.weatherapp.extensions.toDateString
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.toast

//import kotlinx.android.synthetic.main.activity_main.*
/**
 * https://wangjiegulu.gitbooks.io/kotlin-for-android-developers-zh
 */
class MainActivity : AppCompatActivity() {
    val code: Long = 94043
    val items = listOf("AA", "BSA", "asdq", "今天奥术大师多")
    //    var adapter: ForecastListAdapter? = null
    val adapter: ForecastListAdapter by lazy {
        ForecastListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle()
        setRecyclerView()

        getInfo()
    }

    private fun getInfo() {

        async(UI) {
            val result = bg { ForcastCommmand(code).execute() }
            upData(result.await())
        }
    }

    private fun upData(result: ForecastResult) {
        //                    adapter.item = result
        //                    adapter.notifyDataSetChanged()
        adapter.setListData(result)
        toast("" + result.list.size)
    }

    private fun setRecyclerView() {
        recyclerViewList.layoutManager = LinearLayoutManager(this)
        //test
//        recyclerViewList.adapter = ForecastListAdapter(items)
        /**
         * 注意ForecastListAdapter(null,{ bean-> }) 参数bean没有()。
         */
        //        adapter = ForecastListAdapter(null)
        adapter.listener = { log(it.dt.toDateString()) }

        recyclerViewList.adapter = adapter
    }

    private fun setTitle() {
        textViewKotlin.text = getString(R.string.kotlin_Fascinating)
        //        textViewKotlin.setText(R.string.kotlin_Fascinating)
//        textViewKotlin.setText("")
        //匿名内部类的方式
        textViewKotlin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                logClickTitle(v, "对象表达式")
            }
        })
        textViewKotlin.setOnClickListener(View.OnClickListener {
            logClickTitle(it, "ASM Constructor")
        })
        //传递函数-Double-Colon-双引号
        textViewKotlin.setOnClickListener(this::onTitleClick)
        //传递函数变量-Function Variable-函数变量
        textViewKotlin.setOnClickListener(clickListener)
        //传递函数-Lambda-
        textViewKotlin.setOnClickListener({ v -> logClickTitle(v, "Lambda") })
        textViewKotlin.setOnClickListener() { v -> logClickTitle(v, "Lambda") }
        textViewKotlin.setOnClickListener { _ -> logClickTitle(null, "Lambda") }
        textViewKotlin.setOnClickListener { logClickTitle(it, "Lambda") }

        textViewKotlin.setOnFocusChangeListener(View.OnFocusChangeListener {
            v, hasFocus ->
            log("聚焦事件")
        })
        // 以下只是说明Lambda中函数部分是代码块，而不是一行代码
//        textViewKotlin.setOnClickListener {
//            var a = 2 + 2
//            log("" + a)
//        }
//        textViewKotlin.setOnClickListener({ onTitleClick(it) })

    }

    val clickListener = { v: View -> logClickTitle(v, "函数变量") }

    fun onTitleClick(v: View) {
        toast("点击了标题" + v.id)
    }

    fun logClickTitle(v: View?, msg: String) {
        val i = "使用${msg}方式，${v?.id ?: ""}"
        log(i)
        toast(i)
    }
}
