package com.better.learn.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by better on 2020-02-17 15:24.
 */
public class NetUtil {

    /**
     * https://stackoverflow.com/questions/2919414/get-network-type
     */
    public static String getCellularType() {
        String networkType = "";
        int subtype;
        NetworkInfo networkInfo = null;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
                ActivityCompat.checkSelfPermission(ApplicationHolder.get(),
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) ApplicationHolder.get().getSystemService(Context.TELEPHONY_SERVICE);
            subtype = telephonyManager.getDataNetworkType();
            LogUtil.e("lds_5g", "1 getCellularType()", subtype);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelephonyManager telephonyManager =
                    (TelephonyManager) ApplicationHolder.get().getSystemService(Context.TELEPHONY_SERVICE);
            subtype = telephonyManager.getNetworkType();
            LogUtil.e("lds_5g", "2 getCellularType()", subtype);
        } else {
            networkInfo = ((ConnectivityManager) ApplicationHolder.get().getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo == null) {
                return networkType;
            }
            subtype = networkInfo.getSubtype();
            LogUtil.e("lds_5g", "3 getCellularType()", subtype);
        }*/
        networkInfo = ((ConnectivityManager) ApplicationHolder.get().getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo == null) {
            return networkType;
        }
        subtype = networkInfo.getSubtype();
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
                final String subtypeName = networkInfo.getSubtypeName();
                boolean any = Collections.any(Arrays.asList("SCDMA", "WCDMA", "CDMA2000"),
                        new Functor1<String, Boolean>() {
                            @Override
                            public Boolean apply(String t) {
                                return t.equalsIgnoreCase(subtypeName);
                            }
                        });
                if (any) {
                    networkType = "3G";
                } else {
                    networkType = "";
                }
                break;
        }
        return networkType;
    }
}

