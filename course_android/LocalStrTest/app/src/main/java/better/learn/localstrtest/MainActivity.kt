package better.learn.localstrtest

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        updateLocal()
    }

    private fun updateLocal() {
        val config: Configuration = resources.configuration

        var local: Locale

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            local = config.locales[0]
        } else {
            local = config.locale
        }
        log(config.locale.toString())

        if (local.language == "zh") {
            config.locale = Locale.CHINA
        } else {
            config.locale = Locale.ENGLISH
        }

        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun log(str: String) {
        Log.d("Main", str)
    }
}
