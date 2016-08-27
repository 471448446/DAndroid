package better.dtouchlistener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by better on 16/8/24.
 */
public class GradientView extends View {
    Paint mPaint = null;
    /* 线性渐变渲染 */
    Shader mLinearGradient = null;
    String msg="你哈啊";

    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 创建LinearGradient并设置渐变的颜色数组 说明一下这几天参数
         * 第一个 起始的x坐标
         * 第二个 起始的y坐标
         * 第三个 结束的x坐标
         * 第四个 结束的y坐标
         * 第五个 颜色数组
         * 第六个 这个也是一个数组用来指定颜色数组的相对位置 如果为null 就沿坡度线均匀分布
         * 第七个 渲染模式
         * */
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setShader(mLinearGradient);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        int heightY=getMeasuredHeight()/2;
        mLinearGradient = new LinearGradient(0, heightY, 0, heightY-20, ContextCompat.getColor(getContext(),R.color.gray),
                ContextCompat.getColor(getContext(),R.color.white), Shader.TileMode.CLAMP);
        canvas.drawText(msg, 0, heightY, mPaint);
        canvas.drawText(msg+msg, 0, heightY+50, mPaint);
    }

}
