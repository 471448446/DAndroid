package com.better.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.better.banner.transformer.BGAPageTransformer;
import com.better.banner.transformer.TransitionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Des 适用于至少一个的无限滚动,在重复替换fragment时需要注意。
 * 指示点容器背景
 * banner_pointContainerBackground
 * 指示点背景
 * banner_pointDrawable
 * 特殊情况
 * banner_pointDrawable_normal
 * banner_pointDrawable_select
 * 指示点容器左右内间距
 * banner_pointContainerLeftRightPadding dimension
 * 指示点容器上下间距
 * banner_pointContainerLRMargin dimension
 * banner_pointContainerTBMargin dimension
 * 指示点上下外间距
 * banner_pointTopBottomMargin dimension
 * 指示点左右外间距
 * banner_pointLeftRightMargin dimension
 * 指示点的位置
 * banner_pointGravity
 * :
 * top
 * bottom
 * left
 * right
 * center_horizontal
 * :
 * 是否开启自动轮播
 * banner_pointAutoPlayAble boolean
 * 一张图片能否滚动
 * banner_oneitemCanPlayAble boolean
 * 自动轮播的时间间隔
 * banner_pointAutoPlayInterval integer
 * 页码切换过程的时间长度
 * banner_pageChangeDuration integer
 * 页面切换的动画效果
 * banner_transitionEffect
 * :
 * defaultEffect
 * alpha
 * rotate
 * cube
 * flip
 * accordion
 * zoomFade
 * fade
 * zoomCenter
 * zoomStack
 * stack
 * depth
 * zoom
 * :
 * 提示文案的文字颜色
 * banner_tipTextColor reference|color
 * 提示文案的文字大小
 * banner_tipTextSize dimension
 * 是否进行无线滑动
 * banner_ScrollEndless boolean
 * Create By com.better on 2017/1/13 11:42.
 */
public class BBanner extends RelativeLayout implements OnPageChangeListener {
    public static final String TAG = "BBanner";
    public static boolean log = false;
    public int MaxNum = 100;
    private static final int RMP = RelativeLayout.LayoutParams.MATCH_PARENT;
    private static final int RWC = RelativeLayout.LayoutParams.WRAP_CONTENT;
    private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private int mAutoPlayInterval;//自动切花换时间
    private int mPointLeftRightMargin;//原点左右距离
    private int mPointTopBottomMargin;//上下距
    private int mPointContainerTBMargin, mPointContainerLRMargin;//原点容器上下距
    private int mPointContainerLeftRightPadding;//原点容器内容左右距离
    private int mPointGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int mPointDrawableResId = R.drawable.banner_dot_yellow_bgr;//indicator点背景
    private int mPointDrawableNormalResId, mPointDrawableSelectResId;
    private int mPageChangeDuration = 800;
    private int mTipTextSize;
    private int mTipTextColor = Color.WHITE;
    private boolean mAutoPlayAble = true;
    private boolean mIsOneItemScroll = false;//一张图片是否滚动
    private boolean mIsAutoPlaying;
    private boolean mScrollEndlessLess = true;
    private Drawable mPointContainerBackgroundDrawable;//圆点的背景
    private ScrollPagerAdapter mPagerAdapter;
    private BGAViewPager mvPager;
    private LinearLayout mIndicator;
    private ViewConfiguration mViewConfiguration;

    private List<OnPageChangeListener> mOnPageChangeListeners = new ArrayList<>();

    private static final int WHAT_AUTO_PLAY = 1000;//msg
    private Handler mAutoPlayHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mvPager.setCurrentItem(mvPager.getCurrentItem() + 1);
            mAutoPlayHandler.removeMessages(WHAT_AUTO_PLAY);
            mAutoPlayHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayInterval);
            return false;
        }
    });

    public void cleanUp() {
        mAutoPlayHandler.removeCallbacksAndMessages(null);
        mOnPageChangeListeners.clear();
    }

    public LinearLayout getIndicator() {
        return mIndicator;
    }

    public BBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultAttr(context);
        initCustomAttr(context, attrs);
        initView(context);
    }

    private void initDefaultAttr(Context context) {
        mvPager = new BGAViewPager(context);
        mPointLeftRightMargin = 10;
        mPointTopBottomMargin = 15;
        mPointContainerLeftRightPadding = 20;
        mAutoPlayInterval = 5000;
        mPointContainerBackgroundDrawable = new ColorDrawable(Color.parseColor("#00ffffff"));  //#44aaaaaa
        mViewConfiguration = ViewConfiguration.get(getContext());
    }

    private void initCustomAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGABanner);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BGABanner_banner_pointDrawable) {
            mPointDrawableResId = typedArray.getResourceId(attr, R.drawable.banner_dot_yellow_bgr);
        } else if (attr == R.styleable.BGABanner_banner_pointContainerBackground) {
            mPointContainerBackgroundDrawable = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.BGABanner_banner_pointLeftRightMargin) {
            mPointLeftRightMargin = typedArray.getDimensionPixelSize(attr, mPointLeftRightMargin);
        } else if (attr == R.styleable.BGABanner_banner_pointContainerLeftRightPadding) {
            mPointContainerLeftRightPadding = typedArray.getDimensionPixelSize(attr, mPointContainerLeftRightPadding);
        } else if (attr == R.styleable.BGABanner_banner_pointTopBottomMargin) {
            mPointTopBottomMargin = typedArray.getDimensionPixelSize(attr, mPointTopBottomMargin);
        } else if (attr == R.styleable.BGABanner_banner_pointGravity) {
            mPointGravity = typedArray.getInt(attr, mPointGravity);
        } else if (attr == R.styleable.BGABanner_banner_pointAutoPlayAble) {
            mAutoPlayAble = typedArray.getBoolean(attr, mAutoPlayAble);
        } else if (attr == R.styleable.BGABanner_banner_pointAutoPlayInterval) {
            mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
        } else if (attr == R.styleable.BGABanner_banner_pageChangeDuration) {
            mPageChangeDuration = typedArray.getInteger(attr, mPageChangeDuration);
        } else if (attr == R.styleable.BGABanner_banner_transitionEffect) {
            int ordinal = typedArray.getInt(attr, TransitionEffect.Accordion.ordinal());
            setTransitionEffect(TransitionEffect.values()[ordinal]);
        } else if (attr == R.styleable.BGABanner_banner_tipTextColor) {
            mTipTextColor = typedArray.getColor(attr, mTipTextColor);
        } else if (attr == R.styleable.BGABanner_banner_tipTextSize) {
            mTipTextSize = typedArray.getDimensionPixelSize(attr, mTipTextSize);
        } else if (attr == R.styleable.BGABanner_banner_oneitemCanPlayAble) {
            mIsOneItemScroll = typedArray.getBoolean(attr, mIsOneItemScroll);
        } else if (attr == R.styleable.BGABanner_banner_pointDrawable_normal) {
            mPointDrawableNormalResId = typedArray.getResourceId(attr, mPointDrawableNormalResId);
        } else if (attr == R.styleable.BGABanner_banner_pointDrawable_select) {
            mPointDrawableSelectResId = typedArray.getResourceId(attr, mPointDrawableSelectResId);
        } else if (attr == R.styleable.BGABanner_banner_pointContainerTBMargin) {
            mPointContainerTBMargin = typedArray.getDimensionPixelSize(attr, mPointContainerTBMargin);
        } else if (attr == R.styleable.BGABanner_banner_pointContainerLRMargin) {
            mPointContainerLRMargin = typedArray.getDimensionPixelSize(attr, mPointContainerLRMargin);
        } else if (attr == R.styleable.BGABanner_banner_ScrollEndless) {
            mScrollEndlessLess = typedArray.getBoolean(attr, mScrollEndlessLess);
        }
    }

    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        /* 设置不同的id，多个banner在同一个页面时  --better 2017/2/15 16:36. */
        String id = String.valueOf(System.currentTimeMillis());
        if (9 <= id.length()) {
            mvPager.setId(Integer.valueOf(id.substring(id.length() - 9)));
        } else {
            mvPager.setId(R.id.banner_viewpagerId);
        }
        //将indicator放在relive里面方便设置indictor的相对位置
        RelativeLayout.LayoutParams viewpagerParams = new RelativeLayout.LayoutParams(RMP, RMP);
        viewpagerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mvPager, viewpagerParams);

        RelativeLayout pointContainerRl = new RelativeLayout(context);
        RelativeLayout.LayoutParams pointContainerLp = new RelativeLayout.LayoutParams(RMP, RWC);
        // 处理圆点在顶部还是底部
        if ((mPointGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        addView(pointContainerRl, pointContainerLp);

        mIndicator = new LinearLayout(context);
        mIndicator.setOrientation(LinearLayout.HORIZONTAL);
        //始终让圆点居中显示
        mIndicator.setGravity(Gravity.CENTER);
        mIndicator.setPadding(mPointContainerLeftRightPadding, 0, mPointContainerLeftRightPadding, 0);
        if (Build.VERSION.SDK_INT >= 16) {
            mIndicator.setBackground(mPointContainerBackgroundDrawable);
        } else {
            mIndicator.setBackgroundDrawable(mPointContainerBackgroundDrawable);
        }
        RelativeLayout.LayoutParams mIndicatorP = new RelativeLayout.LayoutParams(RWC, RWC);
        mIndicatorP.setMargins(mPointContainerLRMargin, mPointContainerTBMargin, mPointContainerLRMargin, mPointContainerTBMargin);
        //		mIndicatorP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//设置后会造成全屏都是backGround
        int horizontalGravity = mPointGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        // 处理圆点在左边、右边还是水平居中
        if (horizontalGravity == Gravity.LEFT) {
            mIndicatorP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (horizontalGravity == Gravity.RIGHT) {
            mIndicatorP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            mIndicatorP.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }
        pointContainerRl.addView(mIndicator, mIndicatorP);
    }

    //当banner处于Fragment中是切换fragment是不会掉这个函数的所以手动调一下
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        log("onVisibilityChanged()");
        if (visibility == VISIBLE) {
            startAutoPlay();
        } else if (visibility == INVISIBLE || visibility == GONE) {
            stopAutoPlay();
        }
    }

    @Override
    protected void onDetachedFromWindow() {//Activity onDestroy后才调
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    float oldX;
    private OnClickItemListener mOnItemClickListener;

    public OnClickItemListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnClickItemListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            oldX = ev.getX();
        } else if (MotionEvent.ACTION_UP == ev.getAction() || MotionEvent.ACTION_CANCEL == ev.getAction()) {
            if (Math.abs(ev.getX() - oldX) < mViewConfiguration.getScaledTouchSlop()) {
                if (null != mOnItemClickListener && null != mPagerAdapter) {
                    mOnItemClickListener.onClick(mvPager.getCurrentItem() % mPagerAdapter.getLength());
                }
            }
        }
        if (mAutoPlayAble) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void startAutoPlay() {
//        log("startAutoPlay 当前item：" + mvPager.getCurrentItem());
        if (mScrollEndlessLess && null != mvPager.getAdapter() && mAutoPlayAble && !mIsAutoPlaying && (mvPager.getAdapter().getCount() > 1 || mvPager.getAdapter().getCount() == 1 && mIsOneItemScroll)) {
            mIsAutoPlaying = true;
            mAutoPlayHandler.removeMessages(WHAT_AUTO_PLAY);
            mAutoPlayHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayInterval);
        }
    }

    public void stopAutoPlay() {
//        log("stopAutoPlay 当前item：" + mvPager.getCurrentItem());
        if (mScrollEndlessLess && null != mvPager.getAdapter() && mAutoPlayAble && mIsAutoPlaying && (mvPager.getAdapter().getCount() > 1 || mvPager.getAdapter().getCount() == 1 && mIsOneItemScroll)) {
            mIsAutoPlaying = false;
            mAutoPlayHandler.removeMessages(WHAT_AUTO_PLAY);
        }
    }

    /**
     * 设置页码切换过程的时间长度
     *
     * @param duration 页码切换过程的时间长度
     */
    @SuppressWarnings("unused")
    public void setPageChangeDuration(int duration) {
        if (duration >= 0 && duration <= 2000) {
            mvPager.setPageChangeDuration(duration);
        }
    }

    public void setCurrentItem(int position) {
        if (null == mPagerAdapter || position > mPagerAdapter.getLength() - 1) {
            return;
        }
        mvPager.setCurrentItem(position);
    }

    @Deprecated
    public void setOnPagerListener(OnPageChangeListener listener) {
        addOnPagerListener(listener);
    }

    public void addOnPagerListener(OnPageChangeListener listener) {
        mOnPageChangeListeners.add(listener);
    }

    /**
     * 设置页面也换动画
     */
    public void setTransitionEffect(TransitionEffect effect) {
        mvPager.setPageTransformer(true, BGAPageTransformer.getPageTransformer(effect));
    }

    public void setData(FragmentManager manager, ItemAdapter itemAdapter) {
        if (null == mPagerAdapter) {
            mPagerAdapter = new ScrollPagerAdapter(manager, itemAdapter);
            mPagerAdapter.setMaxNum(MaxNum).setOneItemScroll(mIsOneItemScroll).setScrollEndlessLess(mScrollEndlessLess);
            mvPager.setAdapter(mPagerAdapter);
        } else {
            mPagerAdapter.setItemAdapter(itemAdapter);
        }
        mvPager.removeOnPageChangeListener(this);
        mvPager.addOnPageChangeListener(this);
        updateIndicator(null == itemAdapter ? 0 : itemAdapter.getCount());
    }

    private void updateIndicator(int length) {
        mIndicator.removeAllViews();
        if (length == 0 || length == 1) {//
            mvPager.setCurrentItem(0);
        } else {
            mvPager.removeAllViews();
            initPoints(length);
            if (mScrollEndlessLess) {
                //假装可以左右无限滑动,避免获取最新数据后，反方向滑动出现默认页面
                int zeroItem = MaxNum / 2 - (MaxNum / 2) % length;
                mvPager.setCurrentItem(zeroItem);
                startAutoPlay();
            } else {
                mvPager.setCurrentItem(0);
            }
        }
    }

    private void initPoints(int _length) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LWC, LWC);
        lp.setMargins(mPointLeftRightMargin, mPointTopBottomMargin, mPointLeftRightMargin, mPointTopBottomMargin);
        ImageView dot;
        for (int i = 0; i < _length; i++) {
            dot = new ImageView(getContext());
            dot.setLayoutParams(lp);
            if (mPointDrawableNormalResId == 0)
                dot.setImageResource(mPointDrawableResId);
            selectDotBg(0, i, dot);
            mIndicator.addView(dot);
        }
    }

    private void selectDotBg(int selectP, int currentP, ImageView dot) {
        //优先使用分开添加的
        if (mPointDrawableNormalResId != 0) {
            dot.setImageResource(currentP == selectP ? mPointDrawableSelectResId : mPointDrawableNormalResId);
        } else {
            dot.setEnabled(currentP == selectP);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (OnPageChangeListener onPageChangeListener : mOnPageChangeListeners) {
            if (null != onPageChangeListener)
                onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (null == mPagerAdapter) return;
        for (OnPageChangeListener onPageChangeListener : mOnPageChangeListeners) {
            if (null != onPageChangeListener)
                onPageChangeListener.onPageSelected(mPagerAdapter.getRelP(position));
        }
        if (1 < mPagerAdapter.getLength()) {
            int relP = mPagerAdapter.getRelP(position);
            log("real==>" + relP + "currentItem:" + position);
            for (int i = 0; i < mPagerAdapter.getLength(); i++) {
                ImageView dot = (ImageView) mIndicator.getChildAt(i);
                selectDotBg(relP, i, dot);
            }
        }
        // 只有一张图片或者服务器返回0张图片
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        for (OnPageChangeListener onPageChangeListener : mOnPageChangeListeners) {
            if (null != onPageChangeListener) {
                onPageChangeListener.onPageScrollStateChanged(state);
            }
        }
        // 当到第一张时切换到倒数第二张，当到最后一张时切换到第二张
        if (mScrollEndlessLess) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                int curr = mvPager.getCurrentItem();
                int lastReal = mvPager.getAdapter().getCount() - 2;
                if (curr == 0) {
                    mvPager.setCurrentItem(lastReal, false);
                } else if (curr > lastReal) {
                    mvPager.setCurrentItem(1, false);
                }
            }
        }
    }

    public static void log(String msg) {
        if (log) Log.d(TAG, msg);
    }
}
