package com.example.better.linechartview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatteryLineView extends View {

    private static final int AXIS_Y_COLOR = Color.parseColor("#1a0056ff");
    private static final int AXIS_X_COLOR = Color.parseColor("#BED1FF");
    private static final int AXIS_TXT_COLOR = Color.parseColor("#999999");
    private static final int AXIS_TXT_SIZE = 11;
    private static final float AXIS_Y_TXT_MARGIN_RIGHT = 10, AXIS_X_TXT_MARGIN_TOP = 30;
    private static final int LINE_COLOR = Color.parseColor("#0056ff");
    private static final int LINE_WIDTH = 3;
    private static final int X_DIVIDE_COUNT = 12, Y_DIVIDE_COUNT = 10;
    private static final float Y_PROPORTION_X = 30f / 50f;
    private static final float GUILD_TXT_SIZE = 14;
    private static final float GUILD_TXT_MARGIN = 15;
    private static final float GUILD_BG_BOTTOM_MARGIN = 25;
    private static final float GUILD_BG_RADIUS = 5;
    private static final float GUILD_CIRCLE_RADIUS = 10;
    private static final int GUILD_BG_COLOR = LINE_COLOR;
    private static final int GUILD_TXT_COLOR = Color.WHITE;
    // 绘制相关
    private Path linePath = new Path();
    private Paint linePaint = new Paint();
    private RectF yRect = new RectF(), xRect = new RectF(), lineRect = new RectF();
    private float perRectW;
    private Rect bound = new Rect(), guildTxtBound = new Rect();
    // 坐标
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, String> xAxisTxt = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, String> yAxisTxt = new HashMap<>();
    // 一段一段的线数据
    private List<LineInfo> pointsList = new ArrayList<>();
    private LineRender lineRender;
    // 辅助线
    private int guideLineColor;
    private PointF touchPoint = new PointF(), intersectPointPC = new PointF(), xPointPC = new PointF();
    private RectF guildBgRect = new RectF();
    private String guildTxt = "电量：%s%%";
    private int touchSlop;

    public BatteryLineView setPointsList(List<LineInfo> pointsList) {
        this.pointsList = pointsList;
        touchPoint.x = 0;
        touchPoint.y = 0;
        invalidate();
        return this;
    }

    public BatteryLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);
        linePaint.setTextSize(ViewUtil.size2sp(AXIS_TXT_SIZE, getContext()));
        lineRender = new LineRender();

        yRect.left = getPaddingLeft();
        yRect.top = getPaddingTop();
        yRect.right = yRect.left + AXIS_Y_TXT_MARGIN_RIGHT + linePaint.measureText("100%");
        // y Bottom

        xAxisTxt.put(0, "0");
        xAxisTxt.put(2, "2");
        xAxisTxt.put(4, "4");
        xAxisTxt.put(6, "6");
        xAxisTxt.put(8, "8");
        xAxisTxt.put(10, "10");
        xAxisTxt.put(12, "12");
        xAxisTxt.put(14, "14");
        xAxisTxt.put(16, "16");
        xAxisTxt.put(18, "18");
        xAxisTxt.put(20, "20");
        xAxisTxt.put(22, "22");
        xAxisTxt.put(24, "24");

        yAxisTxt.put(0, "0%");
        yAxisTxt.put(20, "20%");
        yAxisTxt.put(40, "40%");
        yAxisTxt.put(60, "60%");
        yAxisTxt.put(80, "80%");
        yAxisTxt.put(100, "100%");

        String txt = String.format(guildTxt, String.valueOf(100));
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.RED);
        linePaint.setTextSize(ViewUtil.size2sp(GUILD_TXT_SIZE, getContext()));
        linePaint.getTextBounds(txt, 0, txt.length(), guildTxtBound);

    }

    private float downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null == pointsList || pointsList.isEmpty()) {
            return false;
        }
        touchPoint.x = event.getX();
        touchPoint.y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                if (calculatePointOk(0)) {
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - downX) > touchSlop ||
                        Math.abs(event.getY() - downY) > touchSlop) {
                    if (calculatePointOk((int) (event.getX() - downX))) {
                        //滑动电量变化折线时，整体界面也会被滑动，建议优化
                        getParent().requestDisallowInterceptTouchEvent(true);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            widthSize = (int) (300 * getResources().getDisplayMetrics().density);
        }
        perRectW = (widthSize - getPaddingLeft() - getPaddingRight() - yRect.width()) * 1.0f / X_DIVIDE_COUNT;

        yRect.bottom = yRect.top + perRectW * Y_PROPORTION_X * Y_DIVIDE_COUNT;

        xRect.left = yRect.right;
        xRect.top = yRect.bottom;
        xRect.right = widthSize - getPaddingRight();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);
        linePaint.setTextSize(ViewUtil.size2sp(AXIS_TXT_SIZE, getContext()));
        linePaint.getTextBounds("22", 0, 2, bound);
        xRect.bottom = xRect.top + AXIS_X_TXT_MARGIN_TOP + bound.height();

        lineRect.left = xRect.left;
        lineRect.top = yRect.top;
        lineRect.right = xRect.right;
        lineRect.bottom = yRect.bottom;

        int heightSize = (int) (getPaddingTop() + getPaddingBottom() + yRect.height() + xRect.height());
        lineRender.lineRect = lineRect;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawXYAxis(canvas);
        if (isInEditMode()) {
            return;
        }
        drawLine(canvas);
        drawGuildInfo(canvas);
    }

    private boolean calculatePointOk(int direct) {
        return lineRect.contains(touchPoint.x, touchPoint.y) && calculateIntersectPoint(direct);
    }

    private boolean calculateIntersectPoint(int direct) {
        PointF[] pointFS = findInPointList(direct);
        if (null == pointFS) {
            return false;
        }
        if (pointFS.length == 1) {
            intersectPointPC.x = lineRender.transformX2Physical(pointFS[0].x);
            intersectPointPC.y = lineRender.transformY2Physical(pointFS[0].y);
            return true;
        }
        //touchPoint
        xPointPC.x = touchPoint.x;
        xPointPC.y = lineRender.transformY2Physical(0);
        PointF A = new PointF();
        PointF B = new PointF();
        A.x = lineRender.transformX2Physical(pointFS[0].x);
        A.y = lineRender.transformY2Physical(pointFS[0].y);
        B.x = lineRender.transformX2Physical(pointFS[1].x);
        B.y = lineRender.transformY2Physical(pointFS[1].y);

        float a0 = xPointPC.y - touchPoint.y;
        float b0 = touchPoint.x - xPointPC.x;
        float c0 = xPointPC.x * touchPoint.y - touchPoint.x * xPointPC.y;

        float a1 = B.y - A.y;
        float b1 = A.x - B.x;
        float c1 = B.x * A.y - A.x * B.y;

        float D = a0 * b1 - a1 * b0;
        if (D == 0) {
            intersectPointPC.x = B.x;
            intersectPointPC.y = B.y;
        } else {
            intersectPointPC.x = (b0 * c1 - b1 * c0) / D;
            intersectPointPC.y = (a1 * c0 - a0 * c1) / D;
        }
        return true;
    }

    /**
     * @param direct <0 左滑，>0右滑，0不滑动
     */
    private PointF[] findInPointList(int direct) {
        float x = lineRender.transformX2Real(touchPoint.x);
        /*
        if (pointsList.get(0).points.get(0).x > x) {
            return new PointF[]{pointsList.get(0).points.get(0)};
        } else if (pointsList.get(pointsList.size() - 1).points.get(pointsList.get(pointsList.size() - 1).points.size() - 1).x < x) {
            return new PointF[]{pointsList.get(pointsList.size() - 1).points.get(pointsList.get(pointsList.size() - 1).points.size() - 1)};
        }*/
        PointF A = null, B = null;
        // 找两个点
        LineInfo lineInfo;
        for (int i = 0, l = pointsList.size(); i < l; i++) {
            lineInfo = pointsList.get(i);
            if (lineInfo.contain(x)) {
                for (int j = 0; j < lineInfo.points.size(); j++) {
                    //点上
                    if (lineInfo.points.get(j).x == x) {
                        guideLineColor = lineInfo.lineColor;
                        return new PointF[]{lineInfo.points.get(j)};
                    }
                    if (j == lineInfo.points.size() - 1) {
                        continue;
                    }
                    //直线上
                    if (lineInfo.points.get(j).x < x && lineInfo.points.get(j + 1).x > x) {
                        A = lineInfo.points.get(j);
                        B = lineInfo.points.get(j + 1);
                        guideLineColor = lineInfo.lineColor;
                    }
                }
                break;
            }
        }
        // 两个区间之间
        if (null == A && direct != 0 &&
                pointsList.get(0).points.get(0).x < x &&
                pointsList.get(pointsList.size() - 1).points.get(pointsList.get(pointsList.size() - 1).points.size() - 1).x > x) {
            if (direct > 0) {
                for (int i = 0; i < pointsList.size(); i++) {
                    if (i < pointsList.size() - 1 &&
                            pointsList.get(i + 1).points.get(0).x >= x) {
                        A = pointsList.get(i + 1).points.get(0);
                        guideLineColor = pointsList.get(i + 1).lineColor;
                        // bug 无法拖动滑动查看其它点坐标点信息，优化
                        break;
                    }
                }
            } else {
                for (int i = pointsList.size() - 1; i >= 0; i--) {
                    if (i > 0 &&
                            pointsList.get(i - 1).points.get(pointsList.get(i - 1).points.size() - 1).x <= x) {
                        A = pointsList.get(i - 1).points.get(pointsList.get(i - 1).points.size() - 1);
                        guideLineColor = pointsList.get(i - 1).lineColor;
                        break;
                    }
                }
            }
        }
        if (null != A) {
            if (null != B) {
                return new PointF[]{A, B};
            }
            return new PointF[]{A};
        }
        return null;
    }

    private void drawGuildInfo(Canvas canvas) {
        if (null == pointsList || pointsList.isEmpty()) {
            return;
        }
        if (touchPoint.x == 0 &&
                touchPoint.y == 0) {
            //init
            PointF last = pointsList.get(pointsList.size() - 1).points.get(pointsList.get(pointsList.size() - 1).points.size() - 1);
            intersectPointPC.x = lineRender.transformX2Physical(last.x);
            intersectPointPC.y = lineRender.transformY2Physical(last.y);
            guideLineColor = pointsList.get(pointsList.size() - 1).lineColor;
        }

        linePaint.setStrokeWidth(LINE_WIDTH);
        linePaint.setShader(null);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(guideLineColor);
        canvas.drawLine(
                intersectPointPC.x,
                intersectPointPC.y,
                intersectPointPC.x,
                lineRender.transformY2Physical(0), linePaint);

        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.WHITE);
        canvas.drawCircle(intersectPointPC.x, intersectPointPC.y, GUILD_CIRCLE_RADIUS, linePaint);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(guideLineColor);
        linePaint.setStrokeWidth(LINE_WIDTH);
        canvas.drawCircle(intersectPointPC.x, intersectPointPC.y, GUILD_CIRCLE_RADIUS, linePaint);

        guildBgRect.left = intersectPointPC.x - guildTxtBound.width() / 2f - GUILD_TXT_MARGIN;
        guildBgRect.top = intersectPointPC.y - GUILD_BG_BOTTOM_MARGIN - (GUILD_TXT_MARGIN * 2 + guildTxtBound.height());
        guildBgRect.right = guildBgRect.left + GUILD_TXT_MARGIN * 2 + guildTxtBound.width();
        guildBgRect.bottom = intersectPointPC.y - GUILD_BG_BOTTOM_MARGIN;
        if (guildBgRect.left < lineRender.transformX2Physical(0)) {
            guildBgRect.left = lineRender.transformX2Physical(0);
            guildBgRect.right = guildBgRect.left + GUILD_TXT_MARGIN * 2 + guildTxtBound.width();
        } else if (guildBgRect.right > lineRender.transformX2Physical(24)) {
            guildBgRect.right = lineRender.transformX2Physical(24);
            guildBgRect.left = guildBgRect.right - GUILD_TXT_MARGIN * 2 - guildTxtBound.width();
        }
        linePath.reset();
        RectF lB = new RectF(
                guildBgRect.left,
                guildBgRect.bottom - GUILD_BG_RADIUS * 2,
                guildBgRect.left + GUILD_BG_RADIUS * 2,
                guildBgRect.bottom);
        linePath.moveTo(lB.left, lB.top);
        linePath.addArc(lB, 90f, 90f);
        RectF lT = new RectF(
                lB.left,
                guildBgRect.top,
                lB.right,
                guildBgRect.top + GUILD_BG_RADIUS * 2
        );
        linePath.lineTo(lT.left, lT.top + GUILD_BG_RADIUS);
        linePath.arcTo(lT, 180, 90f);

        RectF rT = new RectF(
                guildBgRect.right - GUILD_BG_RADIUS * 2,
                lT.top,
                guildBgRect.right,
                lT.bottom
        );
        linePath.lineTo(rT.left + GUILD_BG_RADIUS, rT.top);
        linePath.arcTo(rT, 270, 90f);
        RectF rB = new RectF(
                rT.left,
                lB.top,
                rT.right,
                lB.bottom
        );
        linePath.lineTo(rB.right, rB.top + GUILD_BG_RADIUS);
        linePath.arcTo(rB, 0, 90f);
        linePath.close();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(GUILD_BG_COLOR);
        canvas.drawPath(linePath, linePaint);

        linePaint.setStrokeWidth(2);
        linePaint.setColor(GUILD_TXT_COLOR);
        linePaint.setTextSize(ViewUtil.size2sp(GUILD_TXT_SIZE, getContext()));
        String txt = String.format(guildTxt, String.valueOf(
                new BigDecimal(lineRender.transformY2Real(intersectPointPC.y)).setScale(0, BigDecimal.ROUND_HALF_UP)));
        Rect bound = new Rect();
        linePaint.getTextBounds(txt, 0, txt.length(), bound);
        canvas.drawText(
                String.format(guildTxt, String.valueOf(
                        new BigDecimal(lineRender.transformY2Real(intersectPointPC.y))
                                .setScale(0, BigDecimal.ROUND_HALF_UP))),
                (guildBgRect.left + guildBgRect.right) / 2f - bound.width() / 2f,
                (guildBgRect.top + guildBgRect.bottom) / 2f + bound.height() * 1.8f / 5f,
                linePaint);
    }

    private void drawLine(Canvas canvas) {
        if (pointsList.isEmpty()) {
            return;
        }
        for (BatteryLineView.LineInfo lineInfo : pointsList) {
            lineRender.drawLine(canvas, linePaint, linePath, lineInfo);
        }
    }

    private void drawXYAxis(Canvas canvas) {
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);
        linePaint.setShader(null);
        linePaint.setColor(AXIS_Y_COLOR);

        float x, y, x1, y1;
        for (int i = 0; i <= X_DIVIDE_COUNT; i++) {
            x = yRect.right + i * perRectW;
            y = yRect.top;
            x1 = x;
            y1 = yRect.bottom;
            canvas.drawLine(x, y, x1, y1, linePaint);
        }
        for (int i = 0; i < Y_DIVIDE_COUNT; i++) {
            x = xRect.left;
            y = yRect.top + perRectW * Y_PROPORTION_X * i;
            x1 = xRect.right;
            y1 = y;
            canvas.drawLine(x, y, x1, y1, linePaint);
        }
        linePaint.setColor(AXIS_X_COLOR);
        canvas.drawLine(xRect.left, xRect.top, xRect.right, xRect.top, linePaint);

        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(AXIS_TXT_COLOR);
        linePaint.setStrokeWidth(1);
        linePaint.setTextSize(ViewUtil.size2sp(AXIS_TXT_SIZE, getContext()));
        Rect bound = new Rect();
        String mark;
        for (Map.Entry<Integer, String> entry : yAxisTxt.entrySet()) {
            mark = entry.getValue();
            linePaint.getTextBounds(mark, 0, mark.length(), bound);
            x = yRect.right - AXIS_Y_TXT_MARGIN_RIGHT - bound.width();
            y = yRect.bottom - entry.getKey() * yRect.height() / 100.f;
            canvas.drawText(mark, x, y, linePaint);
        }
        for (Map.Entry<Integer, String> entry : xAxisTxt.entrySet()) {
            mark = entry.getValue();
            linePaint.getTextBounds(mark, 0, mark.length(), bound);
            switch (entry.getKey()) {
                case 0:
                    x = xRect.left;
                    break;
                default:
                    x = xRect.left + entry.getKey() * xRect.width() / 24f - bound.width() / 2f;
                    break;
            }
            y = xRect.top + AXIS_X_TXT_MARGIN_TOP + bound.height() / 2f;
            canvas.drawText(mark, x, y, linePaint);
        }
    }

    private static class LineRender {
        RectF lineRect;

        void drawLine(Canvas canvas, Paint linePaint, Path linePath, BatteryLineView.LineInfo lineInfo) {
            if (lineInfo.points.isEmpty()) {
                return;
            }
            List<PointF> points = lineInfo.points;

            linePaint.setColor(lineInfo.lineColor);
            linePaint.setStrokeWidth(lineInfo.lineW);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setShader(null);
            linePath.reset();
            linePath.moveTo(transformX2Physical(points.get(0).x), transformY2Physical(points.get(0).y));
            if (points.size() == 1) {
                canvas.drawPoint(transformX2Physical(points.get(0).x), transformY2Physical(points.get(0).y), linePaint);
            } else {
                for (int i = 0; i < points.size(); i++) {
                    if (0 == i) {
                        continue;
                    }
                    if (LineInfo.LINE == lineInfo.curve) {
                        linePath.lineTo(transformX2Physical(points.get(i).x), transformY2Physical(points.get(i).y));
                    } else if (LineInfo.CURVE == lineInfo.curve) {
                        PointF[] ab = getControlPoint(points, i - 1);
                        linePath.cubicTo(
                                transformX2Physical(ab[0].x),
                                transformY2Physical(ab[0].y),
                                transformX2Physical(ab[1].x),
                                transformY2Physical(ab[1].y),
                                transformX2Physical(points.get(i).x),
                                transformY2Physical(points.get(i).y)
                        );
                    }
                }
                canvas.drawPath(linePath, linePaint);
            }
            if (lineInfo.isSupportGradientBg()) {
                linePaint.setStyle(Paint.Style.FILL);
                linePaint.setShader(
                        new LinearGradient(
                                transformX2Physical(points.get(0).x), transformY2Physical(points.get(0).y),
                                transformX2Physical(points.get(0).x), transformY2Physical(0),
                                lineInfo.lineGradientSColor, lineInfo.lineGradientEColor,
                                Shader.TileMode.CLAMP));
                linePath.lineTo(transformX2Physical(points.get(points.size() - 1).x), transformY2Physical(0));
                linePath.lineTo(transformX2Physical(points.get(0).x), transformY2Physical(0));
                linePath.close();
                canvas.drawPath(linePath, linePaint);
            }
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

        private float transformX2Physical(float x) {

            return lineRect.left + x * lineRect.width() / 24;
        }

        private float transformX2Real(float x) {
            return (x - lineRect.left) * 24 / lineRect.width();
        }

        private float transformY2Physical(float y) {
            return lineRect.bottom - y * lineRect.height() / 100;
        }

        private float transformY2Real(float y) {
            return -(y - lineRect.bottom) * 100 / lineRect.height();
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
        // not empty
        private List<PointF> points = new ArrayList<>();

        @Override
        public String toString() {
            return "LineInfo{" +
                    "points=" + points +
                    '}';
        }

        public boolean contain(float x) {
            return null != points && 2 <= points.size() &&
                    points.get(0).x <= x && points.get(points.size() - 1).x >= x;
        }

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
