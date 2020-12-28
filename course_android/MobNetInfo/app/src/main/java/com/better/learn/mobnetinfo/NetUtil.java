package com.better.learn.mobnetinfo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

/**
 * 以下检测手机移动网咯类型的方法，都无法侦测到5g类型
 * <p>
 * {@link TelephonyManager#NETWORK_TYPE_NR} 表示5g网络，获取方式
 * 1. {@link NetworkInfo#getSubtype()} 29废弃
 * 2. {@link TelephonyManager#getNetworkType()} R上被废弃
 * 3. {@link TelephonyManager#getDataNetworkType()} R上推荐使用
 * 高版本需要权限：getNetworkType() 、getDataNetworkType()、getVoiceNetworkType()
 * This method was deprecated in API level R.use getDataNetworkType()
 * 但是都无法获取。
 * <p>
 * 测试手机：
 * 名称|android 版本|系统版本|版本号
 * 华为Meta30（TAS NA00）5G|android 10| EMUI 10.0.0|10.0.0.195
 * 华为Meta30 Pro 5G|
 * <p>
 * Created by better on 2020-02-12 11:08.
 */
public class NetUtil {
    public static final String CARRIER_UNKNOWN = "";
    public static final String CARRIER_CMCC = "cmcc";
    public static final String CARRIER_UNICOM = "UNICOM";
    public static final String CARRIER_TELECOM = "telecom";

    /**
     * 当前设置的拨打电话的运营商
     */
    public static String getCarrierName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (null == telephonyManager) {
            return CARRIER_UNKNOWN;
        }
        String networkOperatorName = telephonyManager.getNetworkOperatorName();
        if (TextUtils.isEmpty(networkOperatorName)) {
            networkOperatorName = telephonyManager.getSimOperatorName();
        }
        return networkOperatorName;
    }

    /**
     * https://blog.csdn.net/ReadyShowShow/article/details/84308366
     * 当前设置的联网运营商
     */
    public static String getCurrentCarrierType(Context context) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            return CARRIER_UNKNOWN;
        }
        Context contextApplication = context.getApplicationContext();
        TelephonyManager telMag = (TelephonyManager) contextApplication.getSystemService(Context.TELEPHONY_SERVICE);
        if (telMag == null) {
            return CARRIER_UNKNOWN;
        }
        return parseOperatorCode(telMag.getSimOperator());
    }

    public static String parseOperatorCode(String operatorCode) {
        if (operatorCode == null || "".equals(operatorCode)) return CARRIER_UNKNOWN;
        switch (operatorCode) {
            case "46000":
            case "46002":
            case "46007":
            case "46008":
                return CARRIER_CMCC;
            case "46001":
            case "46006":
            case "46009":
                return CARRIER_UNICOM;
            case "46003":
            case "46005":
            case "46011":
                return CARRIER_TELECOM;
            default:
                return CARRIER_UNKNOWN;
        }
    }

    /**
     * https://stackoverflow.com/questions/2919414/get-network-type
     */
    public static String cellularGeneration(Context context) {
        String networkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE))
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo == null) {
            return networkType;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WiFi";
        }
        int subtype = networkInfo.getSubtype();
        return mapSubtype(subtype, networkInfo);
    }

    private static boolean fucked5g(TelephonyManager telephonyManager) {
        Log.e("lds_5g", "fucked5g reflect");
        try {
            Object obj = Class.forName(telephonyManager.getClass().getName())
                    .getDeclaredMethod("getServiceState", new Class[0]).invoke(telephonyManager, new Object[0]);

            Method[] methods = Class.forName(obj.getClass().getName()).getDeclaredMethods();

            for (Method method : methods) {
                Log.e("lds_5g", "fucked5g reflect：" + method.getName());
                if (method.getName().equals("getNrStatus") || method.getName().equals("getNrState")) {
                    method.setAccessible(true);
                    return ((Integer) method.invoke(obj, new Object[0])).intValue() == 3;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("lds_5g", "fucked5g:" + e.getMessage());

        }
        return false;
    }

    /**
     * [5G 网络的两种模式 SA NSA](https://developer.android.com/guide/topics/connectivity/5g?hl=zh-cn)
     * SA:CellInfoNr 只是用5G
     * NSA:CellInfoLte&&CellInfoNr 第一4G网络+第二5G网咯
     * <p>
     * https://developer.android.com/reference/android/telephony/CellInfoNr
     */
    private static boolean fucked5g2(Context context, TelephonyManager telephonyManager) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.e("lds_5g", "fucked5g2 use location");
            List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            String CellInfoNr = "CellInfoNr";
            String cellName;
            if (null != allCellInfo && !allCellInfo.isEmpty()) {
                for (CellInfo cellInfo : allCellInfo) {

                    cellName = cellInfo.getClass().getSimpleName();
                    Log.e("lds_5g", "fucked5g2 use location : " + cellName);

                    if (CellInfoNr.equalsIgnoreCase(cellName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 1.【compileSdkVersion 29】{@link TelephonyManager#getNetworkClass()} 这个方法根本没有区分5g
     * 但是官方的 {@link https://cs.android.com/android/platform/superproject/+/master:frameworks/base/telephony/java/android/telephony/TelephonyManager.java?q=TelephonyManager}
     * 区分了。
     * 2. https://stackoverflow.com/questions/2802472/detect-network-connection-type-on-android
     * 链接中使用反射调用{@link TelephonyManager#getNetworkClass()}，既然正常情况都获取不到，反射就能获取了？
     */
    private static String mapSubtype(int subtype, NetworkInfo networkInfo) {
        String networkType = "";
        switch (subtype) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                //{@link TelephonyManager#getNetworkClass()} 归为2g
            case TelephonyManager.NETWORK_TYPE_GSM:
                networkType = "2G";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                networkType = "3G";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                /*
                https://en.droidwiki.org/wiki/4G_or_LTE:_What_is_shown_when%3F
                @hide NETWORK_TYPE_LTE_CA
                 */
            case 19:
                networkType = "4G";
                break;
            //https://developer.android.com/reference/android/telephony/TelephonyManager#NETWORK_TYPE_NR
            case 20:
                networkType = "5G";
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                if (null != networkInfo) {
                    final String subtypeName = networkInfo.getSubtypeName();
                    boolean any = false;
                    for (String s : Arrays.asList("SCDMA", "WCDMA", "CDMA2000")) {
                        if (s.equalsIgnoreCase(subtypeName)) {
                            any = true;
                        }
                    }
                    if (any) {
                        networkType = "3G";
                    } else {
                        networkType = "";
                    }
                }
                break;
        }
        return networkType;
    }

    /**
     * https://stackoverflow.com/questions/2919414/get-network-type
     */
    public static String cellularGeneration1(Context context) {
        String networkType = "";
        int subtype;
        ConnectivityManager service = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == service) {
            return networkType;
        }
        NetworkInfo networkInfo = service
                .getActiveNetworkInfo();
        if (networkInfo == null) {
            return networkType;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.e("lds_5g", "cg1. 1 wifi");
            return "WiFi";
        }
        subtype = networkInfo.getSubtype();
        Log.e("lds_5g", "cg1. 2 getSubtype(): " + subtype);
        return mapSubtype(subtype, networkInfo);
    }

    /**
     * 使用 telephonyManager.getDataNetworkType()
     */
    public static String cellularGeneration2(Context context) {
        String networkType = "";
        int subtype;
        NetworkInfo networkInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            subtype = telephonyManager.getDataNetworkType();
            Log.e("lds_5g", "cg2. 3-1 getDataNetworkType(): " + subtype);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            subtype = telephonyManager.getNetworkType();
            Log.e("lds_5g", "cg2. 3-2 getNetworkType()" + subtype);
        } else {
            networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo == null) {
                return networkType;
            }
            subtype = networkInfo.getSubtype();
            Log.e("lds_5g", "cg2. 3-3 getSubtype(): " + subtype);
        }
        return mapSubtype(subtype, networkInfo);
    }

    public static String cellularGeneration3(Context context) {
        String networkType = "";
        int subtype;
        NetworkInfo networkInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            subtype = telephonyManager.getNetworkType();
            Log.e("lds_5g", "cg3. 2-1 getNetworkType()" + subtype);
        } else {
            networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo == null) {
                return networkType;
            }
            subtype = networkInfo.getSubtype();
            Log.e("lds_5g", "cg3. 2-2 getSubtype(): " + subtype);
        }
        return mapSubtype(subtype, networkInfo);
    }

    /**
     * https://stackoverflow.com/questions/55598359/how-to-detect-samsung-s10-5g-is-running-on-5g-network
     * 作者通过TelephonyManager.getNetworkType() 获取出来的值是13 也就是4g，
     * 然后尝试用 反射的方式获取TelephonyManager.getServiceState()检查是否包含5G相关的描述类。看评论是成功了
     */
    public static String cellularGeneration4(Context context) {
        String networkType = "";
        int subtype;
        NetworkInfo networkInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (fucked5g(telephonyManager)) {
                subtype = 20;
                Log.e("lds_5g", "cg4. 3-1 fucked5g(): " + subtype);
            } else {
                subtype = telephonyManager.getDataNetworkType();
                Log.e("lds_5g", "cg4. 3-1 getDataNetworkType(): " + subtype);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (fucked5g(telephonyManager)) {
                subtype = 20;
                Log.e("lds_5g", "cg4. 3-2 fucked5g()" + subtype);
            } else {
                subtype = telephonyManager.getNetworkType();
                Log.e("lds_5g", "cg4. 3-2 getNetworkType()" + subtype);
            }
        } else {
            networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo == null) {
                return networkType;
            }
            subtype = networkInfo.getSubtype();
            Log.e("lds_5g", "cg4. 3-3 getSubtype(): " + subtype);
        }
        return mapSubtype(subtype, networkInfo);
    }

    /**
     * https://developer.android.com/guide/topics/connectivity/5g
     */
    public static String cellularGeneration5(Context context) {
        String networkType = "";
        int subtype;
        NetworkInfo networkInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (fucked5g2(context, telephonyManager)) {
                subtype = 20;
                Log.e("lds_5g", "cg5. 3-1 fucked5g2(): " + subtype);
            } else {
                subtype = telephonyManager.getDataNetworkType();
                Log.e("lds_5g", "cg5. 3-1 getDataNetworkType(): " + subtype);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (fucked5g2(context, telephonyManager)) {
                subtype = 20;
                Log.e("lds_5g", "cg5. 3-2 fucked5g2()" + subtype);
            } else {
                subtype = telephonyManager.getNetworkType();
                Log.e("lds_5g", "cg5. 3-2 getNetworkType()" + subtype);
            }
        } else {
            networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo == null) {
                return networkType;
            }
            subtype = networkInfo.getSubtype();
            Log.e("lds_5g", "cg5. 3-3 getSubtype(): " + subtype);
        }
        return mapSubtype(subtype, networkInfo);
    }


    /**
     * https://stackoverflow.com/questions/51304143/how-detect-networktype-2g-3g-lte-when-connected-to-wifi
     */
    public static String cellularGeneration6(Context context) {
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int subtype;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            subtype = telephonyManager.getVoiceNetworkType();
            Log.e("lds_5g", "cg6. 2-1 getVoiceNetworkType(): " + subtype);
        } else {
            subtype = telephonyManager.getNetworkType();
            Log.e("lds_5g", "cg6. 2-2 or READ_PHONE_STATE no permission then getNetworkType(): " + subtype);
        }

        return mapSubtype(subtype, null);
    }

    /**
     * https://stackoverflow.com/questions/9283765/how-to-determine-if-network-type-is-2g-3g-or-4g
     * https://www.v2ex.com/t/635712#reply5
     */
    public static String cellularGeneration7(Context context) {
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int subtype;
        subtype = telephonyManager.getNetworkType();
        Log.e("lds_5g", "cg7. getNetworkType(): " + subtype);

        return mapSubtype(subtype, null);
    }

    /**
     * 华硕手机5G roga 3,android 10，可以获取
     * 判断是否是5G网络，因为在设置页面（我的手机-状态信息-Sim状态）中可以看见是否是5G网络，反编译查看源码后大致如下逻辑：
     * com.android.settings.deviceinfo.SimStatus#updateNetworkType()
     * 如果支持5G网络，
     * 1. 首先系统有个flag表示支持，既：persist.vendor.asus.mobile_slot
     * 2. 通过全局的系统广播获取当前sim卡网络类型。必须不小于20
     * 3. 根据系统提供的网络名称判断是否是LTE网络
     * 这样看起来，这个是系统自己实现了一套5G网络类型，因为这种时候getNetworkType()返回的还是13.
     *
     * @return 网络类型
     */
    public static String asusRogaSimStatusName(Context context) throws Exception {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return "not support";
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "no permission";
        }
        SubscriptionInfo mSubscriptionInfo = SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(0);
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int dataNetworkType = mTelephonyManager.getDataNetworkType();
//        String networkTypeName = TelephonyManager.getNetworkTypeName(dataNetworkType);
        Class telClass = TelephonyManager.class;
        Method getNetworkTypeName = telClass.getMethod("getNetworkTypeName");
        String networkTypeName = (String) getNetworkTypeName.invoke(mTelephonyManager, null);
        String ii = Other.get("persist.vendor.asus.mobile_slot");
        int i = 0;
        int dataType = AusuReceiver.getInstance().getmStatusbarDataType()[i];
        i = Integer.parseInt(ii);
        if (i == 0 && dataType >= 20 && ("LTE".equals(networkTypeName) || "LTE_CA".equals(networkTypeName))) {
            networkTypeName = "LTE (4G) & NR (5G)";
        }

        return networkTypeName + ":" + dataType;
    }
//
//    private boolean isIWLANAndNoImsRegistered(Context context) {
//        boolean z = false;
//        try {
//            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                return true;
//            }
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                return true;
//            }
//
//            SubscriptionInfo mSubscriptionInfo = SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(0);
//            TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            int subscriptionId = mSubscriptionInfo.getSubscriptionId();
//            int accessNetworkTechnology = getCurrentServiceState().getNetworkRegistrationInfo(2, 1).getAccessNetworkTechnology();
//            Log.d("SimStatus", "isIWLANAndNoImsRegistered(): actualDataNetworkType = " + accessNetworkTechnology);
//            if (accessNetworkTechnology == 0) {
//                accessNetworkTechnology = mTelephonyManager.getDataNetworkType(subscriptionId);
//            }
//            boolean isImsRegistered = mTelephonyManager.isImsRegistered(subscriptionId);
//            Log.d("SimStatus", "isIWLANAndNoImsRegistered(): actualDataNetworkType = " + accessNetworkTechnology + ", isImsRegistered = " + isImsRegistered);
//            if (accessNetworkTechnology == 18 && !isImsRegistered) {
//                z = true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.d("SimStatus", "isIWLANAndNoImsRegistered(): ret = " + z);
//        return z;
//    }


}
