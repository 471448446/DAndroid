package better.hello.social.bugly;

import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import better.hello.AppConfig;
import better.hello.R;

/**
 * Created by better on 2017/5/11 11:40.
 */

public class BuglyConfig {
    public static void init(Context context) {
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = true;
        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
        Bugly.setIsDevelopmentDevice(context, false);

//        CrashReport.initCrashReport(context, context.getString(R.string.Bugly_ID), AppConfig.isLog);
        Bugly.init(context, context.getString(R.string.Bugly_ID), AppConfig.isLog);
    }
}
