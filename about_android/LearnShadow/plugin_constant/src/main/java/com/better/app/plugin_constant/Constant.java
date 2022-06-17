package com.better.app.plugin_constant;

public interface Constant {

    String KEY_PLUGIN_ZIP_PATH = "pluginZipPath";
    String KEY_ACTIVITY_CLASSNAME = "KEY_ACTIVITY_CLASSNAME";
    String KEY_EXTRAS = "KEY_EXTRAS";
    String KEY_PLUGIN_PART_KEY = "KEY_PLUGIN_PART_KEY";
    long FROM_ID_START_ACTIVITY = 1001;
    long FROM_ID_CALL_SERVICE = 1002;

    interface Plugin1 {
        // 为了方便其它地方调用，外一写错了呢
        String PART_KEY = "plugin-demo01";
        String PATH = "/data/local/tmp/plugin-debug.zip";
        String ACTIVITY_MAIN = "com.better.app.plugin1.MainActivity";
        String ACTIVITY_MAIN_JAVA = "com.better.app.plugin1.MainActivityJava";
        String SERVICE1 = "com.better.app.plugin1.MyService";
    }
}
