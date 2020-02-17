package com.better.learn.rippleview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public final class UISizeUtil {

    public static int dpToPx(Context context, float fDpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (fDpValue * scale + 0.5f);
    }

    public static int pxToDp(Context context, float fPxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (fPxValue / scale + 0.5f);
    }

    public static float pxToSp(Context context, float fPxValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                fPxValue, context.getResources().getDisplayMetrics());
    }

    public static float spToPx(Context context, float fSpValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return fSpValue * scale + 0.5f;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(dm);
        return dm;
    }

}
