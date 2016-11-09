package better.lib.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by better on 2016/11/9.
 */

public class NetUtils {
    final static String TAG = "NetUtils";
    public static final int NETWORK_TYPE_NONE = -0x1; // 断网情况
    public static final int NETWORK_TYPE_WIFI = 0x1; // WIFI模式
    public static final int NETWOKR_TYPE_MOBILE = 0x2; // GPRS模式

    public static int getCurrentNetType(Context mContext) {
        ConnectivityManager connManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI); // WIFI
        NetworkInfo gprs = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // GPRS

        if (wifi != null && wifi.getState() == NetworkInfo.State.CONNECTED) {
            BaseUtils.d(TAG, "Current net type:  WIFI.");
            return NETWORK_TYPE_WIFI;
        } else if (gprs != null && gprs.getState() == NetworkInfo.State.CONNECTED) {
            BaseUtils.d(TAG, "Current net type:  GPRS.");
            return NETWOKR_TYPE_MOBILE;
        }
        Log.e(TAG, "Current net type:  NONE.");
        return NETWORK_TYPE_NONE;
    }

    /**
     * 判断Android客户端网络是否连接
     * 只能判断是否有可用的连接，而不能判断是否能连网
     *
     * @return true/false
     */
    public static boolean checkNet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 检验网络连接 并toast提示
     */
    public boolean noteIntentConnect(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            // 当前网络不可用
            Toast.makeText(context.getApplicationContext(), "请先连接Internet！",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        if (!wifi) { // 提示使用WIFI
            Toast.makeText(context.getApplicationContext(), "建议您使用WIFI以减少流量！",
                    Toast.LENGTH_SHORT).show();
        }
        return true;

    }

    /**
     * 判断网络连接是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            // 如果仅仅是用来判断网络连接
            // 则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断网络状态是否可用
     *
     * @return true: 网络可用 ; false: 网络不可用
     */
    public boolean isConnectInternet(Context mContext) {
        ConnectivityManager conManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null/*注意，这个判断一定要的，要不然会出错*/ && networkInfo.isAvailable();
    }

    /**
     * 判断GPS是否打开
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = lm.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    /**
     * 判断WIFI是否打开
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断是否是3G网络
     */
    public static boolean is3gNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是WIFI网络
     */
    public static boolean isWifiNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        return networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
