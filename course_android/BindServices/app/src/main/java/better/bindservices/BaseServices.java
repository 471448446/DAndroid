package better.bindservices;

import android.app.Service;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by better on 2017/6/6 21:48.
 */

public abstract class BaseServices extends Service {
    boolean isLve;
    LogServicesLiveRunnable mRunnable = new LogServicesLiveRunnable(this.getClass().getSimpleName());
    private android.os.Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            toast((String) msg.obj);
            return false;
        }
    });

    protected void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void toastInUi(String msg) {
        android.os.Message messag = mHandler.obtainMessage();
        messag.obj = msg;
        mHandler.sendMessage(messag);
    }

    protected void log(String msg) {
        Log.d(this.getClass().getSimpleName(), "msg:" + msg);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        log("onCreate()");
        isLve = true;
        mRunnable.setLog(isLve);
        new Thread(mRunnable).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy()");
        isLve = false;
        mRunnable.setLog(isLve);
    }
}
