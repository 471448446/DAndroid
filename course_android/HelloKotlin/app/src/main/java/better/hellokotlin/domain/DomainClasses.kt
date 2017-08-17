package better.hellokotlin.domain

/**
 * Created by better on 2017/7/29 14:32.
 */
data class ForecastList(val city: String, val country: String,
                        val dailyForecast:List<Forecast>)

data class Forecast(val date: String, val description: String, val high: Int,
                    val low: Int)