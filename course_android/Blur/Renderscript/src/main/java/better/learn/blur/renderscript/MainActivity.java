package better.learn.blur.renderscript;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * 1、模糊半径(radius)越大，性能要求越高，模糊半径不能超过25，所以并不能得到模糊度非常高的图片。
 * 2、ScriptIntrinsicBlur在API 17时才被引入，如果需要在Android 4.2以下的设备上实现，就需要引入RenderScript Support Library，当然，安装包体积会相应的增大。
 * https://juejin.im/entry/587841d21b69e6006bd6b437
 * Create By better on 2017/9/25 13:24.
 */
public class MainActivity extends AppCompatActivity {
    ImageView mImageView;
    ImageView mImageViewReal;
    RenderScriptGaussianBlur mBlur;
    int blurRadius = 25;
    int scaleRatio = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image_blur);
        mImageViewReal = (ImageView) findViewById(R.id.image_blurReal);
        mBlur = new RenderScriptGaussianBlur(this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_pic);
        Bitmap bitmapMini = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / scaleRatio, bitmap.getHeight() / scaleRatio, true);

        mImageView.setImageBitmap(mBlur.gaussianBlur(blurRadius, bitmap));
        mImageViewReal.setImageBitmap(bitmapMini);
    }
}
