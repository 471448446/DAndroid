package better.learn.view.custom.canvas.view.xfermode

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import better.learn.view.R
import kotlin.math.abs

// https://www.jianshu.com/p/2e987dcc77f5
// https://www.runoob.com/w3cnote/android-tutorial-xfermode-porterduff2.html
class SwipeView(ctx: Context, attributeSet: AttributeSet, style: Int) : View(ctx, attributeSet, style) {
    constructor(ctx: Context, attributeSet: AttributeSet) : this(ctx, attributeSet, 0)

    private var bitmap1 = BitmapFactory.decodeResource(ctx.resources, R.drawable.a01)
    private var bitmap2 = BitmapFactory.decodeResource(ctx.resources, R.drawable.a02)
    private var mode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private var paint = Paint().apply {
        isAntiAlias = true
    }

    var lastX = 0f
    var lastY = 0f
    val path = Path()
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (null == event) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                path.moveTo(lastX, lastY)
            }
            MotionEvent.ACTION_MOVE -> {
                if (abs(event.x - lastX) > 3 || abs(event.y - lastY) > 3) {
                    path.quadTo(event.x, event.y, lastX, lastY)
                    lastX = event.x
                    lastY = event.y
                }
            }
        }
        invalidate()
        return true
    }

    @SuppressLint("WrongConstant")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { can ->
            can.drawBitmap(bitmap1, 0f, 0f, paint)

            val sr = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
            can.drawBitmap(bitmap2, 0f, 0f, paint)
            paint.xfermode = mode
            can.drawPath(path, paint)
            paint.xfermode = null
            can.restoreToCount(sr)
        }
    }
}