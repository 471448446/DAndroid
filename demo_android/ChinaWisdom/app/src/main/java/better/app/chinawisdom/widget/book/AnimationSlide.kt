package better.app.chinawisdom.widget.book

import android.graphics.Canvas
import android.graphics.Rect
import android.widget.Scroller
import better.app.chinawisdom.support.extenions.abs
import better.app.chinawisdom.widget.ReadView

/**
 * Created by better on 2017/9/4 14:59.
 */
class AnimationSlide(readView: ReadView) : BookAnimation(readView) {

    //图片要显示的区域
    var currentSrc: Rect = Rect(0, 0, readView.width, readView.height)
    // 图片显示的位置
    var nextSrc: Rect = Rect(0, 0, readView.width, readView.height)
    var currentDet: Rect = Rect(0, 0, readView.width, readView.height)
    var nextDet: Rect = Rect(0, 0, readView.width, readView.height)

    override fun viewInitOk() {
        super.viewInitOk()
        reset(currentSrc, nextSrc, currentDet, nextDet)
    }

    override fun drawStatic(canvas: Canvas) {
        if (cancel) {
            canvas.drawBitmap(readView.currentPageBitmap, 0f, 0f, null)
        } else {
            canvas.drawBitmap(readView.nextPageBitmap, 0f, 0f, null)
        }
        reset(currentSrc, nextSrc, currentDet, nextDet)
    }

    private fun reset(vararg src: Rect) {
        src.forEach {
            it.left = 0
            it.top = 0
            it.right = readView.width
            it.bottom = readView.height
        }
    }

    override fun drawRunning(canvas: Canvas) {
        var fingerMove = (actionX - downX).toInt()

        if (next) {
            fingerMove = if (fingerMove > 0) 0 else Math.abs(fingerMove)

            currentSrc.left = fingerMove
            currentDet.right = readView.width - fingerMove
            nextSrc.right = fingerMove
            nextDet.left = readView.width - fingerMove

            canvas.drawBitmap(readView.nextPageBitmap, nextSrc, nextDet, null)
            canvas.drawBitmap(readView.currentPageBitmap, currentSrc, currentDet, null)
        } else {
            if (fingerMove < 0) fingerMove = 0
            fingerMove = if (fingerMove < 0) 0 else fingerMove

            currentSrc.right = readView.width - fingerMove
            currentDet.left = fingerMove
            nextSrc.left = readView.width - fingerMove
            nextDet.right = fingerMove

            canvas.drawBitmap(readView.nextPageBitmap, nextSrc, nextDet, null)
            canvas.drawBitmap(readView.currentPageBitmap, currentSrc, currentDet, null)
        }
    }

    override fun startScrollAnim(scroller: Scroller) {
        val dx = if (next) {
            if (cancel) {
                (actionX - downX).abs().toInt()
            } else {
                -(readView.width - (actionX - downX).abs()).toInt()
            }
        } else {
            if (cancel) {
                -(actionX - downX).abs().toInt()
            } else {
                (readView.width - (actionX - downX)).toInt()
            }
        }
        val duration = (Math.abs(dx) * 1000 / readView.width)
//        log("startScrollAnim->x=${actionX.toInt()},cancel=$cancel,next=$next,dx=$dx,duration=$duration")
        scroller.startScroll(actionX.toInt(), 0, dx, 0, duration)
    }

}