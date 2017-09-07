package better.app.chinawisdom.widget

import android.graphics.*
import android.support.v4.content.ContextCompat
import better.app.chinawisdom.App
import better.app.chinawisdom.R
import better.app.chinawisdom.support.utils.ViewUtils
import better.app.chinawisdom.support.utils.log
import better.app.chinawisdom.widget.book.BookPage
import better.app.chinawisdom.widget.book.BookUtils
import better.app.chinawisdom.widget.book.IBookViewInit
import java.math.BigDecimal

/**
 * ui相关
 * Created by better on 2017/8/21 11:35.
 */
class ReadViewHelper(private val readView: ReadView) : IBookViewInit {
    var isInit = false
    // 绘制内容的宽
    private var mVisibleHeight: Float = 0f
    // 绘制内容的宽
    var mVisibleWidth: Float = 0f
    // 左右与边缘的距离 文字开始绘制的地方
    private var paddingX: Float = 0f
    // 上下与边缘的距离
    private var paddingY: Float = 0f
    private var xTxtStart: Float = 0f
    private var yTxtStart: Float = 0f
    // 单个文字高
    private var txtHeight = 0
    // 行间距
    private var lineSpace = 0f
    // 文字大小
    private var frontSize = 20f
    var paintTxt = Paint()
    val paintProgress = Paint()
    private var bgBitmap: Bitmap? = null
    private var textType = Typeface.DEFAULT

    var linesInPage = 0

    init {
        paintTxt.isAntiAlias = true
        paintTxt.color = ContextCompat.getColor(App.instance, R.color.colorBlack)
        paintTxt.textSize = ViewUtils.dip2px(frontSize)
        paintTxt.typeface = textType
        paddingY = ViewUtils.dip2px(15f)
        paddingX = ViewUtils.dip2px(10f)
        lineSpace = ViewUtils.dip2px(8f)
        paintProgress.color = ContextCompat.getColor(App.instance, R.color.colorBlack)
        paintProgress.textSize = ViewUtils.dip2px(13f)
        paintProgress.typeface = textType

    }

    override fun viewInitOk() {
        measureTxt()
        setBookBg()
        isInit = true
    }

    private fun measureTxt() {
        mVisibleWidth = readView.width - 2 * paddingX
        mVisibleHeight = readView.height - 2 * paddingY

        val rectChinese = Rect()
        paintTxt.getTextBounds("中", 0, 1, rectChinese)
        txtHeight = rectChinese.height()
        linesInPage = (mVisibleHeight / (txtHeight + lineSpace)).toInt()
        log("lines:" + linesInPage)

        val wordW = paintTxt.measureText("\u3000")
        val w = mVisibleWidth % wordW

        xTxtStart = paddingX + w / 2
        yTxtStart = paddingY + txtHeight
    }

    private fun setBookBg() {
        if (null != bgBitmap) {
            bgBitmap?.recycle()
        }
        var bitmap = Bitmap.createBitmap(readView.width, readView.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        canvas.drawColor(ContextCompat.getColor(App.instance, R.color.read_bg_default))

        bgBitmap = bitmap
    }

//    private fun draw(page: BookPage?) {
//        if (null == page) return
//        val canvas = Canvas(readView.currentPageBitmap)
//        canvas.drawBitmap(bgBitmap, 0f, 0f, null)
//
//        var y = yTxtStart
//        page.lines.forEach {
//            canvas.drawText(it, xTxtStart, y, paintTxt)
//            y += txtHeight + lineSpace
//        }
//
//        val del = BigDecimal(currentPage.end.toDouble() / BookUtils.bookLength * 100).setScale(2, BigDecimal.ROUND_HALF_UP)
//        val rect = Rect()
//        val progress = "${del.toFloat()}%"
//        paintProgress.getTextBounds(progress, 0, progress.length, rect)
//        canvas.drawText(progress, mVisibleWidth - rect.width(), readView.height - rect.height().toFloat(), paintProgress)
//        readView.postInvalidate()
//    }

    fun drawPage(page: BookPage?, bitmap: Bitmap?) {
        if (null == page || null == bitmap) return
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bgBitmap, 0f, 0f, null)

        var y = yTxtStart
        page.lines.forEach {
            canvas.drawText(it, xTxtStart, y, paintTxt)
            y += txtHeight + lineSpace
        }

        log("${page.end.toDouble() / BookUtils.bookLength * 100}")


        val del = BigDecimal(page.end.toDouble() / BookUtils.bookLength * 100).setScale(2, BigDecimal.ROUND_HALF_UP)
        val rect = Rect()
        val progress = "${del.toFloat()}%"
        paintProgress.getTextBounds(progress, 0, progress.length, rect)
        canvas.drawText(progress, mVisibleWidth - rect.width(), readView.height - rect.height().toFloat(), paintProgress)
    }

    fun setTextType(configTextType: Typeface) {
        textType = configTextType

        paintTxt.typeface = textType
        paintProgress.typeface = textType
        measureTxt()

    }

//    fun onDraw(canvas: Canvas) {
//        canvas.drawBitmap(readView.currentPageBitmap, 0f, 0f, null)
//    }
//
//    fun saveBookInfo() {
//        SettingConfig.rememberBookChapterRead(BookUtils.bookName, currentPage.begin)
//    }

//    fun showBook(bookName: String, path: String) {
//        async {
//            BookUtils.openAssetsBook(bookName, path)
//            currentPage = BookUtils.showOpenPage(this@ReadViewHelper)
//            uiThread {
//                draw(currentPage)
//            }
//        }
//    }
//
//    fun onNextPage() {
//        if (BookUtils.isLastPage()) {
//            return
//        }
//        async {
//            currentPage = BookUtils.nextPage(this@ReadViewHelper, currentPage)
//            uiThread {
//                draw(currentPage)
//            }
//        }
//    }
//
//    fun onPrePage() {
//        if (BookUtils.isFirstPage(currentPage)) {
//            return
//        }
//        async {
//            currentPage = BookUtils.prePage(this@ReadViewHelper, currentPage)
//            uiThread {
//                draw(currentPage)
//            }
//        }
//    }

//    private fun separateParagraphToLines(paragraphStr: String): List<String> {
//        paintTxt.isSubpixelText = true
//        val list = ArrayList<String>()
//        var str = paragraphStr
//        val with = mVisibleWidth - 2 * paddingX
//        do {
//            val length = paintTxt.breakText(str, true, with, null)
//            if (length <= str.length) {
//                val linnStr = str.substring(0, length)
//                list.add(linnStr)
//                str = str.substring(length, str.length)
//            } else {
//                list.add(str)
//                str = ""
//            }
//        } while (str.isNotEmpty())
//        return list
//    }


}