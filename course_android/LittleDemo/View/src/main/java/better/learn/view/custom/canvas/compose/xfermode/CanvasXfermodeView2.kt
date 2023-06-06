package better.learn.view.custom.canvas.compose.xfermode

import android.content.Context
import android.graphics.Bitmap
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
 * 使用两个大小一致的Bitmap
 * @author Better
 * @date 2023/6/6 15:44
 */
class CanvasXfermodeView2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // 使用透明通道
    var useAlpha = false
    private val dstColor: Int
        get() {
            return if (useAlpha) {
                Color.parseColor("#7FFFCC44")
            } else {
                Color.parseColor("#FFFFCC44")
            }
        }
    private val srcColor: Int
        get() {
            return if (useAlpha) {
                Color.parseColor("#7F66AAFF")
            } else {
                Color.parseColor("#FF66AAFF")
            }
        }
    var xfermode: PorterDuff.Mode? = null
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
    private lateinit var dstBitMap: Bitmap
    private lateinit var dstCanvas: Canvas
    private lateinit var srcBitMap: Bitmap
    private lateinit var srcCanvas: Canvas

    init {
        // 关闭硬件加速，
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bgRectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        circleCenter = PointF(width * 0.4f, width * 0.4f)
        circleRadius = width * 0.3f
        squareRectF = RectF(width * 0.4f, width * 0.4f, width * 0.9f, width * 0.9f)
        dstBitMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        srcBitMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        dstCanvas = Canvas(dstBitMap)
        srcCanvas = Canvas(srcBitMap)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (null == xfermode) {
            return
        }
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
        //  DST
        paintFill.color = dstColor
        dstCanvas.drawCircle(circleCenter.x, circleCenter.y, circleRadius, paintFill)
        canvas.drawBitmap(dstBitMap, 0f, 0f, paintFill)
        paintFill.color = srcColor
        srcCanvas.drawRect(squareRectF, paintFill)
        // SRC
        // 设置混合模式
        paintFill.xfermode = PorterDuffXfermode(xfermode)
        canvas.drawBitmap(srcBitMap, 0f, 0f, paintFill)
        // 清除混合模式
        paintFill.xfermode = null

        canvas.restoreToCount(layer)
    }
}