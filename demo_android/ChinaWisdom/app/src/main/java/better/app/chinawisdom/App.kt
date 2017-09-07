package better.app.chinawisdom

import android.app.Application
import kotlin.properties.Delegates

/**
 * Created by better on 2017/8/17 11:31.
 */
class App : Application() {
    companion object {
        var instance: App by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SettingConfig.init()
    }

}