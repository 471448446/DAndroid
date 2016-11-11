package better.hello.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import better.hello.util.C;
import better.hello.util.Utils;

/**
 * Created by better on 2016/11/11.
 */

public class NotificationClickReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.d("Better","点击事件："+intent.getStringExtra(C.EXTRA_FIRST));
        Utils.openFile(context,intent.getStringExtra(C.EXTRA_FIRST));
    }
}
