package better.hellokotlin.http

import better.hellokotlin.data.local.ForCastDbManager
import better.hellokotlin.util.log
import com.antonioleiva.weatherapp.data.server.ForecastResult
import com.antonioleiva.weatherapp.domain.datasource.ForecastDataSource
import com.google.gson.Gson
import java.net.URL

/**
 * Created by better on 2017/7/29 14:06.
 */
class RequestForecastRequest(val dbManager: ForCastDbManager = ForCastDbManager()) : ForecastDataSource {
    override fun requestForecastByZipCode(zipCode: Long): ForecastResult? {
        val url = REQUEST_URL + zipCode
        log("联网请求\n$url")
        val response = URL(url).readText()
        dbManager.saveDayInfo(zipCode, response)
        return Gson().fromJson(response, ForecastResult::class.java)
    }

    /**
     * 伴随对象，为类的所有对象所共享，就像Java中的静态属性或者方法
     */
    companion object {
        private val APP_ID = "15646a06818f61f7b8d7823ca833e1ce"
        private val URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&units=metric&cnt=7"
        private val REQUEST_URL = "${URL}&APPID=${APP_ID}&q="
    }

}