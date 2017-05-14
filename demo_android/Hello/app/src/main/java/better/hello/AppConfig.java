package better.hello;

import better.hello.util.Utils;

/**
 * Created by better on 2016/12/20.
 */

public class AppConfig {
    public static boolean isLog = true;
    /* 分页大小  --better 2017/1/3 15:35. */
    public static int PAGES_SIZE = 20;

    public static void init() {
        if (3 == BuildConfig.appProductType) {
            isLog = false;
            Utils.LOG_LEVEL = Utils.NOTHING;
        } else {
            isLog = true;
            Utils.LOG_LEVEL = Utils.VERBOSE;
        }
    }
}
