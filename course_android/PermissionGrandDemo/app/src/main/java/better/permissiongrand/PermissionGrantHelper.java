package better.permissiongrand;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by better on 16/8/11.
 */
public class PermissionGrantHelper {
    public static final int REQ_CODE_CALENDAR=9999;
    public static final int REQ_CODE_CAMERA=REQ_CODE_CALENDAR+1;
    public static final int REQ_CODE_CONTACTS=REQ_CODE_CAMERA+1;
    public static final int REQ_CODE_LOCATION=REQ_CODE_CONTACTS+1;
    public static final int REQ_CODE_MICROPHONE=REQ_CODE_LOCATION+1;
    public static final int REQ_CODE_PHONE=REQ_CODE_MICROPHONE+1;
    public static final int REQ_CODE_SENSORS=REQ_CODE_PHONE+1;
    public static final int REQ_CODE_SMS=REQ_CODE_SENSORS+1;
    public static final int REQ_CODE_STORAGE=REQ_CODE_SMS+1;

    public static boolean isGrantedThisPermission(Activity activity, String permission){
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, permission);
    }
    public static boolean isGrantedSuccess(String permission,String isgrantedPermission,int isgranted){
        return permission.equals(isgrantedPermission)&&PackageManager.PERMISSION_GRANTED == isgranted;
    }
    public static boolean isGranted(int isgranted){
        return PackageManager.PERMISSION_GRANTED == isgranted;
    }
}
