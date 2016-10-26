package better.hello;

import android.app.Application;

import better.hello.util.Utils;

/**
 * Des 还是按功能划分package
 * Create By better on 2016/10/14 14:02.
 */

public class App extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        Utils.LOG_LEVEL=Utils.VERBOSE;
    }
    public static Application getApplication(){
        return application;
    }
}
