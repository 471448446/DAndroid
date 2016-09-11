package better.slidebar;

import android.content.Context;
import android.util.TypedValue;

/**
 * Des 自定义View
 * Create By better on 16/8/24 11:13.
 */
public class ViewUtil {
    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}
