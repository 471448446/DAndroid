package better.app.chinawisdom.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by better on 2017/8/21 11:28.
 */
class ReadView(context: Context?, attrs: AttributeSet?) : View(context, attrs), Handler.Callback {

    private val MSG_SHOW_PAGE = 100
    private val helper: ReadViewHelper = ReadViewHelper(this)
    private val mHandler: Handler = Handler(this)

    var currentPageBitmap: Bitmap? = null

    override fun handleMessage(msg: Message?): Boolean {
        if (null == msg) return false
        when (msg.what) {
            MSG_SHOW_PAGE ->
                showBook(msg.obj as String)
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                var click = event.x / width.toFloat()
                if (2f / 3 < click) {
                    helper.onNextPage()
                } else if (1f / 3 > click) {
                    helper.onPrePage()
                }
            }
        }

        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (null == currentPageBitmap) {
            currentPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            helper.initHelper()
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (null == canvas) {
            return
        }
        helper.onDraw(canvas)
    }

    fun showBook(bookName: String) {
        if (helper.isInit) {
            helper.showBook(bookName)
        } else {
            mHandler.removeMessages(MSG_SHOW_PAGE)
            val msg = mHandler.obtainMessage(MSG_SHOW_PAGE)
            msg.obj = bookName
            mHandler.sendMessageDelayed(msg, 100)
        }
    }
}