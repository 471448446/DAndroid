package better.demo.contentprovidersp;

import android.content.Context;

public class SharePrf {
    private static final String FILE_NAME = "Test";
    public static final String KEY_ONE = "key_one";
    public static final String KEY_TWO = "key_two";
    public static final String KEY_THREE = "key_three";

    public static String getString(String key) {

        return getString(App.getInstance(), key);
    }

    public static String getString(Context context, String key) {

        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString(key, "");
    }

    public static void setString(String key, String msg) {
        setString(App.getInstance(), key, msg);
    }

    public static void setString(Context c, String key, String msg) {
        c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit().putString(key, msg).apply();
    }
}
