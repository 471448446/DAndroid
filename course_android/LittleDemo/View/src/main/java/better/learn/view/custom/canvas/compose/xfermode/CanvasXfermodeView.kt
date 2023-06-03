package better.learn.view.custom.canvas.compose.xfermode

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * 这里方便计算，强制设定，宽高一致
 * 先绘制的是DST，后绘制的是SRC
 * Created by better on 2023/6/3 17:52.
 */
class CanvasXfermodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var xfermode = PorterDuff.Mode.DST_IN
        set(value) {
            field = value
            invalidate()
        }

    private var paintBackground = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.color = Color.BLACK
    }
    private var paintFill = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.FILL
    }
    private var bgRectF = RectF()
    private var circleCenter = PointF()
    private var circleRadius = 0f
    private var squareRectF = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bgRectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        circleCenter = PointF(width * 0.4f, width * 0.4f)
        circleRadius = width * 0.3f
        squareRectF = RectF(width * 0.4f, width * 0.4f, width * 0.9f, width * 0.9f)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawRect(bgRectF, paintBackground)
        // 开启新图层绘制，这样不影响原来的信息
        val layer = canvas.saveLayer(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            paintFill,
            Canvas.ALL_SAVE_FLAG
        )
        // 绘制圆，先绘制的是是目标图 DST
        paintFill.color = Color.YELLOW
        canvas.drawCircle(circleCenter.x, circleCenter.y, circleRadius, paintFill)
        // 设置混合模式
        paintFill.xfermode = PorterDuffXfermode(xfermode)
        // 绘制矩形，现在绘制源图 SRC
        paintFill.color = Color.BLUE
        canvas.drawRect(squareRectF, paintFill)
        // 清除混合模式
        paintFill.xfermode = null

        canvas.restoreToCount(layer)
    }
}