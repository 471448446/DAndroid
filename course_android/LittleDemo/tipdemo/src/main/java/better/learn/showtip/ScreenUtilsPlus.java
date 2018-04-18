package better.learn.showtip;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Des 不持有单列
 * Create By better on 2016/12/22 10:46.
 */
public class ScreenUtilsPlus {
    public static int getScreenWidth() {
        return getMetrics(App.getInstance()).widthPixels;
    }

    public static int getScreenHeight() {
        return getMetrics(App.getInstance()).heightPixels;
    }

    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }
}
