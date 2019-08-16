package better.learn.view.custom.canvas.view.xfermode

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import better.learn.view.R

// https://www.jianshu.com/p/2e987dcc77f5
// https://www.runoob.com/w3cnote/android-tutorial-xfermode-porterduff2.html
class CircleView(ctx: Context, attributeSet: AttributeSet, style: Int) : View(ctx, attributeSet, style) {
    constructor(ctx: Context, attributeSet: AttributeSet) : this(ctx, attributeSet, 0)

    private var src = BitmapFactory.decodeResource(ctx.resources, R.drawable.a01)
    private var mode_src_in = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private var mode_dst_in = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    private var paint = Paint().apply {
        isAntiAlias = true
    }
    private val size = 300f
    private val radius = size / 2
    private var circleBitmap = createCircleBitmap(src)

    private fun createCircleBitmap(src: Bitmap) = Bitmap.createBitmap(size.toInt(), size.toInt(), Bitmap.Config.ARGB_8888).also {
        val canvas = Canvas(it)
        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.RED
        }
        canvas.drawCircle(size / 2, size / 2, radius, paint)
        paint.xfermode = mode_src_in
        canvas.drawBitmap(src, 0f, 0f, paint)
    }

    @SuppressLint("WrongConstant")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { can ->
            can.drawBitmap(circleBitmap, 0f, 0f, paint)
            // not work
            /* it.save()
             canvas.drawCircle(size * 1.1f, size * 1.1f, 50f, paint)
             paint.xfermode = mode_src_in
             canvas.drawBitmap(src, 0f, 0f, paint)
             paint.xfermode = mode_src_in
             it.restore()*/

            // save 用来Saves the current matrix and clip onto a private stack.
            // savelayer 跟save差不多，不过会在一个新的bitmap上绘制
            val sc = can.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null,
                    Canvas.ALL_SAVE_FLAG)
            // 先绘制的是dst 先绘制的叫DST图(目标图)，后绘制的叫SRC图(原图)
            canvas.drawCircle(300f, 300f, 50f, paint)
            paint.xfermode = mode_src_in
            canvas.drawBitmap(src, 0f, 0f, paint)
            paint.xfermode = null

            can.restoreToCount(sc)
        }
    }
}