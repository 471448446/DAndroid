package better.readcalltime;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by better on 2017/8/31 10:28.
 */

public class CallListenHelper {
    private Context mContext;


    PhoneStateListener listener = new PhoneStateListener() {
        boolean hasCall;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                // 电话挂断
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("Better", "1挂断电话");
                    if (!hasCall) {
                        return;
                    }
                    hasCall = false;
                    Intent intent = new Intent(mContext, OutCallReceiver.class);
                    intent.setAction(Intent.ACTION_NEW_OUTGOING_CALL);
                    mContext.sendBroadcast(intent);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    hasCall = true;
                    // 摘机（正在通话中）
                    Log.d("Better", "2等待ing");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    // 来电接通
                    Log.d("Better", "3来电响铃");
                    break;
            }
        }
    };

    public void register(Context context) {
        mContext = context;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != manager) {
            manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

}
