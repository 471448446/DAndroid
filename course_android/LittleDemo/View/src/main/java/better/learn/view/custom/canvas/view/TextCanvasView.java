package better.learn.view.custom.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.R;
import better.learn.view.custom.ICustomView;

/**
 * 第一类只能指定文本基线位置(基线x默认在字符串左侧，基线y默认在字符串下方)。
 * public void drawText (String text, float x, float y, Paint paint)
 * public void drawText (String text, int start, int end, float x, float y, Paint paint)
 * public void drawText (CharSequence text, int start, int end, float x, float y, Paint paint)
 * public void drawText (char[] text, int index, int count, float x, float y, Paint paint)
 * 第二类可以分别指定每个文字的位置。
 * public void drawPosText (String text, float[] pos, Paint paint)
 * public void drawPosText (char[] text, int index, int count, float[] pos, Paint paint)
 * 第三类是指定一个路径，根据路径绘制文字。
 * public void drawTextOnPath (String text, Path path, float hOffset, float vOffset, Paint paint)
 * public void drawTextOnPath (char[] text, int index, int count, Path path, float hOffset, float vOffset, Paint paint)
 * Created by better on 2017/9/21 10:34.
 */

public class TextCanvasView extends View implements ICustomView {
    public static final String TEXT = "简单绘制文字";
    Paint mPaint = new Paint();

    float[] pos;
    Path mPath = new Path();

    public TextCanvasView(Context context) {
        super(context);
        init();
    }

    public TextCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    public void init() {
        pos = new float[TEXT.length() * 2];
        for (int i = 0; i < pos.length; i += 2) {
            pos[i] = 600 - 30 * i;
            pos[i + 1] = 40 * (i + 1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mPaint.setTextSize(40);
        canvas.drawText(TEXT, 100, 50, mPaint);
        canvas.drawText(TEXT, 1, 3, 100, 100, mPaint);
        canvas.drawText(TEXT.toCharArray(), 1, 2, 100, 150, mPaint);

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        canvas.drawPosText(TEXT, pos, mPaint);

        mPath.reset();
        mPath.moveTo(400, 400);
        mPath.quadTo(650, 450, 400, 700);
        mPaint.setColor(Color.GRAY);
        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        canvas.drawTextOnPath(TEXT, mPath, 20, 0, mPaint);
    }
}
