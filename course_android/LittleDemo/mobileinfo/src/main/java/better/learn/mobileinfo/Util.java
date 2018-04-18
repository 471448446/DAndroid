package better.learn.mobileinfo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by better on 2016/12/28.
 */

public class Util {
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

    public static String getWifi2Ip(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ipAddress);
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        String ip2 = inetAddress.getHostAddress().toString();
                        String ip3 = inetAddress.getHostAddress();
//                        try {
//
//                            String ip4=InetAddress.getByName(inetAddress.getHostName()).getHostAddress();
//                            Log.d("better","ip46="+ip3);
//
//                        } catch (UnknownHostException e) {
//                            e.printStackTrace();
//                        }
                        Log.d("better", "gprs ip-->" + ip);
                        Log.d("better", "gprs-->" + ip2);
                        Log.d("better", "网管-->" + ip3);
                        return ip3;
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    public static void get() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress()) {
                                String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                                String ip2 = inetAddress.getHostAddress().toString();
                                String ip3 = inetAddress.getHostAddress();

                                Log.d("better", "gprs ip===>" + ip);
                                Log.d("better", "gprs===>" + ip2);
                                Log.d("better", "网管===>" + ip3);
                                try {

                                    String ip4 = InetAddress.getByName(inetAddress.getHostName()).getHostAddress();
                                    Log.d("better", "ip46===>" + ip4);

                                } catch (UnknownHostException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (SocketException ex) {
                }
            }
        }).start();
    }

    public static String getCurrentNetType(Context context) {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = "null";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "wifi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA
                    || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
                    || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                type = "4g";
            }
        }
        return type;
    }

    /**
     * 通过wifiManager获取mac地址
     */
    public static String getMacFromWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String mac = wifiInfo.getMacAddress();
        return mac;
    }

    // 获取当前时间
    public static String time() {
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(time);
        String strTime = format.format(date);
        return strTime;
    }
}
