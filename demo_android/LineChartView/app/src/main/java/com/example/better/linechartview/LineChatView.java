package com.example.better.linechartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LineChatView extends View {
    private static final float ARROW_LENGTH = 10f;
    private static final double ARROW_RADIANS_AXIS = Math.toRadians(45);
    private static final int AXIS_COLOR = Color.GRAY;
    private static final int AXIS_WIDTH = 2;
    private static final int LINE_COLOR = 0xFF007DFF;
    private static final int LINE_WIDTH = 4;
    private static final int BottomSpace = 20;
    private static final int LeftSpace = 10;

    private Path linePath = new Path();
    private Paint linePaint = new Paint();
    private LineRender lineRender = new LineRender();
    private HashMap<Integer, String> xAxisTxt = new HashMap<>();
    private HashMap<Integer, String> yAxisTxt = new HashMap<>();
    private int axisTxtSize = 10, axisTxtColor = Color.BLACK;

    // 一段一段的线数据
    private List<LineInfo> pointsList = new ArrayList<>();

    public void setPointsList(List<LineInfo> pointsList) {
        this.pointsList = pointsList;
        invalidate();
    }

    public LineChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        xAxisTxt.put(0, "0");
        xAxisTxt.put(12, "12");
        xAxisTxt.put(24, "24");
        yAxisTxt.put(0, "0%");
        yAxisTxt.put(20, "20%");
        yAxisTxt.put(40, "40%");
        yAxisTxt.put(60, "60%");
        yAxisTxt.put(80, "80%");
        yAxisTxt.put(100, "100%");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale(1, -1);
        canvas.translate(0, -getHeight());
        drawAxis(canvas);
        drawLines(canvas);
        canvas.restore();
    }

    private void drawAxis(Canvas canvas) {
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(axisTxtColor);
        linePaint.setTextSize(ViewUtil.size2sp(axisTxtSize, getContext()));
        linePaint.setStrokeWidth(1);
        linePaint.setShader(null);

        // 坐标轴到View padding边界的距离，绘制文字以及箭头
        float axis2BottomSpace = getXTxtHeight() + BottomSpace;
        float axis2LeftSpace = getYTxtWidth() + LeftSpace;
        float arrowInAxis = (float) (Math.cos(ARROW_RADIANS_AXIS) * ARROW_LENGTH);
        float arrow2Axis = (float) (Math.sin(ARROW_RADIANS_AXIS) * ARROW_LENGTH);
        canvas.translate(getPaddingLeft() + axis2LeftSpace, getPaddingBottom() + axis2BottomSpace);
        //x、y 长,将点映射到坐标上
        float xL = getWidth() - getPaddingLeft() - getPaddingRight() - axis2LeftSpace - arrowInAxis;
        float yL = getHeight() - getPaddingBottom() - getPaddingTop() - axis2BottomSpace - arrowInAxis;

        canvas.save();
        canvas.scale(1, -1);
        int x, y;
        String mark;
        Iterator<Map.Entry<Integer, String>> iterator = xAxisTxt.entrySet().iterator();
        Rect bound = new Rect();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            mark = entry.getValue();
            linePaint.getTextBounds(mark, 0, mark.length(), bound);
            y = BottomSpace + bound.height();
            switch (entry.getKey()) {
                case 0:
                    x = 0;
                    break;
                case 24:
                    x = (int) (xL - bound.width());
                    break;
                default:
                    x = (int) (xL / 24 * entry.getKey() - bound.width() / 2);
                    break;
            }
            canvas.drawText(mark, x, y, linePaint);
        }
        iterator = yAxisTxt.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            mark = entry.getValue();
            linePaint.getTextBounds(mark, 0, mark.length(), bound);
            x = -LeftSpace - bound.width();
            switch (entry.getKey()) {
                case 0:
                    y = bound.height() - BottomSpace;//？
                    break;
                case 100:
                    y = (int) (yL - bound.height());
                    break;
                default:
                    y = (int) (yL / 100 * entry.getKey() - bound.height() / 2);
                    break;
            }
            canvas.drawText(mark, x, -y, linePaint);
        }
        canvas.restore();

        // arrow
        linePath.moveTo(-arrow2Axis, yL);
        linePath.lineTo(0, yL + arrowInAxis);
        linePath.lineTo(arrow2Axis, yL);
        linePath.moveTo(xL, arrow2Axis);
        linePath.lineTo(xL + arrowInAxis, 0);
        linePath.lineTo(xL, -arrow2Axis);
        // xy
        linePath.moveTo(0, yL);
        linePath.lineTo(0, 0);
        linePath.lineTo(xL, 0);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(AXIS_COLOR);
        linePaint.setStrokeWidth(AXIS_WIDTH);
        canvas.drawPath(linePath, linePaint);

        lineRender.xAxisLength = xL;
        lineRender.yAxisLength = yL;
    }

    private int getXTxtHeight() {
        Rect bound = new Rect();
        String max = findMax(xAxisTxt);
        linePaint.getTextBounds(max, 0, max.length(), bound);
        return bound.height();
    }

    private int getYTxtWidth() {
        Rect xBound = new Rect();
        String max = findMax(yAxisTxt);
        linePaint.getTextBounds(max, 0, max.length(), xBound);
        return xBound.width();
    }

    private String findMax(HashMap<Integer, String> map) {
        String max = "";
        Iterator<Map.Entry<Integer, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();

            if (entry.getValue().length() > max.length()) {
                max = entry.getValue();
            }
        }
        return max;
    }

    private void drawLines(Canvas canvas) {
        if (pointsList.isEmpty()) {
            return;
        }
        for (LineInfo lineInfo : pointsList) {
            lineRender.drawLine(canvas, linePaint, linePath, lineInfo);
        }
    }

    private static class LineRender {
        float xAxisLength, yAxisLength;

        void drawLine(Canvas canvas, Paint linePaint, Path linePath, LineInfo lineInfo) {
            if (lineInfo.points.isEmpty()) {
                return;
            }
            List<PointF> points = lineInfo.points;

            linePaint.setColor(lineInfo.lineColor);
            linePaint.setStrokeWidth(lineInfo.lineW);
            if (lineInfo.isSupportGradientBg()) {
                linePaint.setStyle(Paint.Style.FILL);
                linePaint.setShader(new LinearGradient(
                        getTransformX(points.get(0).x), getTransformY(points.get(0).y),
                        getTransformX(points.get(points.size() - 1).x), 0,
                        lineInfo.lineGradientSColor, lineInfo.lineGradientEColor,
                        Shader.TileMode.CLAMP));
            } else {
                linePaint.setStyle(Paint.Style.STROKE);
                linePaint.setShader(null);
            }

            linePath.reset();
            for (int i = 0; i < points.size(); i++) {
                if (0 == i) {
                    linePath.moveTo(getTransformX(points.get(0).x), getTransformY(points.get(0).y));
                    continue;
                }
                if (LineInfo.LINE == lineInfo.curve) {
                    linePath.lineTo(getTransformX(points.get(i).x), getTransformY(points.get(i).y));
                } else if (LineInfo.CURVE == lineInfo.curve) {
                    PointF[] ab = getControlPoint(points, i - 1);
                    linePath.cubicTo(
                            getTransformX(ab[0].x),
                            getTransformY(ab[0].y),
                            getTransformX(ab[1].x),
                            getTransformY(ab[1].y),
                            getTransformX(points.get(i).x),
                            getTransformY(points.get(i).y)
                    );
                }
            }
            if (lineInfo.isSupportGradientBg()) {
                linePath.lineTo(getTransformX(points.get(points.size() - 1).x), 0);
                linePath.lineTo(getTransformX(points.get(0).x), 0);
                linePath.close();
            }

            canvas.drawPath(linePath, linePaint);
        }

        private PointF[] getControlPoint(List<PointF> ps, int i) {
            return getControlPoint(ps, i, 0.25f, 0.25f);
        }

        private PointF[] getControlPoint(List<PointF> ps, int i, float a, float b) {
            //http://www.zheng-hang.com/?id=43
            PointF A = new PointF(), B = new PointF();
            //处理两种极端情形
            if (i < 1) {
                A.x = ps.get(0).x + (ps.get(1).x - ps.get(0).x) * a;
                A.y = ps.get(0).y + (ps.get(1).y - ps.get(0).y) * a;
            } else {
                A.x = ps.get(i).x + (ps.get(i + 1).x - ps.get(i - 1).x) * a;
                A.y = ps.get(i).y + (ps.get(i + 1).y - ps.get(i - 1).y) * a;
            }
            if (i > ps.size() - 3) {
                int last = ps.size() - 1;
                B.x = ps.get(last).x - (ps.get(last).x - ps.get(last - 1).x) * b;
                B.y = ps.get(last).y - (ps.get(last).y - ps.get(last - 1).y) * b;
            } else {
                B.x = ps.get(i + 1).x - (ps.get(i + 2).x - ps.get(i).x) * b;
                B.y = ps.get(i + 1).y - (ps.get(i + 2).y - ps.get(i).y) * b;
            }
            return new PointF[]{A, B};
        }

        private float getTransformX(float x) {
            return x / 24 * xAxisLength;
        }

        private float getTransformY(float y) {
            return y / 100 * yAxisLength;
        }

    }

    public static class LineInfo {
        public static int LINE = 0;
        public static int CURVE = LINE + 1;
        private int lineW = LINE_WIDTH;
        private int lineColor = LINE_COLOR;
        private int lineGradientSColor;
        private int lineGradientEColor;
        private int curve = LINE;
        private List<PointF> points = new ArrayList<>();

        public LineInfo setPoints(List<PointF> points) {
            this.points = points;
            return this;
        }

        public LineInfo setLineW(int lineW) {
            this.lineW = lineW;
            return this;
        }

        public LineInfo setLineColor(int lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        public LineInfo setCurve(int curve) {
            this.curve = curve;
            return this;
        }

        public LineInfo setLineGradientSColor(int color) {
            this.lineGradientSColor = color;
            return this;
        }

        public LineInfo setLineGradientEColor(int color) {
            this.lineGradientEColor = color;
            return this;
        }

        public boolean isSupportGradientBg() {
            return 0 != lineGradientSColor && 0 != lineGradientEColor;
        }
    }
}
