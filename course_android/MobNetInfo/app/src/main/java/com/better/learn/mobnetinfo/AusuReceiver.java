package com.better.learn.mobnetinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class AusuReceiver {
    public static AusuReceiver instance = new AusuReceiver();

    private int[] mStatusbarDataType = new int[2];

    public static AusuReceiver getInstance() {
        return instance;
    }

    public void register(Context context) {
        context.registerReceiver(mDataTypeInfoReceiver,
                new IntentFilter("com.asus.systemui.action.NOTIFY_DATA_TYPE_INFO"),
                "com.asus.systemui.permission.DATA_TYPE_INFO", null);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(mDataTypeInfoReceiver);
    }

    private final BroadcastReceiver mDataTypeInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("slotId", -1);
//            int intExtra2 = intent.getIntExtra(TetherService.EXTRA_SUBID, -1);
            int intExtra3 = intent.getIntExtra("dataType", -1);
            Log.d("SimStatus", "onReceive(): intent.getAction() = " + intent.getAction() +
                    " ,slotId = " + intExtra +
//                    " ,subId = " + intExtra2 +
                    " ,dataType = " + intExtra3);
            if (intExtra >= 0) {
                mStatusbarDataType[intExtra] = intExtra3;
//                updateNetworkType();
            }
        }
    };

    public int[] getmStatusbarDataType() {
        return mStatusbarDataType;
    }
}
