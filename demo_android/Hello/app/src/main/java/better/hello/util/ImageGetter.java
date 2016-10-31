package better.hello.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import better.hello.App;
import better.hello.R;
import better.hello.data.bean.NewsDetailsBean;
import better.lib.utils.ScreenUtils;

/**
 * 异步下载，post刷新
 * Created by better on 2016/10/23.
 */

public class ImageGetter implements Html.ImageGetter {
    private TextView mContainer;
    private List<NewsDetailsBean.ImgBean> list;

    //大图缩放
    private Matrix matrix;

    public ImageGetter(TextView tex, List<NewsDetailsBean.ImgBean> list) {
        this.mContainer = tex;
        this.list = list;
    }

    @Override
    public Drawable getDrawable(String source) {
        Utils.d("ImageGetter", source);
        final MyDrawable d = new MyDrawable();
        if (null != list)
            for (NewsDetailsBean.ImgBean bean : list) {
                if (bean.getSrc().toLowerCase().equals(source.toLowerCase())) {
                    matrix = setBitmapPosition(d, RegularUtils.wh(bean.getPixel()));
                }
            }
        Glide.with(App.getApplication()).load(source).asBitmap().error(R.drawable.icon_downloading_err).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (matrix != null) {
                    // 转换 bitmap 为适合 drawable 的大小
                    Bitmap dstBitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, false);
                    d.setBitmap(dstBitmap);
                } else {
                    d.setBitmap(resource);
                }
//                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                mContainer.postInvalidate();
            }
        });
        return d;
    }

    /**
     * Des 水平居中显示
     * http://blog.qiji.tech/archives/10105
     * Create By better on 2016/10/25 17:32.
     */
    private Matrix setBitmapPosition(MyDrawable d, int[] wh) {
//                    int[] wh = RegularUtils.wh(bean.getPixel());
//                    int start = (canUseW - wh[0]) / 2;
//                    Rect dis = new Rect(start, 0, start + wh[0], 0 + wh[1]);
//                    d.setmDstRect(dis);
//                    Utils.d("ImageGetter", wh[0] + "," + wh[1] + ",," + start);
//                    d.setBounds(0, 0, wh[0], wh[1]);
        final int canUseW = ScreenUtils.getScreenWidth(App.getApplication()) - mContainer.getPaddingLeft() - mContainer.getPaddingRight();
        if (wh[0] <= canUseW) {
            int start = (canUseW - wh[0]) / 2;
            Rect dis = new Rect(start, 0, start + wh[0], wh[1]);
            d.setmDstRect(dis);
            d.setBounds(0, 0, wh[0], wh[1]);
            return null;
        } else {
            //大图
            Matrix matrix = new Matrix();
            float scale = wh[0] / canUseW;
            matrix.postScale(scale, scale);

            int canUseH = (int) (wh[1] * scale);
            d.setBounds(0, 0, canUseW, canUseH);

            Rect dis = new Rect(0, 0, canUseW, canUseH);
            d.setmDstRect(dis);
            return matrix;
        }
    }

    static class MyDrawable extends BitmapDrawable {
        Bitmap mBitmap;
        Rect mDstRect, mSrcRect;

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
//                canvas.drawBitmap(mBitmap, 0, 0, getPaint());
            if (mBitmap != null) {
                if (mSrcRect != null && mDstRect != null) {
                    canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, getPaint());
                } else if (mDstRect != null) {
                    int width = mBitmap.getWidth();
                    int height = mBitmap.getHeight();

                    Rect srcRect = new Rect(0, 0, width, height);
                    canvas.drawBitmap(mBitmap, srcRect, mDstRect, getPaint());
                } else {
                    canvas.drawBitmap(mBitmap, 0, 0, getPaint());
                }
            }

        }

        public void setmSrcRect(Rect mSrcRect) {
            this.mSrcRect = mSrcRect;
        }

        public void setmDstRect(Rect mDstRect) {
            this.mDstRect = mDstRect;
        }

        public void setBitmap(Bitmap bitmap) {
            this.mBitmap = bitmap;
        }
    }


}
