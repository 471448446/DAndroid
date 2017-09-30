package better.demo.scrolldownimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainOldActivity extends AppCompatActivity {
    private static final String TAG = "better";
    RelativeLayout topView;
    ImageView main_img;

    float initScreenHeight = 0.5f;
    float maxScreenHeight = 1.1f;
    int screenW;

    Bitmap mBitmapBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);


        final ScrollView scrollView = (ScrollView) findViewById(R.id.main_list);
        topView = (RelativeLayout) findViewById(R.id.main_top);
        main_img = (ImageView) findViewById(R.id.main_img);
        ScrollDownInfluencesTopFirstViewOld topFirstView = (ScrollDownInfluencesTopFirstViewOld) findViewById(R.id.main_lay);

        screenW = getMetrics(this).widthPixels;
        topView.getLayoutParams().height = (int) (screenW * initScreenHeight);
        topView.requestLayout();
        mBitmapBg = getOriginalBitmapScaleByW(screenW);
        Log.d("Better", screenW + "," + mBitmapBg.getWidth() + "," + mBitmapBg.getHeight() + ",初始高度：" + topView.getLayoutParams().height);
        main_img.setImageBitmap(getBitmap(initScreenHeight, screenW * initScreenHeight, mBitmapBg));
        topFirstView.setTopInfluencesView(topView);
        topFirstView.setOnScrollTopListener(new ScrollDownInfluencesTopFirstViewOld.OnScrollTopListener() {
            @Override
            public boolean isScrolledToTop() {
                //https://stackoverflow.com/questions/38029423/check-if-a-scrollview-has-reached-the-top-of-the-layout
                return 0 == scrollView.getScrollY();
            }

            @Override
            public void onTopMove(float dy) {
                int h = (int) (topView.getLayoutParams().height + dy);
                if (h > screenW * maxScreenHeight) {
                    h = (int) (screenW * maxScreenHeight);
                } else if (h < screenW * initScreenHeight) {
                    h = (int) (screenW * initScreenHeight);
                }
                topView.getLayoutParams().height = h;
                topView.requestLayout();
                //图片高的最大值
                if (main_img.getDrawable() instanceof BitmapDrawable) {
                    ((BitmapDrawable) main_img.getDrawable()).getBitmap().recycle();
                }
                Bitmap b = getBitmap(initScreenHeight, topView.getLayoutParams().height + dy, mBitmapBg);
                Log.d("better", "bitmap:" + b.getWidth() + "," + b.getHeight() + "...");
                main_img.setImageBitmap(b);
            }
        });

        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainOldActivity.this, "ww", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //展示背景
    private Bitmap getBitmap(float mini, float currentHeight, Bitmap originalBitmap) {
        int y = (int) (originalBitmap.getHeight() - currentHeight);
        if (y < 0) y = 0;
        if (y > originalBitmap.getHeight() * mini) {
            y = (int) (originalBitmap.getHeight() * mini);
        }
        return Bitmap.createBitmap(originalBitmap, 0, y, originalBitmap.getWidth(), originalBitmap.getHeight() - y);
    }

    //将背景图片缩放成屏幕宽的比例
    private Bitmap getOriginalBitmapScaleByW(int screenW) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        float scaleX = screenW * 1.0f / bitmap.getWidth();
        float scaleY = screenW * 1.0f / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }
}
