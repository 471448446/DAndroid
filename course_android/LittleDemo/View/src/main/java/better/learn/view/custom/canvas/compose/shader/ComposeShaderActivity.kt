package better.learn.view.custom.canvas.compose.shader

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import better.learn.view.R

// ComposeShader使用
class ComposeShaderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ComposeShaderView(this))
    }
}

class ComposeShaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val bitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.drawable.test_pic)
    }

    // 将Bitmap分层两部分，一部分使用特性，一部分正常绘制
    // 一部分使用组合特效
    private var shaderRect: RectF = RectF()

    // 剩下的部分正常绘制
    private var leftRect: Rect = Rect()
    private val paint = Paint().apply {
        isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shaderRect.set(0f, 0f, width * 0.3f, bitmap.height.coerceAtMost(height).toFloat())
        leftRect.set(
            shaderRect.right.toInt(),
            shaderRect.top.toInt(),
            width,
            shaderRect.bottom.toInt()
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        bitmap.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val shaderA = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val shaderB = LinearGradient(
            shaderRect.left,
            shaderRect.top,
            shaderRect.right,
            0f,
            Color.parseColor("#00000000"),
            Color.parseColor("#FF000000"),
            Shader.TileMode.CLAMP
        )
        // 保留矩形交集部分
        val compose = ComposeShader(shaderA, shaderB, PorterDuff.Mode.DST_IN)
        paint.shader = compose
        canvas.drawRect(shaderRect, paint)
        paint.shader = null
        // 绘制剩余部分
        canvas.drawBitmap(bitmap, leftRect, leftRect, paint)

    }

}