package better.hello.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * drawBitmap用法
 * Matrix缩放
 * Drawable 先订位置，在显示图片
 * Created by better on 2016/10/23.
 */

public class ImageGetter implements Html.ImageGetter {
    private TextView mContainer;
    private List<NewsDetailsBean.ImgBean> list;

    public ImageGetter(TextView tex, List<NewsDetailsBean.ImgBean> list) {
        this.mContainer = tex;
        this.list = list;
    }

    @Override
    public Drawable getDrawable(String source) {
        Utils.d("ImageGetter", source);
        int urlType;
        MyDrawable d = new MyDrawable();
        //大图缩放
        Matrix matrix = null;
        if (source.contains(C.MAP4)) {
            urlType = C.url_type_video;
            source = source.split(C.MAP4)[0];
            matrix = setBitmapPosition(d, new int[]{550, 350});
        } else {
            urlType = C.url_type_image;
            if (null != list)
                for (NewsDetailsBean.ImgBean bean : list) {
                    if (bean.getSrc().toLowerCase().equals(source.toLowerCase())) {
                        matrix = setBitmapPosition(d, RegularUtils.wh(bean.getPixel()));
                    }
                }
        }
        asyncBitmap(urlType, source, d, matrix);
        return d;
    }
//    private int[] handleImageWh(String s){
//        int[] position= RegularUtils.wh(s);
//        if (position[1]>position[0]*C.roatImage){
//            position[1]= (int) (position[0]*C.roatImage);
//        }
//        return position;
//    }

    private void asyncBitmap(final int urlType, String source, final MyDrawable d, final Matrix matrix) {
        Glide.with(App.getApplication()).load(source).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (matrix != null) {
                    // 转换 bitmap 为适合 drawable 的大小
                    Bitmap dstBitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, false);
                    d.setBitmap(dstBitmap);
                } else {
                    d.setBitmap(resource);
                }
                if (C.url_type_video == urlType) {
                    d.setVideoBitmap(BitmapFactory.decodeResource(mContainer.getContext().getResources(), R.mipmap.ic_launcher));
                }
                mContainer.postInvalidate();
            }
        });
    }

    /**
     * Des 水平居中显示
     * http://blog.qiji.tech/archives/10105
     * Create By better on 2016/10/25 17:32.
     */
    private Matrix setBitmapPosition(MyDrawable d, int[] wh) {
        final int canUseW = ScreenUtils.getScreenWidth(App.getApplication()) - mContainer.getPaddingLeft() - mContainer.getPaddingRight();
        if (wh[0] <= canUseW) {
            int start = (canUseW - wh[0]) / 2;
            Rect dis = new Rect(start, 0, start + wh[0], wh[1]);
            d.setDstRect(dis);
            d.setBounds(dis);
            return null;
        } else {
            //大图
            Matrix matrix = new Matrix();
            float scale = (float)canUseW / wh[0];
            matrix.postScale(scale, scale);
            int canUseH = (int) (wh[1] * scale);
//            Utils.d("Better","大图 "+scale+";"+wh[0]+","+wh[1]+";"+canUseW+","+canUseH+".");

            Rect dis = new Rect(0, 0, canUseW, canUseH);
            d.setBounds(dis);
            d.setDstRect(dis);
            return matrix;
        }
    }

    /**
     * Des 图片的展示区域 mDstRect
     * Create By better on 2016/10/31 22:28.
     */
    static class MyDrawable extends BitmapDrawable {
        Bitmap mBitmap/*最终图片*/, mVideoBitmap;
        Rect mDstRect/*用于包裹图片的矩形区域*//*, mSrcRect图片绘制区域*/;

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (mBitmap != null) {
                if (mDstRect != null) {
                    /*null 表示全部显示*/
                    canvas.drawBitmap(mBitmap, null, mDstRect, getPaint());
                } else {
                    canvas.drawBitmap(mBitmap, 0, 0, getPaint());
                }
            }
            if (mVideoBitmap != null) {
                if (mDstRect != null) {
                    int width = mVideoBitmap.getWidth();
                    int height = mVideoBitmap.getHeight();
                    int startX = mDstRect.left/*背景图起点*/ + (mDstRect.width() - width) / 2,
                            startY = (mDstRect.height() - height) / 2;
                    Rect srcRect = new Rect(startX, startY, startX + width, startY + height);
                    canvas.drawBitmap(mVideoBitmap, null, srcRect, getPaint());
                } else {
                    canvas.drawBitmap(mVideoBitmap, 0, 0, getPaint());
                }
            }
        }

        public void setDstRect(Rect mDstRect) {
            this.mDstRect = mDstRect;
        }

        public void setBitmap(Bitmap bitmap) {
            this.mBitmap = bitmap;
        }

        public void setVideoBitmap(Bitmap mVideoBitmap) {
            this.mVideoBitmap = mVideoBitmap;
        }
    }


}
