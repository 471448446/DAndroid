package better.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

/**
 * Created by better on 2017/9/14 14:12.
 */

public class ForWordUtil {
    public static final int NO_REQUEST_CODE = -1;

    public static void to(Context context, Class<?> des) {
        to(context, des, null);
    }

    public static void to(Context context, Class<?> des, Bundle ex) {
        to(context, des, ex, NO_REQUEST_CODE);
    }

    public static void to(Context context, Class<?> des, Bundle extra, int reqCode) {
        Intent intent = new Intent(context, des);
        if (null != extra) {
            intent.putExtras(extra);
        }
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (NO_REQUEST_CODE != reqCode) {
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, reqCode);
            }
        } else {
            context.startActivity(intent);
        }
    }

    public static void to(Fragment context, Class<?> des) {
        to(context, des, null, NO_REQUEST_CODE);
    }

    public static void to(Fragment context, Class<?> des, Bundle ex) {
        to(context, des, ex, NO_REQUEST_CODE);
    }

    public static void to(Fragment context, Class<?> des, Bundle extra, int reqCode) {
        Intent intent = new Intent(context.getContext(), des);
        if (null != extra) {
            intent.putExtras(extra);
        }
        if (NO_REQUEST_CODE != reqCode) {
            context.startActivityForResult(intent, reqCode);
        } else {
            context.startActivity(intent);
        }
    }

    public static void service(Activity context, Class<?> des) {
        service(context, des, null);
    }

    public static void service(Activity context, Class<?> des, Bundle extra) {
        Intent intent = new Intent(context, des);
        if (null != extra) {
            intent.putExtras(extra);
        }
        context.startService(intent);
    }

    public static boolean isExistActivity(Context activity, Intent intent) {
        return null != intent.resolveActivity(activity.getPackageManager());
    }
}
