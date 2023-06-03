package better.library.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by better on 2017/9/14 14:12.
 */

public class ForWordUtil {
    public static final int NO_REQUEST_CODE = -1;

    public static void to(Object context, Class<?> des) {
        to(context, des, null);
    }

    public static void to(Object context, Class<?> des, Bundle ex) {
        to(context, des, ex, NO_REQUEST_CODE);
    }

    public static void to(Object context, Class<?> des, int reqCode) {
        to(context, des, null, reqCode);
    }

    public static void to(Object context, Class<?> des, Bundle extra, int reqCode) {
        boolean a = context instanceof Context;
        Intent intent;
        if (a) {
            intent = new Intent((Activity) context, des);
        } else {
            intent = new Intent(((Fragment) context).getActivity(), des);
        }
        if (null != extra) {
            intent.putExtras(extra);
        }
        // Fragment 启动的时候newTask收不到回调
        if (!(context instanceof Activity) && !(context instanceof Fragment)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (NO_REQUEST_CODE != reqCode) {
            if (a) {
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, reqCode);
                } else {
                    Utils.log("", "startActivityForResult should pass Activity");
                }
            } else {
                ((Fragment) context).startActivityForResult(intent, reqCode);
            }
        } else {
            if (a) {
                if (context instanceof Activity) {
                    ((Activity) context).startActivity(intent);
                } else {
                    Utils.log("", "startActivityForResult should pass Activity");
                }
            } else {
                ((Fragment) context).startActivity(intent);
            }
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
