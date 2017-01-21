package better.lib.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Des 统一跳转方式
 * Create By better on 2016/12/26 13:17.
 */
public class ForWord {
    public static final int NO_REQUEST_CODE = -1;

    public static void to(Activity context, Class<?> des) {
        to(context, des, null);
    }

    public static void to(Activity context, Class<?> des, Bundle ex) {
        to(context, des, ex, NO_REQUEST_CODE);
    }

    public static void to(Activity context, Class<?> des, int reqCode) {
        to(context, des, null, reqCode);
    }

    public static void to(Activity context, Class<?> des, Bundle extra, int reqCode) {
        Intent intent = new Intent(context, des);
        if (null != extra) {
            intent.putExtras(extra);
        }
        if (NO_REQUEST_CODE != reqCode) {
            context.startActivityForResult(intent, reqCode);
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
}
