package better.learn.screenheight;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    ConstraintLayout mainLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.main_txt);
        mainLay = findViewById(R.id.main_lay);
        mainLay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d("Better", "main height =" + mainLay.getHeight());
                if (null != getSupportActionBar()) {
                    Log.d("Better", "actionBar height =" + getSupportActionBar().getHeight());
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mainLay.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            showScreenInfo();
        }
        test();
    }

    private void test() {
        Rect rect = new Rect();
        View content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        Log.d("Better", "content Top=" + content.getTop() + ",bottom=" + content.getBottom() + ",H=" + content.getHeight());
        content.getWindowVisibleDisplayFrame(rect);
        Log.d("Better", "content getWindowVisibleDisplayFrame=" + rect.toString());
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(rect);
        Log.d("Better", "content getDrawingRect=" + rect.toString());
    }

    private void showScreenInfo() {
        int statusBar1 = ScreenUtilsPlus.getStatusHeight(this),
                statusBar2 = ScreenUtilsPlus.getStatusHeight2(this),
                titleHeight = ScreenUtilsPlus.getTitleHeight(this),
                contentHeight = ScreenUtilsPlus.getContentHeight(this),
                decorViewHeight = ScreenUtilsPlus.getDecorHeight(this),
                navHeight = ScreenUtilsPlus.getNavigationBarHeight(this);
        showTxt("屏幕宽高：" + ScreenUtilsPlus.getScreenWidth(this) + "," + ScreenUtilsPlus.getScreenHeight(this),
                "屏幕宽高Real：" + ScreenUtilsPlus.getScreenWidthReal(this) + "," + ScreenUtilsPlus.getScreenHeightReal(this),
                "状态栏：" + statusBar1 + ",or:" + statusBar2,
                "标题栏：" + titleHeight,
                "Content栏：" + contentHeight,
                "DecorView栏:" + decorViewHeight,
                "导航栏：" + navHeight,
                "屏幕高=" + getAddStr(statusBar1, titleHeight, contentHeight, navHeight) + (statusBar1 + titleHeight + contentHeight + navHeight),
                "屏幕高=" + getAddStr(statusBar1, decorViewHeight, navHeight) + (statusBar1 + decorViewHeight + navHeight));
    }

    private void showTxt(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i] + "\n");
        }
        mTextView.setText(builder);
    }

    private StringBuilder getAddStr(int... nums) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < nums.length; i++) {
            builder.append(String.valueOf(nums[i]));
            if (i != nums.length - 1) {
                builder.append("+");
            } else {
                builder.append("=");
            }
        }
        return builder;
    }

}
