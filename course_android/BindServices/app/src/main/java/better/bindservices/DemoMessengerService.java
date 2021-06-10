package better.bindservices;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import android.widget.Toast;

/**
 * https://github.com/android/platform_development/blob/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerService.java
 * https://github.com/android/platform_development/blob/master/samples/ApiDemos/src/com/example/android/apis/app/MessengerServiceActivities.java
 * Created by better on 2017/6/6 13:43.
 */

public class DemoMessengerService extends BaseServices {
    public static final int INIT = 1;
    public static final int BIG_BITMAP = 2;
    public static final String EXTRA_STR = "extra_str";
    public static final String EXTRA_BITMAP = "extra_bitmap";
    /**
     * 处理客户端的请求
     */
    private android.os.Handler mHandler = new android.os.Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case INIT:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(EXTRA_STR), Toast.LENGTH_SHORT).show();
                    //回复
                    android.os.Messenger messenger = msg.replyTo;
                    android.os.Message message = android.os.Message.obtain(null, INIT);
                    android.os.Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_STR, "Hello 客户端");
                    message.setData(bundle);
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                case BIG_BITMAP:
                    android.os.Message msgBitmap = android.os.Message.obtain(null, BIG_BITMAP);
                    android.os.Bundle extra = new Bundle();
                    extra.putParcelable(EXTRA_BITMAP, msg.getData().getParcelable(EXTRA_BITMAP));
                    msgBitmap.setData(extra);
                    try {
                        msg.replyTo.send(msgBitmap);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    });
    private Messenger mMessenger = new android.os.Messenger(mHandler);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
