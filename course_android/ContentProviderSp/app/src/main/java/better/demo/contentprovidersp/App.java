package better.demo.contentprovidersp;

import android.app.Application;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

public class App extends Application {
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Log.d("Better", "________init" + Process.myPid());
        startService(new Intent(this, TestService.class));
    }

    public static App getInstance() {
        return sInstance;
    }
}
