package better.hellokotlin.domain

import better.hellokotlin.domain.datasource.ForecastProvider
import com.antonioleiva.weatherapp.data.server.ForecastResult

/**
 * Created by better on 2017/8/11 16:59.
 */
class ForcastCommmand(val cityId: Long, val provider: ForecastProvider = ForecastProvider()) : Command<ForecastResult> {
    override fun execute(): ForecastResult = provider.requestForCast(cityId)
}