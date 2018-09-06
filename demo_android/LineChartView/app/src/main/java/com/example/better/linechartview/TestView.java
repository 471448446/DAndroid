package com.example.better.linechartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TestView extends View {
    Path path = new Path();
    Paint paint = new Paint();

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        canvas.drawRect(new RectF(100, 100, 200, 200), paint);

        path.moveTo(100, 100);
        canvas.drawArc(new RectF(100, 100, 200, 200), 0, 360, true, paint);

        paint.setColor(Color.GREEN);
        canvas.drawPath(path, paint);
    }
}
