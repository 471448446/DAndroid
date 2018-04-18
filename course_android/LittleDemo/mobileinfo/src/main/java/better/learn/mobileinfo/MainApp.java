package better.learn.mobileinfo;

import android.app.Application;

/**
 * Created by better on 2016/12/28.
 */

public class MainApp extends Application {
    static MainApp mMainApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mMainApp=this;
    }

    static Application getInstance(){
        return mMainApp;
    }
}
