package com.test.handwardtest;

public class SystemProperties {

    private static final String TAG = "SystemProperties";

    public static boolean getBoolean(String key, boolean def) {
        try {
            return (Boolean) Class.forName("android.os.SystemProperties")
                    .getDeclaredMethod("getBoolean", String.class, boolean.class)
                    .invoke(null, key, def);
        } catch (Throwable e) {
            return def;
        }
    }

    public static long getLong(String key, long def) {
        try {
            return (Long) Class.forName("android.os.SystemProperties")
                    .getDeclaredMethod("getLong", String.class, long.class)
                    .invoke(null, key, def);
        } catch (Throwable e) {
            return def;
        }
    }

    public static int getInt(String key, int def) {
        try {
            return (Integer) Class.forName("android.os.SystemProperties")
                    .getDeclaredMethod("getInt", String.class, int.class)
                    .invoke(null, key, def);
        } catch (Throwable e) {
            return def;
        }
    }

    public static String get(String key) {
        try {
            return (String) Class.forName("android.os.SystemProperties")
                    .getDeclaredMethod("get", String.class)
                    .invoke(null, key);
        } catch (Throwable e) {
            return "";
        }
    }

    public static String get(String key, String def) {
        try {
            return (String) Class.forName("android.os.SystemProperties")
                    .getDeclaredMethod("get", String.class, String.class)
                    .invoke(null, key, def);
        } catch (Throwable e) {
            return def;
        }
    }
}
