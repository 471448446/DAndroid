package better.hello;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Des 还是按功能划分package
 * Create By better on 2016/10/14 14:02.
 */

public class App extends Application {
    private static Application application;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        AppConfig.init();
        refWatcher = LeakCanary.install(this);
        CrashReport.initCrashReport(this,getString(R.string.Bugly_ID),AppConfig.isLog);
    }
    public static Application getApplication(){
        return application;
    }
    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }
}
