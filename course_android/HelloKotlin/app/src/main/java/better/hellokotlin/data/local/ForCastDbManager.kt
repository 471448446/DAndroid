package better.hellokotlin.data.local

import better.hellokotlin.util.log
import com.antonioleiva.weatherapp.data.server.ForecastResult
import com.antonioleiva.weatherapp.domain.datasource.ForecastDataSource
import com.antonioleiva.weatherapp.extensions.clear
import com.antonioleiva.weatherapp.extensions.parseOpt
import com.antonioleiva.weatherapp.extensions.toVarargArray
import com.google.gson.Gson
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * Created by better on 2017/8/10 11:14.
 */
class ForCastDbManager(val db: ForcastDbHelper = ForcastDbHelper.instance) : ForecastDataSource {

    override fun requestForecastByZipCode(zipCode: Long): ForecastResult? = db.use {
        log("--查询本地——")
        val res = select(TableDayForcast.NAME)
                .whereSimple("${TableDayForcast.CITY_ID} = ?", zipCode.toString())
                .parseOpt { DayForcastDbBean(it) }
        res?.let {
            log("--查询本地——OK")
            Gson().fromJson(res.json_info, ForecastResult::class.java)
        }
    }

    fun saveDayInfo(city: Long, gson: String?) {
        log("保存\n$gson")
        saveDayInfo(gson) {
            db.use {
                with(DayForcastDbBean(city,it)){
                    clear(TableDayForcast.NAME)
                    insert(TableDayForcast.NAME, *map.toVarargArray())
                }

//                insert(TableDayForcast.NAME, Pair(TableDayForcast.CITY_ID, city))
//                insert(TableDayForcast.NAME, TableDayForcast.JSON_INFO to it)
            }
        }
    }

    private fun saveDayInfo(gson: String?, handle: (msg: String) -> Unit) {
        gson?.let { handle(gson) }
    }
}