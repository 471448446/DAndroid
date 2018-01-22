package better.library.utils;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by better on 2017/5/4 10:42.
 */

public class Utils {
    public static void log(String msg) {
        log("Better", msg);
    }

    public static void log(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void toast(Context context, String ms) {
        if (null == context || null == ms) return;
        Toast.makeText(context, ms, Toast.LENGTH_SHORT).show();
    }

    public static String getEditTextString(EditText et) {
        Editable text = et.getText();
        return text != null && text.toString().trim().length() != 0 ? text.toString().trim() : null;
    }

    public static String getTextViewString(TextView tv) {
        String text = tv.getText().toString().trim();
        return text.length() == 0 ? null : text;
    }

}
