package better.learn.view.custom.canvas.compose.path

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

class HollowView2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bgColor = Color.parseColor("#7F000000")
    private val hollowPath = Path()
    private val paint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.FILL
    }
    private val viewSizeRect = RectF()
    private val mode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewSizeRect.set(RectF(0f, 0f, width.toFloat(), height.toFloat()))
        hollowPath.reset()
        hollowPath.addCircle(width * 0.5f, height * 0.5f, width * 0.25f, Path.Direction.CCW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val saveLayer = canvas.saveLayer(viewSizeRect, paint, Canvas.ALL_SAVE_FLAG)
        paint.color = bgColor
        canvas.drawRect(viewSizeRect, paint)
        paint.xfermode = mode
        paint.color = Color.TRANSPARENT
        canvas.drawPath(hollowPath, paint)
        paint.xfermode = null
        canvas.restoreToCount(saveLayer)
    }
}