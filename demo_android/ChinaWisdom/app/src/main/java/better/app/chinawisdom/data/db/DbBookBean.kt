package better.app.chinawisdom.data.db

/**
 * Created by better on 2017/8/17 13:50.
 */
class DbBookBean(val map: Map<String, Any?>) {
//    val id: Int by map
    val book: String by map
    val chapter: String by map
    val chapter_path: String by map

    constructor(book: String, chapter: String, chapterPath: String) : this(
            mapOf(DbTableBook.BOOK to book,
                    DbTableBook.CHAPTER to chapter,
                    DbTableBook.CHAPTER_PATH to chapterPath))

    override fun toString(): String {
        return "DbBookBean(map=$map)"
    }

}