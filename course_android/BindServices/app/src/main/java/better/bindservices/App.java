package better.bindservices;

import android.app.Application;
import android.util.Log;

/**
 * Created by better on 2017/6/8 15:24.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Better", "Application onCreate()");
    }
}
