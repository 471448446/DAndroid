package com.better.learn.imagescale

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Scroller
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * 期望实现这种效果,至少填充整个屏幕,默认剧中展示,可以缩放\平移来查看图片
 * 矩阵:
 * https://stackoverflow.com/questions/5198125/how-do-you-find-out-the-scale-of-an-graphics-matrix-in-android
 * 使用 View.post() 是为了防止在修改matrix过程中,被其他的打断,造成并发修改
 * [-]虚线就是手机屏幕，[+]加号就是真实的图片
 * 高满足：
 * +++++------+++++++
 * +    -    -      +
 * +    -    -      +
 * +    -    -      +
 * +    -    -      +
 * +++++------+++++++
 * 宽满足:
 * ++++++++
 * +      +
 * --------
 * -      -
 * -      -
 * --------
 * +      +
 * ++++++++
 * @author Better
 * @date 2020/8/10 15:27
 */
class ScaleImageView @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    companion object {
        /**
         * x 轴默认展示位置
         */
        private const val X_TRANS_FACTORY = 0.5f

        /**
         * y 轴默认展示位置
         */
        private const val Y_TRANS_FACTORY = 0.5

        /**
         * 最大缩放倍数
         */
        private const val MAX_SCALE_FACTORY = 5f
    }

    private val localMatrix = Matrix()
    private val localMatrixArray = FloatArray(9)
    private var initScale = 0f
    private var hasGetDrawable = false

    /**
     * 缩放事件
     */
    private val scaleGestureDetector: ScaleGestureDetector

    /**
     * 滑动事件
     */
    private val gestureDetector: GestureDetector
    private val overScroller: Scroller
    private var flingStart: Float = 0f

    init {
        scaleType = ScaleType.MATRIX
        overScroller = Scroller(context)
        gestureDetector = GestureDetector(context, object :
            GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                overScroller.forceFinished(true)
                // 刚好与实际图片移动作标相反
                val x = -distanceX
                val y = -distanceY
                post { translateXY(x, y) }
                return true
            }

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                //velocity 已经是过滤了最小滑动距离
                if (null == drawable) {
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
                // 区分水平还是竖直滑动
                val horizontal = abs(e2.x - e1.x) > abs(e2.y - e1.y)
                localMatrix.set(imageMatrix)
                localMatrix.getValues(localMatrixArray)
                if (horizontal) {
                    flingStart = localMatrixArray[Matrix.MTRANS_X]
                    val distanceFactory =
                        abs(width - drawable.intrinsicWidth * localMatrixArray[Matrix.MSCALE_X])
                    overScroller.fling(
                        flingStart.toInt(), 0,
                        velocityX.toInt(), 0,
                        // min max 就是一个区间，使得 min<=current<=max
                        (-distanceFactory).toInt(), distanceFactory.toInt(),
                        0, 0
                    )
                } else {
                    flingStart = localMatrixArray[Matrix.MTRANS_Y]
                    val distanceFactory =
                        abs(height - drawable.intrinsicHeight * localMatrixArray[Matrix.MSCALE_Y])
                    overScroller.fling(
                        0, flingStart.toInt(),
                        0, velocityY.toInt(),
                        0, 0,
                        (-distanceFactory).toInt(), distanceFactory.toInt()
                    )
                }
                invalidate()
                return true
            }
        })
        scaleGestureDetector = ScaleGestureDetector(context,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    // 按点位(x,y)缩放,缩放比例
                    val x = detector.focusX
                    val y = detector.focusY
                    val scale = detector.scaleFactor
                    post { scale(scale, x, y) }
                    return true
                }
            })
    }

    override fun setScaleType(scaleType: ScaleType?) {
        // 只支持矩阵
        super.setScaleType(ScaleType.MATRIX)
    }

    /**
     * 设置src，最后都会调用到 [setImageDrawable]
     */
    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        hasGetDrawable = false
        initMatrix()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isEnabled || !hasGetDrawable) {
            super.onTouchEvent(event)
        }
        gestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (overScroller.computeScrollOffset()) {
            // 如果结果是 0 那就不需要滑动了
            // setImageMatrix() 会调用  invalidate()
            val point = if (overScroller.finalX != 0) {
                val consume = (overScroller.currX - flingStart)
                flingStart = overScroller.currX.toFloat()
                Triple(consume != 0f, consume, 0f)
            } else {
                val consume = (overScroller.currY - flingStart)
                flingStart = overScroller.currY.toFloat()
                Triple(consume != 0f, 0f, consume)
            }
            if (point.first) {
                translateXY(point.second, point.third)
                Log.e("Better", "${point.second},${point.third}")
            }
        }
    }

    /**
     * 初始化设置的图片
     * 矩阵变换并不会改变图片的真实大小,通过 drawable.intrinsicWidth 大小始终是一样的
     * 对于Matrix ,一次变换只会应用最后一个setXXX(),所以需要结合preXXX()和postXXX组合.
     * preXXX()前乘,意思是setXXX()应用在前面 other*pre
     * postXXX()后乘,意思是setXXX()应用在后面 post*other
     * 前后乘法,这个是真的有点绕 [https://blog.csdn.net/cquwentao/article/details/51445269]
     */
    private fun initMatrix() {
        post {
            if (null == drawable) {
                return@post
            }

            localMatrix.reset()

            val dWidth = drawable.intrinsicWidth
            val dHeight = drawable.intrinsicHeight

            val vWidth = measuredWidth
            val vHeight = measuredHeight
            Log.e("Better", "matrix $dWidth,$dHeight $vWidth,$vHeight")

            val fw = vWidth.toFloat() / dWidth.toFloat()
            val fh = vHeight.toFloat() / dHeight.toFloat()
            val scale = max(fw, fh)
            initScale = scale
            localMatrix.setScale(scale, scale)
            Log.i("Better", "scale $fw,$fh = $scale")

            val sdW = dWidth * scale
            val sdh = dHeight * scale
            localMatrix.postTranslate(
                ((vWidth - sdW) * X_TRANS_FACTORY).roundToInt().toFloat(),
                ((vHeight - sdh) * Y_TRANS_FACTORY).roundToInt().toFloat()
            )

            imageMatrix = localMatrix
            hasGetDrawable = true

//            Log.d("Better", "$localMatrix")

//            GlobalScope.launch(Dispatchers.Main) {
//                delay(1_000)
//                translateXY(-200f, 0f)
//                delay(1_000)
//                translateXY(200f, 0f)
//                delay(1_000)
//                scale(1.1f, width * 0.5f, height * 0.5f)
//                delay(1_000)
//                scale(0.9f, width * 0.5f, height * 0.5f)
//            }
        }
    }

    private fun scale(factor: Float, x: Float, y: Float) {
        if (null == drawable) {
            return
        }
        localMatrix.set(imageMatrix)
        localMatrix.getValues(localMatrixArray)
        val max = initScale * MAX_SCALE_FACTORY
        val min = initScale
        val current = localMatrixArray[Matrix.MSCALE_X]
        val apply = current * factor
        val scale = when {
            apply > max -> {
                max / current
            }
            apply < min -> {
                min / current
            }
            else -> {
                factor
            }
        }
        if (1.0f.equals(scale)) {
            return
        }
        localMatrix.postScale(scale, scale, x, y)
        /*
        adjust 放大在缩小后,Y轴上右偏移,消除偏移.
        如果始终以一个中心作标进行放大,在缩小,就没这个偏移产生
         */
        val adjustX = adjust(drawable.intrinsicWidth, width, Matrix.MSCALE_X, Matrix.MTRANS_X)
        val adjustY = adjust(drawable.intrinsicHeight, height, Matrix.MSCALE_Y, Matrix.MTRANS_Y)
        if (adjustX != 0f || adjustY != 0f) {
            localMatrix.postTranslate(adjustX, adjustY)
            Log.e("Better", "scale trans x:$adjustX, y:$adjustY")
        }

        imageMatrix = localMatrix
    }

    private fun adjust(
        drawableH: Int,
        viewWidth: Int,
        indexMatrixScale: Int,
        indexMatrixTrans: Int
    ): Float {
        localMatrix.getValues(localMatrixArray)
        val currentScale = localMatrixArray[indexMatrixScale]
        val sdw = drawableH * currentScale
        val xFactory = abs((viewWidth - sdw))
        val currentTrans = localMatrixArray[indexMatrixTrans]
        return when {
            currentTrans < -xFactory -> {
                -xFactory - currentTrans
            }
            currentTrans > 0 -> {
                -currentTrans
            }
            else -> {
                0f
            }
        }
    }

    private fun translateXY(dx: Float, dy: Float) {
        if (null == drawable) {
            return
        }
        val x = calculateXY(drawable.intrinsicWidth, width, Matrix.MSCALE_X, Matrix.MTRANS_X, dx)
        val y = calculateXY(drawable.intrinsicHeight, height, Matrix.MSCALE_Y, Matrix.MTRANS_Y, dy)
        if (x == 0f && y == 0f) {
            return
        }
        localMatrix.postTranslate(x, y)
        imageMatrix = localMatrix
//        Log.e("Better", "translateXY() dx:$dx,dy:$dy  x:$x,y:$y")
    }

    /**
     * 作标是在屏幕左上角,向右向下为正
     * 假设 delta 是 x 轴方向变化
     * dx <0 将图像左边移动,界面上看起来就是像右移动
     * dx >0 将图像向右边移动,使得界面上看起来就是像左移动
     */
    private fun calculateXY(
        drawableW: Int,
        viewWidth: Int,
        indexMatrixScale: Int,
        indexMatrixTrans: Int,
        delta: Float
    ): Float {
        if (delta < 0) {
            localMatrix.set(imageMatrix)
            localMatrix.getValues(localMatrixArray)
            val scale = localMatrixArray[indexMatrixScale]
            val sdw = drawableW * scale
            val xFactory = abs((viewWidth - sdw))
            val current = localMatrixArray[indexMatrixTrans]
            val apply = current + delta
            if (apply > -xFactory) {
                return delta
            } else if (apply < -xFactory) {
                // 本次滑动不满足给定的delta
                return (-xFactory - current)
            }
        } else if (delta > 0) {
            localMatrix.set(imageMatrix)
            localMatrix.getValues(localMatrixArray)
            val current = localMatrixArray[indexMatrixTrans]
            val apply = current + delta
            if (apply < 0) {
                return delta
            } else if (apply > 0) {
                return 0 - current
            }
        }
        return 0f

    }

    private suspend fun mockScaleD() {
        if (null == drawable) {
            return
        }
        delay(1000)
        scale(width * 0.5f, height * 0.5f, 0.9f)
        localMatrix.set(imageMatrix)
        localMatrix.getValues(localMatrixArray)
        val scale = localMatrixArray[Matrix.MSCALE_X]
        val min = initScale
        if (scale > min) {
            // 假设以0.9倍数缩小
            val newScale = if (scale * 0.9f > min) {
                0.9f
            } else {
                min / scale
            }
            localMatrix.postScale(newScale, newScale, width * 0.5f, height * 0.5f)
            imageMatrix = localMatrix
        }
    }

    private suspend fun mockScaleU() {
        if (null == drawable) {
            return
        }
        delay(1000)
        localMatrix.set(imageMatrix)
        localMatrix.getValues(localMatrixArray)
        val scale = localMatrixArray[Matrix.MSCALE_X]
        val max = initScale * MAX_SCALE_FACTORY
        if (scale < max) {
            // 假设放大1.1倍数
            val newScale = if (scale * 1.1f < max) {
                1.1f
            } else {
                max / scale
            }
            localMatrix.postScale(newScale, newScale, width * 0.5f, height * 0.5f)
            imageMatrix = localMatrix
        }
    }


    /**
     * 将图像向右边移动,使得界面上看起来就是像左移动
     */
    private suspend fun mockSwipeR() {
        if (null == drawable) {
            return
        }

        delay(1000)
        localMatrix.set(imageMatrix)
        localMatrix.getValues(localMatrixArray)
        val x = localMatrixArray[Matrix.MTRANS_X]
        if (x < 0) {
            val left = abs(x)
            val swipe = if (left > 200f) 200f else left
            localMatrix.postTranslate(swipe, 0f)
        }
        imageMatrix = localMatrix
    }

    /**
     * 将图像左边移动,界面上看起来就是像右移动
     */
    private suspend fun mockSwipeL() {
        if (null == drawable) {
            return
        }

        delay(1000)
        val dWidth = drawable.intrinsicWidth
        localMatrix.set(imageMatrix)
        localMatrix.getValues(localMatrixArray)
        val scale = localMatrixArray[Matrix.MSCALE_X]
        val sdw = dWidth * scale
        val xFactory = abs((width - sdw))
        val x = localMatrixArray[Matrix.MTRANS_X]
        if (abs(x) < xFactory) {
            val left = xFactory - abs(x)
            val swipe = if (left > 200f) 200f else left
            localMatrix.postTranslate(-swipe, 0f)
            imageMatrix = localMatrix
        }
    }
}