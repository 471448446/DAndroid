package better.app.chinawisdom.ui

import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.support.v7.widget.Toolbar
import better.app.chinawisdom.App
import better.app.chinawisdom.R
import better.app.chinawisdom.SettingConfig
import better.app.chinawisdom.support.common.CustomTypefaceSpan
import better.app.chinawisdom.support.extenions.put
import better.app.chinawisdom.support.extenions.setTypeFace
import better.app.chinawisdom.ui.base.BaseActivity
import kotlinx.android.synthetic.main.tool_bar.*
import kotlin.properties.Delegates

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initBackToolBar(toolBar)
        supportActionBar?.title = getString(R.string.setting)

        fragmentManager.beginTransaction().replace(R.id.setting_content, SettingPreferenceFragment(toolBar)).commit()
    }

    class SettingPreferenceFragment(private val bar: Toolbar) : PreferenceFragment(), Preference.OnPreferenceChangeListener {
        private var pListSlide: ListPreference by Delegates.notNull()
        private var pListTextType: ListPreference by Delegates.notNull()

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            if (null == preference || newValue == null) return true
            // true 之后才在保存
            PreferenceManager.getDefaultSharedPreferences(App.instance).put(preference.key, newValue)
            SettingConfig.recoverFromSetting()
            if (preference.key == (getString(R.string.key_setting_textType))) {
                setTypeFace()
            }
            return true
        }

        private fun setTypeFace() {
            bar.setTypeFace(SettingConfig.configTextFace)
            CustomTypefaceSpan.convertPreferenceToUseCustomFont(pListSlide, SettingConfig.configTextFace)
            CustomTypefaceSpan.convertPreferenceToUseCustomFont(pListTextType, SettingConfig.configTextFace)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.xml_setting)
            pListSlide = findPreference(getString(R.string.key_setting_slide)) as ListPreference
            pListTextType = findPreference(getString(R.string.key_setting_textType)) as ListPreference
            if (null == pListSlide.value) {
                pListSlide.setValueIndex(SettingConfig.configBookAnimation.id)
            }
            if (null == pListTextType.value) {
                pListTextType.setValueIndex(SettingConfig.configTextType.id)
            }

            setTypeFace()

            pListSlide.onPreferenceChangeListener = this
            pListTextType.onPreferenceChangeListener = this
        }
    }

}
