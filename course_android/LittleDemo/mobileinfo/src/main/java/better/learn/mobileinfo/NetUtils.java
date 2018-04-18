package better.learn.mobileinfo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by better on 2016/12/28.
 */

public class NetUtils {
    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifiConnection() {
        ConnectivityManager cm = (ConnectivityManager) MainApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkINfo = cm.getActiveNetworkInfo();
            if (null != networkINfo && networkINfo.isConnected()) {
                return networkINfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
        }
        return false;
    }

    public String getIpAddress() {
        return isWifiConnection() ? getWifiIp() : getLocalIp();
    }

    public static String getWifiIp() {
        WifiManager wifiManager = (WifiManager) MainApp.getInstance().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ipAddress);
    }

    public static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface element = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = element.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    /**
     * 通过wifiManager获取mac地址
     */
    public static String getMacFromWifi() {
        WifiManager wifiManager = (WifiManager) MainApp.getInstance().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (null != wifiInfo) {
            return wifiInfo.getMacAddress();
        }
        return null;
    }

    /**
     * Des Obviously the SIM may not be present at any specific time
     * Create By better on 2016/12/28 13:18.
     */
    public static String getIMSI() {
        TelephonyManager telManager = (TelephonyManager) MainApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return telManager.getSubscriberId();
    }

    public static String getIMEI() {
        TelephonyManager telManager = (TelephonyManager) MainApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return telManager.getDeviceId();
    }


}
