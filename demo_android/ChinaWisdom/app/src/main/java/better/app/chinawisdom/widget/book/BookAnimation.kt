package better.app.chinawisdom.widget.book

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.Scroller
import better.app.chinawisdom.widget.ReadView

/**
 * Created by better on 2017/9/4 14:51.
 */
abstract class BookAnimation(val readView: ReadView) : IBookViewInit {

    var widthView = 0f
    var heightView = 0f

    var downX = 0f
    var downY = 0f
    var moveX = 0f
    var moveY = 0f
    var actionX = 0f
    var actionY = 0f
    var move = false
    var running = false
    var cancel = false

    var next = false
    /**
     * 展示滑动的动态页面
     */
    abstract fun drawRunning(canvas: Canvas)

    /**
     * 展示已滑完的页面
     */
    abstract fun drawStatic(canvas: Canvas)

    /**
     * 开始完成没有滑完的动画
     */
    abstract fun startScrollAnim(scroller: Scroller)

    override fun viewInitOk() {
        widthView = readView.width.toFloat()
        heightView = readView.height.toFloat()
    }

    open fun setDirect(isNext: Boolean) {
        next = isNext
    }

    open fun onFingerDown() {

    }

    fun finishScroll() {
        running = false
    }

    fun draw(canvas: Canvas) {
        if (running) {
            drawRunning(canvas)
        } else {
            drawStatic(canvas)
            if (!cancel)
                readView.pageChangeFinish()
        }
    }

    fun setActionXY(currX: Int, currY: Int) {
        actionX = currX.toFloat()
        actionY = currY.toFloat()
    }

    private fun abortAnimation() {
        if (!readView.scroller.isFinished) {
            readView.scroller.abortAnimation()
            setActionXY(readView.scroller.finalX, readView.scroller.finalY)
            readView.postInvalidate()
        }
    }

    fun event(event: MotionEvent) {
        val x = event.x
        val y = event.y

        actionX = x
        actionY = y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = x
                downY = y
                moveX = 0f
                moveY = 0f
                move = false
                running = false
                cancel = false
                next = false
                onFingerDown()
                abortAnimation()
            }
            MotionEvent.ACTION_MOVE -> {
                val slop = ViewConfiguration.get(readView.context).scaledTouchSlop
                if (!move) {
                    move = (Math.abs(x - downX) > slop || Math.abs(y - downY) > slop)
                }
                if (move) {
                    var fake = false
                    if (0f == moveX && 0f == moveY) {
                        setDirect(0 > x - downX)
                    } else {
                        if (next) {
                            if (0 < x - moveX) {
                                cancel = true
                            }
                            fake = readView.fakeNextSlide()
                        } else {
                            if (0 > x - moveX) {
                                cancel = true
                            }
                            fake = readView.fakePreSlide()
                        }
                    }
                    moveX = x
                    moveY = y
                    running = true
                    if (fake) {
                        readView.invalidate()
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!move) {
                    var click = x / readView.width.toFloat()
                    setDirect(when {
                        2f / 3 < click -> {
                            readView.fakeNextSlide()
                            true
                        }
                        1f / 3 > click -> {
                            readView.fakePreSlide()
                            false
                        }
                        else -> {
                            readView.onClickCenter()
                            return
                        }
                    })
                }
                if (readView.canAnimation()) {
                    running = true
                    startScrollAnim(readView.scroller)
                    readView.postInvalidate()
                }
            }
        }
    }

}
