package better.hello.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import better.hello.BuildConfig;

/**
 * Created by better on 16/8/11.
 */
public class PermissionGrantHelper {
    public static final int REQ_CODE_CALENDAR = 229;
    public static final int REQ_CODE_CAMERA = REQ_CODE_CALENDAR + 1;
    public static final int REQ_CODE_CONTACTS = REQ_CODE_CAMERA + 1;
    public static final int REQ_CODE_LOCATION = REQ_CODE_CONTACTS + 1;
    public static final int REQ_CODE_MICROPHONE = REQ_CODE_LOCATION + 1;
    public static final int REQ_CODE_PHONE = REQ_CODE_MICROPHONE + 1;
    public static final int REQ_CODE_SENSORS = REQ_CODE_PHONE + 1;
    public static final int REQ_CODE_SMS = REQ_CODE_SENSORS + 1;
    public static final int REQ_CODE_STORAGE = REQ_CODE_SMS + 1;
    public static final int REQ_CODE_MUTI = REQ_CODE_STORAGE + 1;

    public static boolean isGrantedThisPermission(Object activity, String permission) {
        return checkSelfPermissionWrapper(activity,permission);
    }
    public static boolean isGrantedThisPermissionOrGrantDirect(Object activity, String permission, int requestCode) {
        boolean grant= checkSelfPermissionWrapper(activity,permission);
        if (!grant){
            grantPermission(activity,requestCode,permission);
        }
        return grant;
    }
    @TargetApi(Build.VERSION_CODES.M)
    private static boolean checkSelfPermissionWrapper(Object cxt, String permission) {
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
        } else if (cxt instanceof Fragment) {
            Fragment fragment = (Fragment) cxt;
            return fragment.getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            throw new RuntimeException("cxt is net a activity or fragment");
        }
    }

    public static boolean isGrantedSuccess(String permission, String isgrantedPermission, int isgranted) {
        return permission.equals(isgrantedPermission) && PackageManager.PERMISSION_GRANTED == isgranted;
    }

    public static boolean isGranted(int isgranted) {
        return PackageManager.PERMISSION_GRANTED == isgranted;
    }
    /**
    * Des 同事申请一个火多个权限
    * Create By better on 16/8/12 14:26.
    */
    public static void grantPermission(Object activity, int requestCode, String...permission) {
        requestPermissionsWrapper(activity, permission, requestCode);
    }

    private static void requestPermissionsWrapper(Object cxt, String[] permissions, int requestCode) {
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else if (cxt instanceof Fragment) {
            Fragment fragment = (Fragment) cxt;
            fragment.requestPermissions(permissions, requestCode);
        } else {
            throw new RuntimeException("cxt is net a activity or fragment");
        }
    }
    /**
    * Des 点击了不在询问
    * Create By better on 16/8/12 13:38.
    */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
    /**
    * Des 调到应用详情设置页面
    * Create By better on 16/8/12 13:39.
    */
    public static void forWordGrantPermission(Activity activity) {
        String packageName = BuildConfig.APPLICATION_ID;
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
        if (null != intent.resolveActivity(activity.getPackageManager())) {
            activity.startActivity(intent);
        }
    }

}
