package better.bindservices;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import better.bindservices.BaseServices;

/**
 *
 * https://github.com/android/platform_development/blob/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerService.java
 * https://github.com/android/platform_development/blob/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerServiceActivities.java
 * Created by better on 2017/6/6 13:43.
 */

public class DemoMessengerService extends BaseServices {
    public static final int INIT = 1;
    /**
     * 处理客户端的请求
     */
    private android.os.Handler mHandler = new android.os.Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case INIT:
                    Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
                    break;

            }
            return false;
        }
    });
    private android.os.Messenger mMessenger = new android.os.Messenger(mHandler);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
