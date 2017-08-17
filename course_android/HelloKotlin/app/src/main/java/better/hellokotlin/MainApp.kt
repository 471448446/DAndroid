package better.hellokotlin

import android.app.Application
import kotlin.properties.Delegates

/**
 * Created by better on 2017/7/28 15:55.
 */
class MainApp : Application() {
    companion object {
        var instance: MainApp by Delegates.notNull<MainApp>()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

//    fun getInstance(): MainApp = instance
}