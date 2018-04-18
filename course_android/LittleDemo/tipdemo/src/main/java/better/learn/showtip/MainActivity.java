package better.learn.showtip;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TipDialog.OnGetBitShowListener {

    private TextView mTextView;
    private ImageView mImageView;

    private TipBean oneTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.main_txt);
        mImageView = (ImageView) findViewById(R.id.main_img);

        mTextView.setText("SDBDBBDBD");
        mTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (null != oneTip) {
                    return;
                }

                Bitmap bitmap = getBitMap(mTextView);
                if (null != bitmap) {

                    Bitmap b = getScaleBitmap(bitmap, 2, 30, 0, 0);
                    mImageView.setImageBitmap(bitmap);

                    oneTip = new TipBean(mTextView.getTop(), b);

                    new TipDialog()
                            .setOnGetBitShowListener(MainActivity.this)
                            .show(getFragmentManager(), "dialog");
                }
            }
        });
    }

    private Bitmap getBitMap(View view) {
        // 设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
        // 如果绘图缓存无法，强制构建绘图缓存
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return bitmap;
    }

    private Bitmap getScaleBitmap(Bitmap bitmap, int left, int top, int right, int bottom) {
        return Bitmap.createBitmap(bitmap, left, top, bitmap.getWidth() - left - right, bitmap.getHeight() - top - bottom);
    }

    @Override
    public TipBean getBitmap() {
        return oneTip;
    }
}
