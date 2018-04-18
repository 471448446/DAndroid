package better.learn.showtip;

import android.app.Application;

/**
 * Created by better on 2018/1/7 14:37.
 */

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
