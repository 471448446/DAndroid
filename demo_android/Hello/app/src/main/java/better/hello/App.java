package better.hello;

import android.app.Application;

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
    }
    public static Application getApplication(){
        return application;
    }
}
