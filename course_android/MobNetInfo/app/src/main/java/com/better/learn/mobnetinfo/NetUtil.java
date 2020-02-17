package com.better.learn.mobnetinfo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import androidx.core.app.ActivityCompat;

/**
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
        try {
            Object obj = Class.forName(telephonyManager.getClass().getName())
                    .getDeclaredMethod("getServiceState", new Class[0]).invoke(telephonyManager, new Object[0]);

            Method[] methods = Class.forName(obj.getClass().getName()).getDeclaredMethods();

            for (Method method : methods) {
                if (method.getName().equals("getNrStatus") || method.getName().equals("getNrState")) {
                    method.setAccessible(true);
                    return ((Integer) method.invoke(obj, new Object[0])).intValue() == 3;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean fucked5g2(Context context, TelephonyManager telephonyManager) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.GET_RECEIVERS) {
            Log.e("lds_5g", "use location get cg");
            List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            String CellInfoNr = "CellInfoNr";
            String CellInfoLte = "CellInfoLte";
            String cellName;
            if (null != allCellInfo && !allCellInfo.isEmpty()) {
                for (CellInfo cellInfo : allCellInfo) {

                    cellName = cellInfo.getClass().getSimpleName();

                    if (CellInfoNr.equalsIgnoreCase(cellName) ||
                            CellInfoLte.equalsIgnoreCase(cellName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static String mapSubtype(int subtype, NetworkInfo networkInfo) {
        String networkType = "";
        switch (subtype) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
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
            case TelephonyManager.NETWORK_TYPE_GSM:
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
            return "WiFi";
        }
        subtype = networkInfo.getSubtype();
        return mapSubtype(subtype, networkInfo);
    }

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
            Log.e("lds_5g", "1 getCellularType2(): " + subtype);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            subtype = telephonyManager.getNetworkType();
            Log.e("lds_5g", "2 getCellularType2()" + subtype);
        } else {
            networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo == null) {
                return networkType;
            }
            subtype = networkInfo.getSubtype();
            Log.e("lds_5g", "3 getCellularType2(): " + subtype);
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
            Log.e("lds_5g", "1 cellularGeneration3()" + subtype);
        } else {
            networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo == null) {
                return networkType;
            }
            subtype = networkInfo.getSubtype();
            Log.e("lds_5g", "2 cellularGeneration3(): " + subtype);
        }
        return mapSubtype(subtype, networkInfo);
    }

    /**
     * https://stackoverflow.com/questions/55598359/how-to-detect-samsung-s10-5g-is-running-on-5g-network
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
            } else {
                subtype = telephonyManager.getDataNetworkType();
            }
            Log.e("lds_5g", "1 getCellularType2(): " + subtype);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (fucked5g(telephonyManager)) {
                subtype = 20;
            } else {
                subtype = telephonyManager.getNetworkType();
            }
            Log.e("lds_5g", "2 getCellularType2()" + subtype);
        } else {
            networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo == null) {
                return networkType;
            }
            subtype = networkInfo.getSubtype();
            Log.e("lds_5g", "3 getCellularType2(): " + subtype);
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
            } else {
                subtype = telephonyManager.getDataNetworkType();
            }
            Log.e("lds_5g", "1 getCellularType2(): " + subtype);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (fucked5g2(context, telephonyManager)) {
                subtype = 20;
            } else {
                subtype = telephonyManager.getNetworkType();
            }
            Log.e("lds_5g", "2 getCellularType2()" + subtype);
        } else {
            networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo == null) {
                return networkType;
            }
            subtype = networkInfo.getSubtype();
            Log.e("lds_5g", "3 getCellularType2(): " + subtype);
        }
        return mapSubtype(subtype, networkInfo);
    }

}
