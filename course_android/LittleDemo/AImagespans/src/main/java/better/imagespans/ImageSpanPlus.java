package better.imagespans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import android.text.style.ImageSpan;
import android.util.Log;

/**
 * Created by better on 2017/6/26 22:06.
 */

public class ImageSpanPlus extends ImageSpan {
    /* 与文字中间对齐  --better 2017/6/27 16:55. */
    public static final int ALIGN_CENTER = ALIGN_BASELINE + 1;
    /* 与文字顶部对齐  --better 2017/6/27 16:56. */
    public static final int ALIGN_TOP_ASCENT = ALIGN_CENTER + 1;

    public ImageSpanPlus(Context context, @DrawableRes int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    public ImageSpanPlus(Context context, @DrawableRes int resourceId) {
        super(context, resourceId);
    }

    /**
     * 返回span的宽，同时设置Paint.FontMetricsInt的高度。这样就限定了当前span的大小
     * Create By better on 2017/6/27 17:21.
     */
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        log(text + "," + start + ",end" + end);
        if (mVerticalAlignment == ALIGN_BASELINE || mVerticalAlignment == ALIGN_BOTTOM) {
            return super.getSize(paint, text, start, end, fm);
        }

        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }

    /**
     * 确定span的位置
     * Create By better on 2017/6/27 17:22.
     */
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        if (mVerticalAlignment == ALIGN_BASELINE ||
                mVerticalAlignment == ALIGN_BOTTOM) {
            super.draw(canvas, text, start, end, x, top, y, bottom, paint);
            return;
        }
        if (mVerticalAlignment == ALIGN_CENTER) {
            alignCenter(canvas, start, end, x, top, y, bottom, paint);
            return;
        }
        if (mVerticalAlignment == ALIGN_TOP_ASCENT) {
            alignTop(canvas, start, end, x, top, y, bottom, paint);
            return;
        }
    }

    private void alignTop(Canvas canvas, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        Paint.FontMetricsInt metricsInt = paint.getFontMetricsInt();
        int transY = y + metricsInt.ascent;

        canvas.save();
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }

    private void alignCenter(Canvas canvas, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        Paint.FontMetricsInt metricsInt = paint.getFontMetricsInt();
        int transY = (y + metricsInt.descent + y + metricsInt.ascent) / 2 - drawable.getBounds().bottom / 2;

        canvas.save();
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }

    private void log(String msg) {
        Log.d("ImageSpanPlus", msg);
    }
}
