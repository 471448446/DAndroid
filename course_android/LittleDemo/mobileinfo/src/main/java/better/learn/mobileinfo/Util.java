package better.learn.mobileinfo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import better.library.utils.Utils;

/**
 * Created by better on 2016/12/28.
 */

public class Util extends Utils {
    /**
     * Des Obviously the SIM may not be present at any specific time
     * Create By better on 2016/12/28 13:18.
     */
    public static String getIMSI() {
        TelephonyManager telManager = (TelephonyManager) MainApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return telManager.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getIMEI() {
        TelephonyManager telManager = (TelephonyManager) MainApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return telManager.getDeviceId();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAndroidId(Context context) {
        String androidId = null;
        try {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return androidId;
    }

    public static String getSerial(Context context) {
        if (Build.VERSION.SDK_INT < 9) {
            return null;
        }
        // apps targeting SDK higher than O_MR1 this field is set to UNKNOWN
        if (Build.VERSION.SDK_INT < 26) {
            return Build.SERIAL;
        }
        String var0 = null;
        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            var0 = Build.getSerial();
        }

        //com.umeng.commonsdk.statistics.common.DeviceConfig
        if (TextUtils.isEmpty(var0)) {
            try {
                Class var1 = Class.forName("android.os.Build");
                Method var2 = var1.getMethod("getSerial");
                var0 = (String) var2.invoke(var1);
            } catch (Exception e) {
                // unknown,but AUM-AL20、PIXEL2 get
                var0 = Build.SERIAL;
                e.printStackTrace();
            }
        }

        return var0;
    }

    // 没有权限就获取不到
    public static String getDeviceId(Context context) {
        String deviceId = null;
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != manager) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    deviceId = invokeDeviceID(manager);
                    Util.log("deviceID:" + deviceId);
                } else {
                    deviceId = invokeImei(manager);
                    Util.log("imei:" + deviceId);
                    if (null == deviceId) {
                        deviceId = invokeMeid(manager);
                        Util.log("meid:" + deviceId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    private static String invokeDeviceID(TelephonyManager manager) {

        try {
            Class clz = Class.forName("android.telephony.TelephonyManager");
            Method function = clz.getDeclaredMethod("getDeviceId");
            function.setAccessible(true);
            return (String) function.invoke(manager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String invokeMeid(TelephonyManager tm) {
        try {
            Method function = tm.getClass().getDeclaredMethod("getMeid");
            function.setAccessible(true);
            return (String) function.invoke(tm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String invokeImei(TelephonyManager tm) {
        try {
            Method function = tm.getClass().getDeclaredMethod("getImei");
            function.setAccessible(true);
            return (String) function.invoke(tm);
        } catch (Exception e) {
            //java.lang.SecurityException: getSubscriberId: Neither user 10406 nor current process has android.permission.READ_PHONE_STATE.
            e.printStackTrace();
        }
        return null;
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

    public static String getMacFromBluetooth() {
        try {
            BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
            if (ba != null) {
                return ba.getAddress();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return "";
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
