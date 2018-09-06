package com.example.better.linechartview;

import android.content.Context;
import android.util.TypedValue;

public class ViewUtil {
    public static float size2sp(float sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, context.getResources().getDisplayMetrics());
    }
}
