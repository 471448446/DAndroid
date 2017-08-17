package better.hellokotlin.data.local

import android.database.sqlite.SQLiteDatabase
import better.hellokotlin.MainApp
import org.jetbrains.anko.db.*

/**
 * Created by better on 2017/8/10 10:50.
 */
class ForcastDbHelper : ManagedSQLiteOpenHelper(MainApp.instance, NAME, null, VERSION) {
    companion object {
        val NAME = "forCast.db"
        val VERSION = 1
        val instance by lazy { ForcastDbHelper() }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (null == db || newVersion <= oldVersion) {
            return
        }
//        db.delete(TableCity.NAME)
        db.delete(TableDayForcast.NAME)
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase?) {
//        db?.createTable(TableCity.NAME, true,
//                TableCity.ID to INTEGER + PRIMARY_KEY,
//                TableCity.COUNTRY to TEXT)
        db?.createTable(TableDayForcast.NAME, true,
                TableDayForcast.CITY_ID to INTEGER + PRIMARY_KEY,
                TableDayForcast.JSON_INFO to TEXT)
    }

}