package better.app.chinawisdom.widget.book

import android.graphics.Canvas
import android.widget.Scroller
import better.app.chinawisdom.widget.ReadView

/**
 * Created by better on 2017/9/4 15:10.
 */
class AnimationNone(readView: ReadView) : BookAnimation(readView) {

    override fun drawStatic(canvas: Canvas) {
        if (cancel) {
            canvas.drawBitmap(readView.currentPageBitmap, 0f, 0f, null)
        } else {
            canvas.drawBitmap(readView.nextPageBitmap, 0f, 0f, null)
        }
    }

    override fun drawRunning(canvas: Canvas) {
        if (cancel) {
            canvas.drawBitmap(readView.currentPageBitmap, 0f, 0f, null)
        } else {
            canvas.drawBitmap(readView.nextPageBitmap, 0f, 0f, null)
        }
    }

    override fun startScrollAnim(mScroller: Scroller) {
        finishScroll()
    }

//    var click = x / readView.width.toFloat()
//    if (2f / 3 < click) {
//        readView.onNextPage()
//    } else if (1f / 3 > click) {
//        readView.onPrePage()
//    }
}
