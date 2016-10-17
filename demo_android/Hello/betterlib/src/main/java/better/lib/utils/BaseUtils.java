package better.lib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtils {
    /**
     * Log wrapper</br>
     * 你只需要在开发阶段将LEVEL 指定成VERBOSE，当项目正式上线的时候将LEVEL 指定成NOTHING 就可以了。
     * 只需要修改LEVEL 常量的值，就可以自由地控制日志的打印行为了。比如让LEVEL 等于VERBOSE 就可以把所有的日志都打印出来，
     * 让LEVEL 等于WARN 就可以只打印警告以上级别的日志，让LEVEL 等于NOTHING 就可以把所有日志都屏蔽掉
     */
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static int LOG_LEVEL = VERBOSE;


    public static void v(String tag, String msg) {
        if (LOG_LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

    /**
     * Toast
     */
    public static void toastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, int msgId) {
        Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show();
    }

    /**
     * Refresh view status
     */
    public static void setVisibleGone(View view, View... views) {
        if (null != view && view.getVisibility() != View.VISIBLE)
            view.setVisibility(View.VISIBLE);
        setGone(views);
    }

    public static void setGone(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (null != view && view.getVisibility() != View.GONE)
                    view.setVisibility(View.GONE);
            }
        }
    }

    public static void setVisible(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (null != view && view.getVisibility() != View.VISIBLE)
                    view.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void setEnable(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null && !view.isEnabled()) {
                    view.setEnabled(true);
                }
            }
        }
    }

    public static void setDisable(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null && view.isEnabled()) {
                    view.setEnabled(false);
                }
            }
        }
    }

    public static void setInvisible(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (null != view && view.getVisibility() != View.INVISIBLE)
                    view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Get widget text string
     */
    public static String getEditTextString(EditText et) {
        final Editable text = et.getText();
        if (null == text || text.toString().trim().length() == 0) {
            return null;
        }
        return text.toString().trim();
    }

    public static String getTextViewString(TextView tv) {
        final String text = tv.getText().toString().trim();
        if (0 == text.length()) {
            return null;
        }
        return text;
    }

    /**
     * 判别手机是否为正确手机号码；
     * 电信中国电信手机号码开头数字
     * 133、153、180、181、189、177
     * 联通中国联通手机号码开头数字
     * 130、131、132、145、155、156、185、186
     * 移动中国移动手机号码开头数字
     * 134、135、136、137、138、139、147、
     * 150、151、152、157、158、159、182、183、184、187、188
     */
    public static boolean isMobileNum(String phone) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0,1,2,3,5,6,7,8,9])|(18[0-9]|14[57]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 密码只能为6~20位的字母，数字，下划线
     */
    public static boolean isValidPassword(String password) {
        Pattern p = Pattern.compile("^\\w{5,19}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 输入内容不能包含特殊字符： ^%&',;=?$\"
     */
    public static boolean containSpecialChar(String content) {
        Pattern p = Pattern.compile("[^%&',;=?$\\x22]+");
        Matcher m = p.matcher(content);
        return m.matches();
    }

//	public static String transferIs2String(InputStream is) {
//		String str = null;
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//		byte[] buffer = new byte[1024];
//		int len = 0;
//		try {
//			while ((len = is.read(buffer)) != -1) {
//				outStream.write(buffer, 0, len);
//			}
//			str = new String(outStream.toByteArray(), HTTP.UTF_8);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return str;
//	}

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}