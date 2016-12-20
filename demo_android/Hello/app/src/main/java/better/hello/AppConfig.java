package better.hello;

import better.hello.util.Utils;

/**
 * Created by better on 2016/12/20.
 */

public class AppConfig {
    public static boolean isLog = true;

    public static void init() {
        if (3 == BuildConfig.appProductType) {
            Utils.LOG_LEVEL = Utils.NOTHING;
        } else {
            Utils.LOG_LEVEL = Utils.VERBOSE;
        }
    }
}
