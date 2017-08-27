package better.app.chinawisdom.widget.book

import android.graphics.Paint
import android.text.TextUtils
import better.app.chinawisdom.App
import better.app.chinawisdom.extensions.notEmpty
import better.app.chinawisdom.util.log
import better.app.chinawisdom.widget.ReadViewHelper
import org.jetbrains.anko.collections.forEachReversed
import java.io.InputStreamReader
import java.lang.ref.WeakReference

/**
 * Created by better on 2017/8/25 14:40.
 */
object BookUtils {
    private val cachedSize = 30000
    private var bookLength = 0
    private var bookPath = ""
    private var booksInfo = arrayListOf<CacheBook>()
    private var positon = 0
    fun openAssetsBook(path: String) {
        bookPath = path
        bookLength = 0
        booksInfo.clear()
        val reader = InputStreamReader(App.instance.resources.assets.open(path))
        reader.use {
            while (true) {
                var buffer = CharArray(cachedSize)
                if (-1 == reader.read(buffer)) {
                    break
                }
                var string = String(buffer)
                string = string.replace("\r\n+\\s*".toRegex(), "\r\n\u3000\u3000")
                string = string.replace("\u0000".toRegex(), "")
                buffer = string.toCharArray()
                bookLength += buffer.size
                var cache = CacheBook(buffer.size, WeakReference(buffer))
                booksInfo.add(cache)
            }
        }
        log("book length :$bookLength")
    }

    fun showOpenPage(helper: ReadViewHelper): BookPage = page(helper, begin = 0)

    fun prePage(helper: ReadViewHelper, current: BookPage): BookPage = page(helper, end = current.begin - 1)

    fun nextPage(helper: ReadViewHelper, current: BookPage): BookPage = page(helper, begin = current.end + 1)

    fun isLastPage(): Boolean = positon == bookLength
    fun isFirstPage(page: BookPage?): Boolean = page?.begin == 0

    private fun page(helper: ReadViewHelper, begin: Int = -1, end: Int = -1): BookPage {
        if (begin >= 0) {
            positon = begin
            val lines: List<String> = getNextPageLines(helper.paintTxt, helper.mVisibleWidth, helper.linesInPage)
            return BookPage(begin, positon, lines)
        } else {
            positon = end
            val lines: List<String> = getPrePageLines(helper.paintTxt, helper.mVisibleWidth, helper.linesInPage)
            return BookPage(positon, end, lines)
        }
    }

    private fun getPrePageLines(paint: Paint, mVisibleWidth: Float, pageLines: Int): List<String> {
        val list = arrayListOf<String>()
        val listPage = arrayListOf<String>()
        var line = ""
        var width = 0f
        var thisParagraph: List<Char>
        var size = 0
        var paragraph = 0
        while (true) {
            thisParagraph = getParagraph(positon)
            positon = positon - thisParagraph.size - 1
            paragraph++
            var thisParagraphLines = arrayListOf<String>()
            thisParagraph.forEach {
                val widthChar = paint.measureText(it + "")
                width += widthChar
                if (width > mVisibleWidth) {
                    width = widthChar
                    thisParagraphLines.add(line)
                    line = it + ""
                } else {
                    line += it
                }
            }
            if (!TextUtils.isEmpty(line)) {
                thisParagraphLines.add(line)
                line = ""
                width = 0f
            }
            list.addAll(0, thisParagraphLines)
            if (list.size >= pageLines) {
                break
            }
        }
        list.forEachReversed {
            if (listPage.size < pageLines) {
                listPage.add(it)
            } else {
                size += it.length
            }
        }
        if (0 < positon) {
            //2 最后一个段落的时候position已经移动到上一个段落的段末了
            positon += size + 2
        } else {
            positon = 0
        }
        return listPage.reversed()
    }

    private fun getParagraph(p: Int): List<Char> {
        val list = arrayListOf<Char>()
        var word: Char
        var end = p
        while (true) {
            if (checkRange(end)) {
                break
            }
            word = current(end)
            if (("" + word) == ("\n") && ("" + current(end - 1)) == "\r") {
                end--
                break
            } else {
                list.add(word)
            }
            end--
        }
        return list.reversed()
    }

    private fun getNextPageLines(paint: Paint, mVisibleWidth: Float, pageLines: Int): List<String> {
        val list = arrayListOf<String>()
        var word: Char
        var line = ""
        var width = 0f
        while (true) {
            if (checkRange()) {
                if (!TextUtils.isEmpty(line)) {
                    list.add(line)
                }
                break
            }
            word = current(positon)
            if (("" + word) == ("\r") && ("" + current(positon + 1)) == "\n") {
                line.notEmpty {
                    list.add(it)
                    line = ""
                    width = 0f
                }
                positon++
            } else {
                val widthChar = paint.measureText(word + "")
                width += widthChar
                if (width > mVisibleWidth) {
                    width = widthChar
                    list.add(line)
                    line = word + ""
                } else {
                    line += word
                }
            }
            if (list.size == pageLines) {
                if (!TextUtils.isEmpty(line)) {
                    positon--
                }
                break
            }
            positon++
        }
        return list
    }

    private fun checkRange(p: Int = positon): Boolean = p < 0 || p >= bookLength

    private fun current(p: Int): Char {
        val index: Int = p / cachedSize
        if (null == booksInfo[index] || null == booksInfo[index].data.get()) {
            //恢复
            log("恢复:$index")
            openAssetsBook(bookPath)
        }
        return booksInfo[index].data.get()!![p]
    }

}