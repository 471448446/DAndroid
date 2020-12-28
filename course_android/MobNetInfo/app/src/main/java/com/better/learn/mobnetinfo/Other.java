package com.better.learn.mobnetinfo;

public class Other {

    public static String get(String key) {
        try {
            return (String) Class.forName("android.os.SystemProperties")
                    .getDeclaredMethod("get", String.class)
                    .invoke(null, key);
        } catch (Throwable e) {
            return "";
        }
    }

}
