package better.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Data save&get by SharedPreferences.
 *  You can either add parameters and methods there
 *
 */
public class BaseSharedPref {
	// xml file name
	protected static String PREFS_NAME = "com.android.spfn";
	// default return value, adjust by request
	public final static int PREFS_INT_INVALID = -1;
	public final static String PREFS_STR_INVALID = "";
	public final static int PREFS_COLOR_INVALID = 1;
	
	public BaseSharedPref(String name) {
		Log.i("BaseSharedPref", name);
		PREFS_NAME = name;
	}
	
	/**
	 * Current support type: Boolean, String, Int
	 * Add methods as following format if need more type
	 */
	public static void setBoolean(String key, boolean value, Context c) {
		SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static boolean getBoolean(String key, Context c){
		return c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(key, false);
	}

	public static void setString(String key, String value, Context c) {
		SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getString(String key, Context c) {
		return c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(key, PREFS_STR_INVALID);
	}

	public static boolean isInvalidPrefString(String value) {
		return null == value || PREFS_STR_INVALID.equals(value);
	}
	
	public static void setInvalidPreString(String key, Context c) {
		setString(key, PREFS_STR_INVALID, c);
	}
	
	public static void setInt(String key, int value, Context c) {
		SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static int getInt(String key, Context c) {
		return c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(key, PREFS_INT_INVALID);
	}
	
}
