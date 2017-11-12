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
    private val mHelper: ReadViewHelper = ReadViewHelper(this)
    private val mHandler = Handler(this)
    private var mBookAnimation: BookAnimation = AnimationSlide(this)
    private var mCurrentPage: BookPage? = null
    private var mNextPage: BookPage? = null
    // 获取时不重复调用分页
    private var getIng = false
    val scroller = Scroller(getContext())
    var currentPageBitmap: Bitmap? = null
    var nextPageBitmap: Bitmap? = null
    var centerListener: OnCenterClickListener? = null

    override fun handleMessage(msg: Message?): Boolean {
        if (null == msg) return false
        when (msg.what) {
            MSG_SHOW_PAGE -> {
                val info: List<String> = msg.obj as List<String>
                openBook(info[0], info[1])

            }
        }
        return false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacksAndMessages(null)
        currentPageBitmap?.recycle()
        nextPageBitmap?.recycle()
        mHelper.destroy()
        scroller.abortAnimation()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mBookAnimation.event(event)
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (null == currentPageBitmap) {
            currentPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            nextPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            mBookAnimation.viewInitOk()
            mHelper.viewInitOk()
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (null == canvas) {
            return
        }
        mBookAnimation.draw(canvas)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            mBookAnimation.setActionXY(scroller.currX, scroller.currY)
            if (scroller.finalX == scroller.currX && scroller.currY == scroller.finalY) {
                mBookAnimation.finishScroll()
            }
            invalidate()
        }
    }

    fun openBook(bookName: String, path: String) {
        if (mHelper.isInit) {
            async {
                BookUtils.openAssetsBook(bookName, path)
                mCurrentPage = BookUtils.showOpenPage(mHelper)
                drawFirstSeeBitmap()
                uiThread {
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
        if (BookUtils.isLastPage(mCurrentPage)) {
            toastShort(context.getString(R.string.str_readOver))
            return false
        }
        if (null != mNextPage && mNextPage!!.begin == mCurrentPage!!.end + 1) {
            return true
        }
        if (getIng) {
            return false
        }

        getIng = true
        mHelper.drawPage(mCurrentPage, currentPageBitmap)
        BookUtils.nextPage(mHelper, mCurrentPage!!) {
            mNextPage = it
            mHelper.drawPage(mNextPage, nextPageBitmap)
            getIng = false
            invalidate()
        }
        return false
    }

    fun fakePreSlide(): Boolean {
        if (BookUtils.isFirstPage(mCurrentPage)) {
            toastShort(context.getString(R.string.str_readFirst))
            return false
        }
        if (null != mNextPage && mNextPage!!.end == mCurrentPage!!.begin - 1) {
            return true
        }
        if (getIng) {
            return false
        }

        getIng = true
        mHelper.drawPage(mCurrentPage, currentPageBitmap)
        BookUtils.prePage(mHelper, mCurrentPage!!) {
            mNextPage = it
            mHelper.drawPage(mNextPage, nextPageBitmap)
            getIng = false
            invalidate()
        }
        return false
    }

    fun onClickCenter() {
        centerListener?.onCenterClick()
    }

    fun canAnimation(): Boolean = when {
        BookUtils.isFirstPage(mCurrentPage) -> mBookAnimation.next
        BookUtils.isLastPage(mCurrentPage) -> !mBookAnimation.next
        else -> true
    }

    fun pageChangeFinish() {
        if (null != mNextPage) {
            mCurrentPage = mNextPage!!
        }
    }

    fun saveBookInfo() {
        SettingConfig.rememberBookChapterRead(BookUtils.bookName, mCurrentPage!!.begin)
    }

    fun setSlideAnimation(anim: BookAnimEnum) {
        mBookAnimation = when (anim) {
            BookAnimEnum.SLIDE -> {
                AnimationSlide(this)
            }
            BookAnimEnum.NONE -> {
                AnimationNone(this)
            }
            BookAnimEnum.REAL -> {
                AnimationLikeReal(this)
            }
            else -> {
                AnimationCover(this)
            }
        }
    }

    fun setTextType(configTextType: Typeface) {
        mHelper.setTextType(configTextType)
    }

    fun setTextSize(testSize: Float) {
        mHelper.setTextSize(testSize)
    }

    fun setBgColor(color: Int) {
        mHelper.setCustomBgColor(color)
    }

    fun getBgColor(): Int = mHelper.bgColor

    fun validateFeature() {
        if (null == mCurrentPage) return
        mCurrentPage = BookUtils.currentPage(mHelper, mCurrentPage!!.begin)
        drawFirstSeeBitmap()
        invalidate()
    }

    private fun drawFirstSeeBitmap() {
        mHelper.drawPage(mCurrentPage, currentPageBitmap)
        mHelper.drawPage(mCurrentPage, nextPageBitmap)
    }


}