package com.better.learn.teststart.night;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.view.ViewCompat;

import java.util.LinkedList;

public class Utils {

    /* renamed from: if  reason: not valid java name */
    public static boolean nightModel(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            Resources resources = context.getResources();
            // 这步是官方的判断方式
            return (resources.getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                    == Configuration.UI_MODE_NIGHT_YES;
        }
        try {
            boolean z = false;
            int i = m7726do(context) | ViewCompat.MEASURED_STATE_MASK;
            int red = Color.red((int) ViewCompat.MEASURED_STATE_MASK) - Color.red(i);
            int green = Color.green((int) ViewCompat.MEASURED_STATE_MASK) - Color.green(i);
            int blue = Color.blue((int) ViewCompat.MEASURED_STATE_MASK) - Color.blue(i);
            if (Math.sqrt(((double) ((green * green) + (red * red))) + ((double) (blue * blue))) < 180.0d) {
                z = true;
            }
            return !z;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static int m7726do(Context context) throws Exception {
        RemoteViews remoteViews = new NotificationCompat.Builder(context).build().contentView;
        View inflate = LayoutInflater.from(context).inflate(remoteViews.getLayoutId(), (ViewGroup) null, false);
        if (inflate == null) {
            throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
        }
        ViewGroup viewGroup = (ViewGroup) inflate;
//        if (viewGroup.findViewById(0x1020016) != null) {
//            View findViewById = viewGroup.findViewById(0x1020016);
//            if (findViewById != null) {
//                return ((TextView) findViewById).getCurrentTextColor();
//            }
//            throw new NullPointerException("null cannot be cast to non-null type android.widget.TextView");
//        }
        LinkedList<View> linkedList = new LinkedList<>();
        linkedList.add(viewGroup);
        int i = 0;
        while (linkedList.size() > 0) {
            ViewGroup viewGroup2 = (ViewGroup) linkedList.getFirst();
            int childCount = viewGroup2.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                if (viewGroup2.getChildAt(i2) instanceof ViewGroup) {
                    View childAt = viewGroup2.getChildAt(i2);
                    if (childAt != null) {
                        linkedList.add((ViewGroup) childAt);
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
                    }
                } else if (!(viewGroup2.getChildAt(i2) instanceof TextView)) {
                    continue;
                }
                View childAt2 = viewGroup2.getChildAt(i2);
                if (childAt2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type android.widget.TextView");
                } else if (((TextView) childAt2).getCurrentTextColor() == -1) {
                    continue;
                }
                View childAt3 = viewGroup2.getChildAt(i2);
                if (childAt3 != null) {
                    i = ((TextView) childAt3).getCurrentTextColor();
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.widget.TextView");
                }
            }
            linkedList.remove(viewGroup2);
        }
        return i;
    }

}
