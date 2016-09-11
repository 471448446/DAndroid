package better.slidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by better on 16/9/10.
 * 口
 * 口     口
 * --  ---口---
 * 口     口
 * 口
 *
 */
public class SlideBarView extends View implements ICustomView {
    List<String> mABC;
    private Paint mPaint,mPaintR;
    private int mSize;
    private int mColor;

    private int mHeight;
    private int mWidth;
    private int mADBPadding/*文字上下padding*/;

    private int mCellWidth,cellHeight,/*cell 起始坐标*/startX, startY;
    private OnScrollListener listener;

    public SlideBarView(Context context) {
        super(context);
        initDefaultAttr(context);
        initView(context);
    }

    public SlideBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultAttr(context);
        initCustomAttr(context, attrs);
        initView(context);
    }

    public SlideBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttr(context);
        initCustomAttr(context, attrs);
        initView(context);
    }
    @Override
    public void initDefaultAttr(Context context) {
        l("initDefaultAttr");
        mPaint=new Paint();
        mSize=ViewUtil.sp2px(context,16);
        mColor= ContextCompat.getColor(context,R.color.black);
        mPaint.setTextSize(mSize);
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mADBPadding=ViewUtil.dp2px(context,4);
        mPaintR=new Paint();
        mPaintR.setColor(ContextCompat.getColor(context,R.color.colorPrimary));
    }

    @Override
    public void initCustomAttr(Context context, AttributeSet attrs) {
    }

    @Override
    public void initCustomAttrDetail(int attr, TypedArray typedArray) {

    }

    @Override
    public void initView(Context context) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth =MeasureSpec.getMode(widthMeasureSpec)==MeasureSpec.AT_MOST?ViewUtil.dp2px(getContext(),50):MeasureSpec.getSize(widthMeasureSpec);
        mHeight =MeasureSpec.getSize(heightMeasureSpec);
        l("measure H="+mHeight);
        setMeasuredDimension(MeasureSpec.getSize(mWidth),MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null==mABC) return;
        setPram();
        for (int i=0,l=mABC.size();i<l;i++){
            int txtY= (int) (startY+cellHeight*i+cellHeight/2-(mPaint.ascent() + mPaint.descent()) / 2);
//            canvas.drawRect((mWidth-mCellWidth)/2,startY+cellHeight*i,(mWidth+mCellWidth)/2,startY+cellHeight*(i+1),mPaintR);
            l("s--s--"+(startY+cellHeight*i));
            l("draw,"+startX+","+txtY+"="+mABC.get(i));
            canvas.drawText(mABC.get(i), startX,txtY,mPaint);
            l("s--e--"+(startY+cellHeight*i+cellHeight));

        }
    }

    private void setPram() {
        Rect bounds=new Rect();
        int maxTxtH=0;
        for (int i=0,l=mABC.size();i<l;i++){
            mPaint.getTextBounds(mABC.get(i),0,mABC.get(i).length(),bounds);
            if (bounds.height()>maxTxtH){
                maxTxtH=bounds.height();
            }
        }
        cellHeight=maxTxtH+mADBPadding*2;
        if (cellHeight*mABC.size()>mHeight){
            cellHeight=mHeight/mABC.size();
        }
        mCellWidth=bounds.width();
        startX= (mWidth-mCellWidth)/2;
        boolean odd=mABC.size()%2!=0;
        if (odd){
            startY=mHeight/2-mABC.size()/2*cellHeight-cellHeight/2;
        }else {
            startY=mHeight/2-mABC.size()/2*cellHeight;
        }
        l("cell txtH="+maxTxtH+" cellH="+cellHeight+"startY="+startY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        l("dis="+event.getY()+","+event.getAction());
        float y=event.getY();
        if (mABC!=null&&y>=startY&&y<=startY+mABC.size()*cellHeight){
            float dexY=y-startY;
            int p=(int)dexY/cellHeight;
            if (p==mABC.size()){
                //最后一个item会出现
                p--;
            }
            l(mABC.get(p));
            if (null!=listener) {
                listener.onChoose(p,mABC.get(p));
                listener.onUp(false);
            }
        }
        if (event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_CANCEL){
            if (null!=listener) listener.onUp(true);
        }
        return true;
    }

    public void setABC(List<String> abc) {
        this.mABC = abc;
        l("adb size==="+abc.size());
        invalidate();
    }
    private void l(String msg){
        Log.d("SlideBarView",msg);
    }

    public interface OnScrollListener{
        void onChoose(int p,String letter);
        void onUp(boolean isUp);
    }

    public void setListener(OnScrollListener listener) {
        this.listener = listener;
    }
}
