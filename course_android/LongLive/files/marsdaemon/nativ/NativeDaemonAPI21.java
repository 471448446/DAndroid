package com.better.learn.longlive.nativeforlive.marsdaemon.nativ;

import android.content.Context;
import android.util.Log;
import com.better.learn.longlive.nativeforlive.marsdaemon.NativeDaemonBase;

/**
 * native code to watch each other when api over 21 (contains 21)
 *
 * @author Mars
 */
public class NativeDaemonAPI21 extends NativeDaemonBase {

    public NativeDaemonAPI21(Context context) {
        super(context);
    }

    static {
        Log.e("Fucker", "load 21");
        try {
            System.loadLibrary("daemon_api21");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Fucker", "load 21" + e.getMessage());
        }
    }

    public native void doDaemon(String indicatorSelfPath, String indicatorDaemonPath, String observerSelfPath, String observerDaemonPath);
}
