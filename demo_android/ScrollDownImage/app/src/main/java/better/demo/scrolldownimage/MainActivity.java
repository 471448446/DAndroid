package better.demo.scrolldownimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageView main_img;
    ScrollView scrollView;
    ScrollDownInfluencesTopFirstView mFirstView;
    Bitmap mBitmapBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = (ScrollView) findViewById(R.id.main_list);
        main_img = (ImageView) findViewById(R.id.main_img);
        mFirstView = (ScrollDownInfluencesTopFirstView) findViewById(R.id.main_lay);
        mBitmapBg = getOriginalBitmapScaleByW(getMetrics(this).widthPixels);
        main_img.setImageBitmap(mBitmapBg);

        mFirstView.setOnScrollTopListener(new ScrollDownInfluencesTopFirstView.OnScrollTopListener() {
            @Override
            public boolean isScrolledToTop() {
                return 0 == scrollView.getScrollY();
            }
        });
        main_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "He", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.main_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "11", Toast.LENGTH_SHORT).show();
            }
        });
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
