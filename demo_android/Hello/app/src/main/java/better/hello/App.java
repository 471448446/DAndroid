package better.hello;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Des 还是按功能划分package
 * Create By better on 2016/10/14 14:02.
 */

public class App extends TinkerApplication {
    private static Application application;
    private RefWatcher refWatcher;

    public App() {
        super(ShareConstants.TINKER_ENABLE_ALL, "better.hello.AppTikner",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        AppConfig.init();
        refWatcher = LeakCanary.install(this);
//        CrashReport.initCrashReport(this, getString(R.string.Bugly_ID), AppConfig.isLog);
    }

    public static Application getApplication() {
        return application;
    }

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }
}
