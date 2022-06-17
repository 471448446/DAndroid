package com.better.app.framework;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePre {
    private static SharedPreferences test;

    public static void init(Context context) {
        test = context.getSharedPreferences("test",  Context.MODE_APPEND);
    }

    public static String getString(String spKey) {
        return test.getString(spKey, "");
    }

    public static void setString(String spKey, String value) {
        test.edit().putString(spKey, value).apply();
    }
}
