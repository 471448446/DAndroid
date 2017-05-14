package better.lib.circleimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 找到bitmap，
 * 设置要画的shader，配置给paint
 * 画
 * 某天突然看到 http://blog.chengyunfeng.com/?p=457。于是试一试
 * Created by better on 2017/5/7 13:38.
 */

public class CircleImageView extends AppCompatImageView {
    private final String TAG = "CircleImageView";
    private Paint mPaint, mBorderPaint;
    private BitmapShader mShader;
    private Matrix mBitmapMatrix;
    private float mRadius;

    private boolean autoMatrix;
    private float borderWidth;
    private int borderColor;

    public CircleImageView(Context context) {
        super(context);
        init(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initCustomAttrs(context, attrs);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        autoMatrix = typedArray.getBoolean(R.styleable.CircleImageView_autoScale, autoMatrix);
        borderWidth = typedArray.getDimension(R.styleable.CircleImageView_borderWith, borderWidth);
        borderColor = typedArray.getColor(R.styleable.CircleImageView_borderColor, borderColor);
        typedArray.recycle();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initCustomAttrs(context, attrs);
    }

    private void init(Context context) {

        autoMatrix = true;
        borderColor = Color.parseColor("#ffffff");
        borderWidth = 0f;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);

        mBitmapMatrix = new Matrix();

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(TAG, "onSaveInstanceState: ");
        return super.onSaveInstanceState();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int min = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = min / 2;
        setMeasuredDimension(min, min);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (null == getDrawable()) return;
        drawBorder(canvas);
        drawCircleImageImage(canvas);

    }

    private void drawCircleImageImage(Canvas canvas) {
//        canvas.drawRoundRect(mSharderRect, mRadius, mRadius, mPaint);
        setShader();
        if (null != mShader) {
            canvas.drawCircle(mRadius, mRadius, Math.abs(mRadius - borderWidth), mPaint);
        }
    }

    private void drawBorder(Canvas canvas) {
        mBorderPaint.setColor(borderColor);
        canvas.drawCircle(mRadius, mRadius, mRadius, mBorderPaint);
    }

    private void setShader() {
        Bitmap bitmap = null;
        Drawable drawable = getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Log.d("", "找到bitmap");
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        if (null != bitmap) {
            mShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mPaint.setShader(mShader);

            if (autoMatrix) {
                /**
                 * a把bitmap按照控件的宽高缩放
                * Create By better on 2017/5/14 15:46.
                 */
                float scale = mRadius * 2 / Math.min(bitmap.getWidth(), bitmap.getHeight());
                mBitmapMatrix.setScale(scale, scale);
                mShader.setLocalMatrix(mBitmapMatrix);
            }
        } else {
            mShader = null;
        }
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    public void setAutoMatrix(boolean autoMatrix) {
        this.autoMatrix = autoMatrix;
        invalidate();
    }
}
