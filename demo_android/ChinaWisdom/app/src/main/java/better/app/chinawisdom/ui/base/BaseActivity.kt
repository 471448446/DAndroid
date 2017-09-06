package better.app.chinawisdom.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import better.app.chinawisdom.R
import kotlinx.android.synthetic.main.tool_bar.*
import kotlin.properties.Delegates

/**
 * Created by better on 2017/8/17 11:35.
 */
open class BaseActivity : AppCompatActivity() {
    var mActivity: AppCompatActivity by Delegates.notNull()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
    }

    fun initBackToolBar(bar: Toolbar): Toolbar {
        setSupportActionBar(bar)
        toolBar.setNavigationIcon(R.drawable.icon_navgation_back_white)
        toolBar.setNavigationOnClickListener {
            onBackPressed()
        }
        return bar
    }
}