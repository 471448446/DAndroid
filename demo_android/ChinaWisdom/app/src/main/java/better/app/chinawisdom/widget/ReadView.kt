package better.app.chinawisdom.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import better.app.chinawisdom.R
import better.app.chinawisdom.SettingConfig
import better.app.chinawisdom.support.utils.toastShort
import better.app.chinawisdom.widget.book.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread

/**
 * Created by better on 2017/8/21 11:28.
 */
class ReadView(context: Context?, attrs: AttributeSet?) : View(context, attrs), Handler.Callback {
    interface OnCenterClickListener {
        fun onCenterClick()
    }

    private val MSG_SHOW_PAGE = 100
    private val helper: ReadViewHelper = ReadViewHelper(this)
    private val mHandler = Handler(this)
    private var bookAnimation: BookAnimation = AnimationSlide(this)
    val mScroller = Scroller(getContext())

    var currentPageBitmap: Bitmap? = null
    var nextPageBitmap: Bitmap? = null
    private var currentPage: BookPage? = null
    private var nextPage: BookPage? = null
    // 获取时不重复调用分页
    private var getIng = false

    var centerListener: OnCenterClickListener? = null

    override fun handleMessage(msg: Message?): Boolean {
        if (null == msg) return false
        when (msg.what) {
            MSG_SHOW_PAGE -> {
                val info: List<String> = msg.obj as List<String>
                showBook(info[0], info[1])

            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        bookAnimation.event(event)
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (null == currentPageBitmap) {
            currentPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            nextPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            bookAnimation.viewInitOk()
            helper.viewInitOk()
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (null == canvas) {
            return
        }
        bookAnimation.draw(canvas)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            bookAnimation.setActionXY(mScroller.currX, mScroller.currY)
            if (mScroller.finalX == mScroller.currX && mScroller.currY == mScroller.finalY) {
                bookAnimation.finishScroll()
            }
            invalidate()
        }
    }

    fun showBook(bookName: String, path: String) {
        if (helper.isInit) {
            async {
                BookUtils.openAssetsBook(bookName, path)
                currentPage = BookUtils.showOpenPage(helper)
                uiThread {
                    drawFirstSeeBitmap()
                    invalidate()
                }
            }
        } else {
            mHandler.removeMessages(MSG_SHOW_PAGE)
            val msg = mHandler.obtainMessage(MSG_SHOW_PAGE)
            msg.obj = arrayListOf(bookName, path)
            mHandler.sendMessageDelayed(msg, 100)
        }
    }

    fun fakeNextSlide(): Boolean {
        if (BookUtils.isLastPage(currentPage)) {
            toastShort(context.getString(R.string.str_readOver))
            return false
        }
        if (null != nextPage && nextPage!!.begin == currentPage!!.end + 1) {
            return true
        }
        if (getIng) {
            return false
        }

        getIng = true
        helper.drawPage(currentPage, currentPageBitmap)
        BookUtils.nextPage(helper, currentPage!!) {
            nextPage = it
            helper.drawPage(nextPage, nextPageBitmap)
            getIng = false
            invalidate()
        }
        return false
    }

    fun fakePreSlide(): Boolean {
        if (BookUtils.isFirstPage(currentPage)) {
            toastShort(context.getString(R.string.str_readFirst))
            return false
        }
        if (null != nextPage && nextPage!!.end == currentPage!!.begin - 1) {
            return true
        }
        if (getIng) {
            return false
        }

        getIng = true
        helper.drawPage(currentPage, currentPageBitmap)
        BookUtils.prePage(helper, currentPage!!) {
            nextPage = it
            helper.drawPage(nextPage, nextPageBitmap)
            getIng = false
            invalidate()
        }
        return false
    }

    fun onClickCenter() {
        centerListener?.onCenterClick()
    }

    fun canAnimation(): Boolean = when {
        BookUtils.isFirstPage(currentPage) -> bookAnimation.next
        BookUtils.isLastPage(currentPage) -> !bookAnimation.next
        else -> true
    }

    fun pageChangeFinish() {
        if (null != nextPage) {
            currentPage = nextPage!!
        }
    }

    fun saveBookInfo() {
        SettingConfig.rememberBookChapterRead(BookUtils.bookName, currentPage!!.begin)
    }

    fun setSlideAnimation(anim: BookAnimEnum) {
        bookAnimation = when (anim) {
            BookAnimEnum.SLIDE -> {
                AnimationSlide(this)
            }
            BookAnimEnum.NONE -> {
                AnimationNone(this)
            }
            else -> {
                AnimationCover(this)
            }
        }
        invalidate()
    }

    fun setTextType(configTextType: Typeface) {
        helper.setTextType(configTextType)
        currentPage = if (null == currentPage) {
            BookUtils.showOpenPage(helper)
        } else {
            BookUtils.currentPage(helper, currentPage!!.begin)
        }
        drawFirstSeeBitmap()
        invalidate()
    }

    private fun drawFirstSeeBitmap() {
        helper.drawPage(currentPage, currentPageBitmap)
        helper.drawPage(currentPage, nextPageBitmap)
    }
}