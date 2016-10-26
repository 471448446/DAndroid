package better.lib.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by better on 2016/10/24.
 */

public class ScreenUtils {
    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    public static int getScreenWidth(Context context) {
        return getMetrics(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getMetrics(context).heightPixels;
    }

}
