package better.hellokotlin.domain.datasource

import better.hellokotlin.data.local.ForCastDbManager
import better.hellokotlin.http.RequestForecastRequest
import com.antonioleiva.weatherapp.data.server.ForecastResult
import com.antonioleiva.weatherapp.domain.datasource.ForecastDataSource
import com.antonioleiva.weatherapp.extensions.firstResult

/**
 * 数据提供类，本地or网络
 * Created by better on 2017/8/11 16:57.
 */
class ForecastProvider {
    companion object {
        val SOURCE by lazy { listOf(ForCastDbManager(), RequestForecastRequest()) }
    }

    fun requestForCast(id: Long): ForecastResult = reqSource {
        val res = it.requestForecastByZipCode(id)

        res?.let { res }
    }

    private fun <T : Any> reqSource(function: (ForecastDataSource) -> T?): T = SOURCE.firstResult { function(it) }
}