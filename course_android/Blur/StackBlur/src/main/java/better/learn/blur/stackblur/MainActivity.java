package better.learn.blur.stackblur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * 在内存应许的情况下可以快速实现虚化
 * http://wingjay.com/2016/03/12/%E4%B8%80%E7%A7%8D%E5%BF%AB%E9%80%9F%E6%AF%9B%E7%8E%BB%E7%92%83%E8%99%9A%E5%8C%96%E6%95%88%E6%9E%9C%E5%AE%9E%E7%8E%B0/
 * Create By better on 2017/9/22 17:34.
 */
public class MainActivity extends AppCompatActivity {

    ImageView mImageView;
    ImageView mImageViewReal;

    int blurRadius = 8;
    int scaleRatio = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image_blur);
        mImageViewReal = (ImageView) findViewById(R.id.image_blurReal);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_pic);
        Bitmap bitmapMini = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / scaleRatio, bitmap.getHeight() / scaleRatio, true);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageView.setImageBitmap(FastBlurUtil.doBlur(bitmapMini, blurRadius, false));
        mImageViewReal.setImageBitmap(bitmapMini);
    }
}
