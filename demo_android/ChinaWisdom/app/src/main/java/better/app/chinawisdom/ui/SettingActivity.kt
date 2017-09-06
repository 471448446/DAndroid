package better.app.chinawisdom.ui

import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import better.app.chinawisdom.R
import better.app.chinawisdom.config.SettingConfig
import better.app.chinawisdom.ui.base.BaseActivity
import kotlinx.android.synthetic.main.tool_bar.*

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initBackToolBar(toolBar).title = getString(R.string.setting)

        fragmentManager.beginTransaction().replace(R.id.setting_content, SettingPreferenceFragment()).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        SettingConfig.recoverFromSetting()
    }

    class SettingPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.xml_setting)
            val pListSlide: ListPreference = findPreference("setting_slide") as ListPreference
            if (null == pListSlide.value) {
                pListSlide.setValueIndex(SettingConfig.bookAnimation.id)
            }
        }
    }

}
