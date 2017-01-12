package better.hello.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by better on 2016/10/24.
 */

public class RegularUtils {
    /*120*120*/
    private static String reg_wh = "\\*";

    public static int[] wh(String widthInfo) {
        int[] wh = new int[2];
        Pattern p = Pattern.compile(reg_wh);
        String[] split = p.split(widthInfo);
        if (null != split && split.length > 1) {
            wh[0] = Integer.valueOf(split[0]);
            wh[1] = Integer.valueOf(split[1]);
        }
        return wh;
    }

    public static String getImageId(String url) {
        if (TextUtils.isEmpty(url)) return "";
        String[] res = url.split("\\|");
        if (null == res || res.length <= 1) return "";
        return res[1];
    }
}
