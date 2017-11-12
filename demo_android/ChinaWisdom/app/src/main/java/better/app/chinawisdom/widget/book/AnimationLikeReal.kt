package better.app.chinawisdom.widget.book

import android.graphics.*
import android.widget.Scroller
import better.app.chinawisdom.support.extenions.abs
import better.app.chinawisdom.widget.ReadView

/**
 * 逆时针为各个点位开始
 * http://blog.csdn.net/hmg25/article/details/6306479
 * Created by better on 2017/9/15 09:56.
 */
class AnimationLikeReal(readView: ReadView) : BookAnimation(readView) {
    private var cornerX = 1f
    private var cornerY = 1f
    private var middleX = 0f
    private var middleY = 0f
    private var mDegrees: Float = 0f
    private var bezierOnePointStart = PointF()
    private var bezierOnePointControl = PointF()
    private var bezierOnePointEnd = PointF()
    private var bezierTwoPointStart = PointF()
    private var bezierTwoPointControl = PointF()
    private var bezierTwoPointEnd = PointF()
    private var bezierOneCutOffPoint = PointF()
    private var bezierTwoCutOffPoint = PointF()
    private val path0 = Path()
    private val path1 = Path()
    private val paint = Paint()
    private val matrix = Matrix()
    private var colorMatrixFilter: ColorMatrixColorFilter? = null
    private val matrixArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1.0f)

    init {
        val array = floatArrayOf(1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f)
        val cm = ColorMatrix()//设置颜色数组
        cm.set(array)
        colorMatrixFilter = ColorMatrixColorFilter(cm)
    }

    override fun viewInitOk() {
        super.viewInitOk()
        cornerX = readView.width.toFloat()
        cornerY = readView.height.toFloat()
    }

    override fun drawRunning(canvas: Canvas) {
        calcPoints()
        if (next) {
            drawCurrentPageArea(canvas, readView.currentPageBitmap)
            drawNextPageArea(canvas, readView.nextPageBitmap)
            drawCurrentBackArea(canvas, readView.currentPageBitmap)
        } else {
            drawCurrentPageArea(canvas, readView.nextPageBitmap)
            drawNextPageArea(canvas, readView.currentPageBitmap)
            drawCurrentBackArea(canvas, readView.nextPageBitmap)
        }
    }

    override fun drawStatic(canvas: Canvas) {
        if (cancel) {
            canvas.drawBitmap(readView.currentPageBitmap, 0f, 0f, null)
        } else {
            canvas.drawBitmap(readView.nextPageBitmap, 0f, 0f, null)
        }
    }

    override fun startScrollAnim(scroller: Scroller) {
        var dx = 0f
        var dy = 0f
        if (next) {
            if (cancel) {
                dx = (widthView - actionX).abs()
                dy = (heightView - actionY).abs()
            } else {
                dx = -(widthView - (actionX - downX).abs())
                dy = -(heightView - (actionY - downY).abs())
            }
        } else {
            if (cancel) {
                dx = -(widthView - actionY).abs()
                dy = -(actionY - actionY).abs()
            } else {
                dx = widthView - (actionX - downX).abs()
                dy = heightView - (actionY - downY).abs()
            }
        }
        val duration = (Math.abs(dx) * 1000 / readView.width)
        readView.scroller.startScroll(actionX.toInt(), actionY.toInt(), dx.toInt(), dy.toInt(), duration.toInt())
    }

    private fun drawCurrentBackArea(canvas: Canvas, pageBitmap: Bitmap?) {
        canvas.save()

        canvas.clipPath(path0)
        canvas.clipPath(path1, Region.Op.INTERSECT)
        paint.colorFilter = colorMatrixFilter
        val dis = Math.hypot((cornerX - bezierOnePointControl.x).toDouble(),
                (bezierTwoPointControl.y - cornerY).toDouble()).toFloat()
        val f8 = (cornerX - bezierOnePointControl.x) / dis
        val f9 = (bezierTwoPointControl.y - cornerY) / dis
        matrixArray[0] = 1 - 2f * f9 * f9
        matrixArray[1] = 2f * f8 * f9
        matrixArray[3] = matrixArray[1]
        matrixArray[4] = 1 - 2f * f8 * f8
        matrix.reset()
        matrix.setValues(matrixArray)
        matrix.preTranslate(-bezierOnePointControl.x, -bezierOnePointControl.y)
        matrix.postTranslate(bezierOnePointControl.x, bezierOnePointControl.y)
        canvas.drawColor(readView.getBgColor())
        canvas.drawBitmap(pageBitmap, matrix, paint)
        paint.colorFilter = null
        canvas.rotate(mDegrees, bezierOnePointEnd.x, bezierOnePointEnd.y)
        canvas.restore()

    }

    private fun drawNextPageArea(canvas: Canvas, pageBitmap: Bitmap?) {
//        path1.reset()
//        path1.moveTo(bezierOnePointEnd.x, bezierOnePointEnd.y)
//        path1.lineTo(bezierOneCutOffPoint.x, bezierOneCutOffPoint.y)
//        path1.lineTo(bezierTwoCutOffPoint.x, bezierTwoCutOffPoint.y)
//        path1.lineTo(bezierTwoPointStart.x, bezierTwoPointStart.y)
//        path1.lineTo(cornerX, cornerY)
//        path1.close()

        path1.reset()
        path1.moveTo(bezierOneCutOffPoint.x, bezierOneCutOffPoint.y)
        path1.lineTo(bezierOnePointStart.x, bezierOnePointStart.y)
        path1.lineTo(moveX, moveY)
        path1.lineTo(bezierTwoPointEnd.x, bezierTwoPointEnd.y)
        path1.lineTo(bezierTwoCutOffPoint.x, bezierTwoCutOffPoint.y)
        path1.close()

        mDegrees = Math.toDegrees(Math.atan2((bezierOnePointControl.x - cornerX).toDouble(),
                (bezierTwoPointControl.y - cornerY).toDouble())).toFloat()
        canvas.save()
        canvas.clipPath(path0)
        canvas.clipPath(path1, Region.Op.DIFFERENCE)
        canvas.drawBitmap(pageBitmap, 0f, 0f, null)
        canvas.restore()
    }

    private fun drawCurrentPageArea(canvas: Canvas, pageBitmap: Bitmap?) {
        if (null == pageBitmap) return
        // 显示各个点位
//        mTestPaint.color = Color.RED
//        mTestPaint.strokeWidth = 20f
//        canvas.drawPoint(bezierTwoPointControl.x, bezierTwoPointControl.y, mTestPaint)
//        canvas.drawPoint(bezierOnePointControl.x, bezierOnePointControl.y, mTestPaint)
//        mTestPaint.color = Color.BLACK
//        canvas.drawPoint(bezierTwoPointStart.x, bezierTwoPointStart.y, mTestPaint)
//        canvas.drawPoint(bezierTwoPointEnd.x, bezierTwoPointEnd.y, mTestPaint)
//        canvas.drawPoint(moveX, moveY, mTestPaint)
//        canvas.drawPoint(bezierOnePointStart.x, bezierOnePointStart.y, mTestPaint)
//        canvas.drawPoint(bezierOnePointEnd.x, bezierOnePointEnd.y, mTestPaint)
//        canvas.drawPoint(cornerX, cornerY, mTestPaint)

        path0.reset()
        path0.moveTo(bezierTwoPointStart.x, bezierTwoPointStart.y)
        path0.quadTo(bezierTwoPointControl.x, bezierTwoPointControl.y, bezierTwoPointEnd.x, bezierTwoPointEnd.y)
        path0.lineTo(moveX, moveY)
        path0.lineTo(bezierOnePointStart.x, bezierOnePointStart.y)
        path0.quadTo(bezierOnePointControl.x, bezierOnePointControl.y, bezierOnePointEnd.x, bezierOnePointEnd.y)
        path0.lineTo(cornerX, cornerY)
        path0.close()

        canvas.save()
        canvas.clipPath(path0, Region.Op.XOR)
        canvas.drawBitmap(pageBitmap, 0f, 0f, null)
        canvas.restore()
    }

    override fun setDirect(isNext: Boolean) {
        super.setDirect(isNext)
//        if (next) {
//            if (widthView / 2 > downX) {
//                calcCornerXY(widthView - downX, downY)
//            }
//        } else {
//            if (widthView / 2 > downX) {
//                calcCornerXY(widthView - downX, heightView)
//            } else {
//                calcCornerXY(downX, heightView)
//            }
//        }
    }

    override fun onFingerDown() {
        super.onFingerDown()
        calcCornerXY(actionX, actionY)
    }

    private fun calcCornerXY(x: Float, y: Float) {
        cornerX = if (x <= widthView / 2) {
            0f
        } else {
            widthView
        }
        cornerY = if (y <= heightView / 2) {
            0f
        } else {
            heightView
        }
    }

    private fun calcPoints() {
        onePoints()
        //第一个Bezier的终点在其他象限了需要限制,至于为什么时这么实现，不理解
        if (moveX > 0 && moveX < widthView) {
            if (0 > bezierOnePointEnd.x || widthView < bezierOnePointEnd.x) {
                if (0 > bezierOnePointEnd.x) bezierOnePointEnd.x = widthView - bezierOnePointEnd.x
                val f1 = Math.abs(cornerX - moveX)
                val f2 = widthView * f1 / bezierOnePointEnd.x
                moveX = Math.abs(cornerX - f2)
                val f3 = Math.abs(cornerX - moveX) * Math.abs(cornerY - moveY) / f1
                moveY = Math.abs(cornerY - f3)
                onePoints()
            }
        }
        bezierTwoPointStart.y = bezierTwoPointControl.y - 1 / 2f * (cornerY - bezierTwoPointControl.y)
        bezierTwoPointStart.x = cornerX

        val movePointF = PointF(moveX, moveY)
        bezierOnePointStart = getCrossPoint(movePointF, bezierOnePointControl, bezierOnePointEnd, bezierTwoPointStart)
        bezierTwoPointEnd = getCrossPoint(movePointF, bezierTwoPointControl, bezierOnePointEnd, bezierTwoPointStart)

//        log(bezierOnePointStart.toString() + "\n" + bezierOnePointControl.toString() + "\n" + bezierOnePointEnd.toString() + "\n" + "\n" +
//                bezierTwoPointStart.toString() + "\n" + bezierTwoPointControl.toString() + "\n" + bezierTwoPointEnd.toString() + "\n" + "\n" +
//                moveX + "," + moveY + "\n" + cornerX + "," + cornerY)
        bezierOneCutOffPoint.x = ((bezierOnePointStart.x + bezierOnePointEnd.x) / 2f + bezierOnePointControl.x) / 2f
        bezierOneCutOffPoint.y = ((bezierOnePointStart.y + bezierOnePointEnd.y) / 2f + bezierOnePointControl.y) / 2f
        bezierTwoCutOffPoint.x = ((bezierTwoPointStart.x + bezierTwoPointEnd.x) / 2f + bezierTwoPointControl.x) / 2f
        bezierTwoCutOffPoint.y = ((bezierTwoPointStart.y + bezierTwoPointEnd.y) / 2f + bezierTwoPointControl.y) / 2f
    }

    private fun onePoints() {
        middleX = (moveX + cornerX) / 2f
        middleY = (moveY + cornerY) / 2f
        bezierOnePointControl.x = middleX - (cornerY - middleY) * (cornerY - middleY) / (cornerX - middleX)
        bezierOnePointControl.y = cornerY
        bezierTwoPointControl.x = cornerX
        if (0f == (cornerY - middleY)) {
            bezierTwoPointControl.y = middleY - (cornerX - middleX) * (cornerX - middleX) / 0.1f
        } else {
            bezierTwoPointControl.y = middleY - (cornerX - middleX) * (cornerX - middleX) / (cornerY - middleY)
        }

        bezierOnePointEnd.x = bezierOnePointControl.x - 1 / 2f/*注意*/ * (cornerX - bezierOnePointControl.x)
        bezierOnePointEnd.y = cornerY
    }

    /**
     * 求解直线P1P2和直线P3P4的交点坐标
     *
     * @param P1
     * @param P2
     * @param P3
     * @param P4
     * @return 交点坐标
     */
    private fun getCrossPoint(P1: PointF, P2: PointF, P3: PointF, P4: PointF): PointF {
        val CrossP = PointF()
        // 二元函数通式： y=ax+b
        val a1 = (P2.y - P1.y) / (P2.x - P1.x)
        val b1 = (P1.x * P2.y - P2.x * P1.y) / (P1.x - P2.x)

        val a2 = (P4.y - P3.y) / (P4.x - P3.x)
        val b2 = (P3.x * P4.y - P4.x * P3.y) / (P3.x - P4.x)
        CrossP.x = (b2 - b1) / (a1 - a2)
        CrossP.y = a1 * CrossP.x + b1
        return CrossP
    }

}