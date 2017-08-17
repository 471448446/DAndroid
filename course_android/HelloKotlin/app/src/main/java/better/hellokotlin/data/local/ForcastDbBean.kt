package better.hellokotlin.data.local

/**
 * Created by better on 2017/8/11 09:59.
 */
class DayForcastDbBean(val map: Map<String, Any?>) {
    val city_id: Long by map
    val json_info: String by map

    constructor(id: Long, json: String) :
            this(mapOf(TableDayForcast.CITY_ID to id, TableDayForcast.JSON_INFO to json)) {
//        this.json_info = json_info
//        this.country = country
    }

}

