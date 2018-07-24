package better.learn.localstrtest;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by better on 2018/5/2 17:22.
 */
public class LocaleUtil {

    public static void updateAppLanguage(Context ctx) {
        Resources resources = ctx.getResources();
        Configuration configuration = resources.getConfiguration();

        if (configuration.locale.getLanguage().equals("zh")) {
            configuration.locale = Locale.CHINA;
        } else {
            configuration.locale = Locale.US;
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
