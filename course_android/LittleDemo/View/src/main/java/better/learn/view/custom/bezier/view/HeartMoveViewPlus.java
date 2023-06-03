package better.learn.view.custom.bezier.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.R;


/**
 * 二|一
 * ----->
 * 三|四
 * 四段三阶贝萨尔
 * Created by better on 2017/9/14 16:46.
 */

public class HeartMoveViewPlus extends View {
    final int MSG=100;
    static final float C = 0.551915024494f;     // 一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置
    int radius;
    float difference = radius * C;        // 圆形的控制点与数据点的差值
    Paint paintCurve = new Paint();
    Path path = new Path();

    PointF top = new PointF(), right = new PointF(), bottom = new PointF(), left = new PointF();
    PointF top1 = new PointF(), top2 = new PointF(),
            right1 = new PointF(), right2 = new PointF(),
            bottom1 = new PointF(), bottom2 = new PointF(),
            left1 = new PointF(), left2 = new PointF();

    int duration =1000;
    int count =100;
    long speed = duration/count;
    int current;

    android.os.Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            current+=speed;
            if (current<=duration){
                top.y-=200/count;

                right2.y+=100/count;
                bottom1.y+=100/count;

                right1.x-=50/count;
                bottom2.x+=50/count;

                invalidate();
            }
            return false;
        }
    });

    public HeartMoveViewPlus(Context context) {
        super(context);
        init();
    }

    public HeartMoveViewPlus(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartMoveViewPlus(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintCurve.setStyle(Paint.Style.STROKE);
        paintCurve.setAntiAlias(true);
        paintCurve.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        paintCurve.setStrokeWidth(6);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 3;
        difference = radius * C;

        top.x = 0;
        top.y = radius;
        right.x = radius;
        right.y = 0;
        bottom.x = 0;
        bottom.y = -radius;
        left.x = -radius;
        left.y = 0;

        //一四三二
        top1.x = difference;
        top1.y = radius;
        top2.x = radius;
        top2.y = difference;
        right1.x = radius;
        right1.y = -difference;
        right2.x = difference;
        right2.y = -radius;
        bottom1.x = -difference;
        bottom1.y = -radius;
        bottom2.x = -radius;
        bottom2.y = -difference;
        left1.x = -radius;
        left1.y = difference;
        left2.x = -difference;
        left2.y = radius;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(1, -1);

        path.reset();
        path.moveTo(top.x, top.y);
        path.cubicTo(top1.x, top1.y, top2.x, top2.y, right.x, right.y);
        path.cubicTo(right1.x, right1.y, right2.x, right2.y, bottom.x, bottom.y);
        path.cubicTo(bottom1.x, bottom1.y, bottom2.x, bottom2.y, left.x, left.y);
        path.cubicTo(left1.x, left1.y, left2.x, left2.y, top.x, top.y);
        canvas.drawPath(path, paintCurve);

        if (current<duration) handler.sendEmptyMessageDelayed(MSG,speed);
    }
}
