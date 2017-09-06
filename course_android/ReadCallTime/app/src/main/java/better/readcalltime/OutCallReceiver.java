package better.readcalltime;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * https://www.studytutorial.in/android-phonestatelistener-phone-call-broadcast-receiver-tutorial
 * Created by better on 2017/8/22 14:25.
 */

public class OutCallReceiver extends BroadcastReceiver {
    private boolean callOut;
    private TelephonyManager manager;
    private android.os.Handler mHandler = new android.os.Handler();

    private String getType() {
        return callOut ? "去电:" : "来电:";
    }
//    public IntentFilter get() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
//        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
//        return intentFilter;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Better", "onReceive");
        if (null == manager) {
            manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            callOut = true;
//            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        } else {
//            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            switch (manager.getCallState()) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("Better", getType() + "挂断电话");
                    callOut = false;
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getLog();
                        }
                    }, 1000);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("Better", getType() + "等待ing");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("Better", getType() + "来电响铃");
                    break;
            }
        }
    }

    public void getLog() {
        if (ActivityCompat.checkSelfPermission(App.instance, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            Log.d("better", "没有权限");
            return;
        }
        Cursor cursor = App.instance.getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[]{
                CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE, CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        }, CallLog.Calls.NUMBER + " = ?" + " and " + CallLog.Calls.TYPE + " =?", new String[]{"10086", "2"}, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (null != cursor && cursor.getCount() > 0 && cursor.moveToNext()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            do {
                int callDuration = Integer.parseInt(cursor.getString(4));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String time = (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec);
                date.setTime(Long.parseLong(cursor.getString(3)));
                Log.d("Better",
                        cursor.getString(0) + "," +
                                cursor.getString(1) + "," +
                                cursor.getString(2) + "," +
                                sdf.format(date) + "," +
                                time);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
