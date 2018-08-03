package better.demo.contentprovidersp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class TestService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        log("onCreate");
        log("SharePrf.KEY_ONE:" + SharePrf.getString(SharePrf.KEY_ONE));
        SharePrf.setString(SharePrf.KEY_ONE, "TestService onCreate");
        log("SharePrf.KEY_ONE:" + SharePrf.getString(SharePrf.KEY_ONE));

        SharePreProvider.setString(SharePrf.KEY_THREE, "provider设置三");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SharePrf.setString(SharePrf.KEY_TWO, "key_two" + System.currentTimeMillis());
                log("save two" + SharePrf.getString(SharePrf.KEY_TWO) + ";KEY_THREE= " + SharePreProvider.getStr(SharePrf.KEY_THREE));
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //KEY_THREE= null
                //adb shell ps |findstr better
                //adb root
                //adb shell kill pid
                log("s 后当adb kill 主进程后查询 " + ";KEY_THREE= " + SharePreProvider.getStr(SharePrf.KEY_THREE));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log("s +1后当adb kill 主进程后查询 " + ";KEY_THREE= " + SharePreProvider.getStr(SharePrf.KEY_THREE));

            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }

    private void log(String msg) {
        Log.d("Better", "TestService:" + msg);
    }
}
