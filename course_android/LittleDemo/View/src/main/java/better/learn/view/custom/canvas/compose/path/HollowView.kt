package better.learn.view.custom.canvas.compose.path

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.Toast

class HollowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bgColor = Color.parseColor("#7F000000")
    private val hollowPath = Path()
    private val paint = Paint().also {
        it.color = Color.TRANSPARENT
        it.isAntiAlias = true
    }
    private val viewSizeRect = RectF()
    private val viewSizePath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        hollowPath.reset()
        viewSizeRect.set(RectF(0f, 0f, width.toFloat(), height.toFloat()))
        hollowPath.addCircle(width * 0.5f, height * 0.5f, width * 0.25f, Path.Direction.CCW)
        viewSizePath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(width.toFloat(), 0f)
            lineTo(width.toFloat(), height.toFloat())
            lineTo(0f, height.toFloat())
            close()
        }
        // 这里利用Path的裁剪，实现了图形的组合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            viewSizePath.op(hollowPath, Path.Op.DIFFERENCE)
        } else {
            Toast.makeText(context, "不支持的API", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = bgColor
        canvas.drawPath(viewSizePath, paint)
    }
}