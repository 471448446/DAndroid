package better.app.chinawisdom.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import better.app.chinawisdom.R
import better.app.chinawisdom.SettingConfig
import better.app.chinawisdom.support.extenions.gone
import better.app.chinawisdom.support.extenions.isVisible
import better.app.chinawisdom.support.extenions.visible
import better.app.chinawisdom.support.utils.ForWordUtils
import better.app.chinawisdom.support.utils.SystemBarHelper
import better.app.chinawisdom.ui.base.BaseActivity
import better.app.chinawisdom.widget.ReadView
import kotlinx.android.synthetic.main.activity_read_activity.*
import kotlinx.android.synthetic.main.tool_bar.*

class ReadActivity : BaseActivity() {
    companion object {
        fun start(activity: Activity, name: String, path: String) {
            val b = Bundle()
            b.putString(Intent.EXTRA_KEY_EVENT, path)
            b.putString(Intent.EXTRA_ALARM_COUNT, name)
            ForWordUtils.to(activity, ReadActivity::class.java, b)
        }
    }

    private val mHandler = android.os.Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_activity)
        initBackToolBar(toolBar)
        fadeActionLay()
        read_readView.centerListener = object : ReadView.OnCenterClickListener {
            override fun onCenterClick() {
                if (toolBar.isVisible()) {
                    goneToolBar()
                } else {
                    visibleToolBar()
                }
            }
        }
        if (null != intent && null != intent.extras) {
            val name = intent.extras.getString(Intent.EXTRA_ALARM_COUNT)
            supportActionBar?.title = name
            read_readView.openBook(name, intent.extras.getString(Intent.EXTRA_KEY_EVENT))
        }
        toolBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_setting) {
                ForWordUtils.to(this, SettingActivity::class.java)
            }
            true
        }

//        read_readView.openBook("第九卷　　　　　【汉纪一】", "book/baihuazizhitongjian/9_zizhitongjianbaihuaban_资治通鉴第九卷.txt")
    }

    override fun onStart() {
        super.onStart()
        read_readView.setSlideAnimation(SettingConfig.configBookAnimation)
        read_readView.setTextType(SettingConfig.configTextFace)
        read_readView.setBgColor(SettingConfig.configBgType.getColorRes())
        read_readView.setTextSize(SettingConfig.configTextSize.testSize)
        read_readView.validateFeature()
    }

    override fun onDestroy() {
        super.onDestroy()
        read_readView.saveBookInfo()
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_setting, menu)
        return true
    }

    private fun fadeActionLay() {
        mHandler.postDelayed({
            goneToolBar()
        }, 1500)
    }

    private fun goneToolBar() {
        toolBar.gone()
        SystemBarHelper.setBarColor(mActivity, SettingConfig.configBgType.getColorRes())
    }

    private fun visibleToolBar() {
        toolBar.visible()
        SystemBarHelper.setBarColor(mActivity, ContextCompat.getColor(mActivity, R.color.colorPrimaryDark))
    }

}
