package better.app.chinawisdom.data.db

import android.database.sqlite.SQLiteDatabase
import better.app.chinawisdom.App
import org.jetbrains.anko.db.*

/**
 * Created by better on 2017/8/17 13:09.
 */
class DbHelper : ManagedSQLiteOpenHelper(App.instance, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_VERSION = 2
        val DB_NAME = "book.db"
        val instance by lazy { DbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        if (null == db) return
        db.createTable(DbTableBook.NAME, true,
                DbTableBook.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                DbTableBook.BOOK to TEXT,
                DbTableBook.CHAPTER to TEXT,
                DbTableBook.CHAPTER_PATH to TEXT)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        if (null == db) return
        db.delete(DbTableBook.NAME)
        onCreate(db)
    }
}