package better.app.chinawisdom.data.db

import better.app.chinawisdom.App
import better.app.chinawisdom.SettingConfig
import better.app.chinawisdom.data.bean.BookInfoBean
import better.app.chinawisdom.support.extenions.clear
import better.app.chinawisdom.support.extenions.parseList
import better.app.chinawisdom.support.extenions.toVarargArray
import better.app.chinawisdom.support.utils.SharePref
import better.app.chinawisdom.support.utils.log
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * Created by better on 2017/8/17 13:23.
 */
class DbManager(private val db: DbHelper = DbHelper.instance) {

    fun initBook(listener: DbInitListener) {
        doAsync {
            db.use {

                val noBook = !SharePref.getBoolean(SharePref.KEY_INIT_BOOK)
                uiThread {
                    if (noBook) {
                        listener.initFail()
                    } else {
                        listener.initOk()
                    }
                }
                //插入数据
                if (noBook) {
                    clear(DbTableBook.NAME)
                    val chapters = copyBookToDb()
                    transaction {
                        chapters.forEach {
                            with(it) {
                                log(toString())
                                insert(better.app.chinawisdom.data.db.DbTableBook.NAME, *map.toVarargArray())
                            }
                        }
                    }
                    uiThread {
                        SharePref.put(SharePref.KEY_INIT_BOOK, true)
                        listener.initOk()
                    }
                }
            }
        }
    }

    fun getBookInfo(key: String, listener: (List<BookInfoBean>) -> Unit) {
        doAsync {
            val books = getBookInfo(key)
            uiThread {
                listener(books)
            }
        }
    }

    private fun getBookInfo(key: String): List<BookInfoBean> = db.use {
        val bean = select(DbTableBook.NAME)
                .whereSimple("${DbTableBook.BOOK} = ?", key)
                .parseList {
                    DbBookBean(it)
                }
        //Warning:(74, 13) Unnecessary safe call on a non-null receiver of type List<DbBookBean>
        bean.map {
            with(it) {
                BookInfoBean(book, chapter, chapter_path)
            }
        }
    }

    private fun copyBookToDb(): List<DbBookBean> {
        val list = arrayListOf<DbBookBean>()
        val chapter: ArrayList<String> = arrayListOf()
        val chapterCount: ArrayList<Int> = arrayListOf()
        val chapterFile: ArrayList<String> = arrayListOf()
        val chapterTitle: ArrayList<String> = arrayListOf()
        //waring use destructuring declaration
        for ((name, fileDirName) in SettingConfig.books) {
            clearAll(chapter, chapterFile, chapterTitle)
            clearAll(chapterCount)
            readLineStr("book/" + fileDirName + "/" + fileDirName + "_chapter.txt", chapter)
            readLineNumStr("book/" + fileDirName + "/" + fileDirName + "_chaptercount.txt", chapterCount)
            readLineStr("book/" + fileDirName + "/" + fileDirName + "_essayfile.txt", chapterFile)
            readLineStr("book/" + fileDirName + "/" + fileDirName + "_essaytitle.txt", chapterTitle)

            var bookChapter: DbBookBean
            var start = 0
            var length: Int
            val multiChapter = chapter.size > 1
            chapter.forEachIndexed { chapterNameIndex, chapterName ->
                if (multiChapter) {
                    bookChapter = DbBookBean(name, chapterName, "")
                    list.add(bookChapter)
                }
                length = chapterCount[chapterNameIndex]
                val end = start + length
                while (start < end) {
                    bookChapter = DbBookBean(name, chapterTitle[start],
                            "book" + File.separator + fileDirName + File.separator + chapterFile[start] + ".txt")
                    list.add(bookChapter)
                    start++
                }
            }
        }
        return list
    }


    private fun readLineNumStr(path: String, list: ArrayList<Int>) {
        val buff = BufferedReader(InputStreamReader(App.instance.resources.assets.open(path)))
        buff.use {
            //waring loop can be replaced with stdlib operation
//                for (numStr in buff.readLine().split(",")) {
//                    list.add(numStr.toInt())
//                }
            //会抛出异常
//                buff.readLine().split(",").forEach {
//                    list.add(it.toInt())
//                }
            buff.forEachLine {
                it.split(",").forEach {
                    list.add(it.toInt())
                }
            }
        }
    }

    private fun readLineStr(path: String, list: ArrayList<String>) {
//        try {
//            val buff = BufferedReader(InputStreamReader(App.instance.resources.assets.open(path)))
//            buff.use {
//                var line: String
//                while (true) {
//                    line = buff.readLine()
//                    if (TextUtils.isEmpty(line)) {
//                        break
//                    }
//                    list.add(line)
//                }
//                list.addAll(buff.readLines())
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        val buff = BufferedReader(InputStreamReader(App.instance.resources.assets.open(path)))
        buff.use {
            list.addAll(buff.readLines())
        }
    }

    private fun <T> clearAll(vararg lists: ArrayList<T>) {
        for (list in lists) {
            list.clear()
        }
    }
}