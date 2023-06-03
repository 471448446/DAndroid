package better.learn.view.custom.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.custom.ICustomView;

/**
 * -------------------
 * UNION：并集
 * INTERSECT:交集
 * XOR:并集排除他们的交集（并集排除交集）
 * REPLACE：替换
 * DIFFERENCE：第一个区域不同于第二个的区域 （第一个区域的差集）
 * REVERSE_DIFFERENCE:第二个区域不同于第一个的区域（第二个区域的差集）
 * -------------------
 * 集合，替换，不同（差集）
 * -------------
 * 从Canvas中“挖”取一块画布,可能是规则形状，也可以是不规则的Path
 * https://zhidao.baidu.com/question/116778634.html
 * com.example.android.apis.graphics.Clipping
 * http://www.cnblogs.com/tianzhijiexian/p/4300988.html
 * Created by better on 2017/9/25 15:06.
 */

public class ClipPathView extends View implements ICustomView {
    Paint mPaint = new Paint();
    Path mPath = new Path();

    public ClipPathView(Context context) {
        super(context);
        init();

    }

    public ClipPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClipPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    public void init() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        canvas.save();
        drawScene(canvas);
        canvas.restore();
        /*
        这里的REVERSE_DIFFERENCE 显示不出，因为第二个与第一个的不同区域是没有的，是空的，因为第二个区域被第一个包含了。
        即在第二个中找不到与第一个不同的区域
        */
        oneTest(0, canvas, Region.Op.UNION);
        oneTest(1, canvas, Region.Op.INTERSECT);
        oneTest(2, canvas, Region.Op.XOR);
        oneTest(3, canvas, Region.Op.REPLACE);
        oneTest(4, canvas, Region.Op.DIFFERENCE);
        oneTest(5, canvas, Region.Op.REVERSE_DIFFERENCE);

        twoTest(0, canvas, Region.Op.UNION);
        twoTest(1, canvas, Region.Op.INTERSECT);
        twoTest(2, canvas, Region.Op.XOR);
        twoTest(3, canvas, Region.Op.REPLACE);
        twoTest(4, canvas, Region.Op.DIFFERENCE);
        twoTest(5, canvas, Region.Op.REVERSE_DIFFERENCE);

        threeTest(0, canvas, Region.Op.UNION);
        threeTest(1, canvas, Region.Op.INTERSECT);
        threeTest(2, canvas, Region.Op.XOR);
        threeTest(3, canvas, Region.Op.REPLACE);
        threeTest(4, canvas, Region.Op.DIFFERENCE);
        threeTest(5, canvas, Region.Op.REVERSE_DIFFERENCE);

    }

    private void oneTest(int index, Canvas canvas, Region.Op ox) {
        canvas.save();
        canvas.translate(index * 110, 110);
        canvas.clipRect(10, 10, 90, 90);
        canvas.clipRect(30, 30, 70, 70, ox);
        drawScene(canvas, ox.name());
        canvas.restore();
    }

    private void twoTest(int index, Canvas canvas, Region.Op ox) {
        canvas.save();
        canvas.translate(index * 110, 220);
        canvas.clipRect(0, 0, 60, 60);
        canvas.clipRect(40, 40, 100, 100, ox);
        drawScene(canvas, ox.name());
        canvas.restore();
    }

    private void threeTest(int index, Canvas canvas, Region.Op ox) {
        mPath.reset();
        mPath.addCircle(50, 50, 50, Path.Direction.CW);
        canvas.save();
        canvas.translate(index * 110, 330);
        //前面没有clip相当于和一个空的Path裁剪
        canvas.clipPath(mPath, ox);
        drawScene(canvas, ox.name());
        canvas.restore();
    }

    private void drawScene(Canvas canvas) {
        drawScene(canvas, "Clipping");
    }

    private void drawScene(Canvas canvas, String s) {
        canvas.clipRect(0, 0, 100, 100);
        canvas.drawColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        canvas.drawLine(0, 0, 100, 100, mPaint);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(25, 75, 25, mPaint);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(11);
        canvas.drawText(s, (100 - mPaint.measureText(s)) / 2f, 15, mPaint);
    }
}
