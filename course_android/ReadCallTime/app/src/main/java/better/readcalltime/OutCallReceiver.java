package better.readcalltime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by better on 2017/8/22 14:25.
 */

public class OutCallReceiver extends BroadcastReceiver {
    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    // 电话挂断
                    Log.d("Better", "o挂断电话");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // 来电响铃
                    Log.d("Better", "o来电响铃");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    // 来电接通
                    Log.d("Better", "o来电接通");
                    break;
            }
        }
    };

    public IntentFilter get() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Better","onReceive");
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != manager) {
            manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}
