package better.hello.util;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;


public class JsonUtils {

	private static final Gson gson = new Gson();

	public static String getParams(HashMap<String, Object> data) {
		return toJson(data);
	}

	public static String toJson(Object src) {
		return gson.toJson(src);
	}

	public static <T> T fromJson(String json, Type typeOfT) {
		try {
			return gson.fromJson(json, typeOfT);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return gson.fromJson(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String toBase64(String str) {
		String value = "";
		try {
			value = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
		} catch (Exception e) {
			Log.e("error", "字符转Base64错误");
			e.printStackTrace();
		}
		return value;
	}
}
