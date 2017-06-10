package better.bindservices;

import android.util.Log;

/**
 * Created by better on 2017/6/8 21:25.
 */

public class LogServicesLiveRunnable implements Runnable {
    String name;
    boolean isLog;

    public void setLog(boolean log) {
        isLog = log;
    }

    public LogServicesLiveRunnable(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        while (isLog) {
            Log.d(name, "lives");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
