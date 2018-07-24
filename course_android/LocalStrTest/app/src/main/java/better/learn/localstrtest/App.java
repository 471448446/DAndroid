package better.learn.localstrtest;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by better on 2018/5/2 17:27.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocaleUtil.updateAppLanguage(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleUtil.updateAppLanguage(this);
    }
}
