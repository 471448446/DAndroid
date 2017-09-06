package better.readcalltime;

import android.app.Application;

/**
 * Created by better on 2017/8/31 10:56.
 */

public class App extends Application {
    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
