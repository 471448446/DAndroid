package better.myapplication;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by better on 16/9/20.
 */
public class MainApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
