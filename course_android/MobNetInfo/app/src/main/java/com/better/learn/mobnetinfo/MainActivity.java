package com.better.learn.mobnetinfo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * https://github.com/react-native-community/react-native-netinfo/issues/153
 * 从 Android 11 开始，5g检查：https://developer.android.com/about/versions/11/features/5g#detection
 * 还必须有 READ_PHONE_STATE
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applyInfo(true);
    }

    private void applyInfo(boolean forceCheck) {
        if (forceCheck &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                return;
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            }
        }
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String carrierType = NetUtil.getCurrentCarrierType(this);
        String carrierName = NetUtil.getCarrierName(this);
        String cg = NetUtil.cellularGeneration(this);
        String cg1 = NetUtil.cellularGeneration1(this);
        String cg2 = NetUtil.cellularGeneration2(this);
        String cg3 = NetUtil.cellularGeneration3(this);
        String cg4 = NetUtil.cellularGeneration4(this);
        String cg5 = NetUtil.cellularGeneration5(this);
        String cg6 = NetUtil.cellularGeneration6(this);
        String cg7 = NetUtil.cellularGeneration7(this);
        String cg8 = "";
        try {
            cg8 = NetUtil.asusRogaSimStatusName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView textView = findViewById(R.id.netInfoTxt);

        String builder =
                "sim count =" + (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ? "os version < 23" : telephonyManager.getPhoneCount()) + "\n" +
                        "getSimOperator() =" + carrierType + "\n" +
                        "cg =" + cg + "\n" +
                        "cg1 =" + cg1 + "\n" +
                        "cg2 =" + cg2 + "\n" +
                        "cg3 =" + cg3 + "\n" +
                        "cg4 =" + cg4 + "\n" +
                        "cg5 =" + cg5 + "\n" +
                        "cg6 =" + cg6 + "\n" +
                        "cg7 =" + cg7 + "\n" +
                        "cg8 =" + cg8 + "\n" +
                        "carrierName() =" + carrierName + "\n";
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                builder += "sim list = no permission";
            } else {
                List<SubscriptionInfo> infoList = subscriptionManager.getActiveSubscriptionInfoList();
                if (null != infoList) {
                    for (SubscriptionInfo info : infoList) {
                        builder += "sim number?,subscription?,displayName? =" + info.getNumber() + "," + info.getSubscriptionId() + "," + info.getDisplayName() + "\n";
                    }
                }
            }
        }

        textView.setText(builder);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        applyInfo(false);
    }
}
