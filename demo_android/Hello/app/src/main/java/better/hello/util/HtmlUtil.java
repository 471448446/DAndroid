package better.hello.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import better.hello.data.bean.NewsDetailsBean;

/**
 * Created by better on 2016/10/23.
 */

public class HtmlUtil {
    /**
     * Des You have to add a version check and use the old method on Android M and below, on Android N and higher you should use the new method. If you don't add a version check your app will break on lower Android versions.
     * Create By better on 2016/10/23 10:17.
     */
    public static Spanned from(String body) {
        if (Utils.isBelowAndroidVersion(Build.VERSION_CODES.N)) {
            return Html.fromHtml(body);
        } else {
            return Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY);
        }
    }

    public static Spanned from(String body, Html.ImageGetter getter) {
        if (Utils.isBelowAndroidVersion(Build.VERSION_CODES.N)) {
            return Html.fromHtml(body, getter, null);
        } else {
            return Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY, getter, null);
        }
    }

    private static String getImgTag(NewsDetailsBean.ImgBean bean) {
//        int[] wh=RegularUtils.wh(bean.getPixel());
//        if (null!=wh&&wh.length>1){
//            return "<img src=\"" + bean.getSrc()+"\"width=\""+wh[0]+"\" "+"height=\""+wh[1]+"\"" + "/>";
//        }
        return "<img src=\"" + bean.getSrc() + "\"/>";
    }


    /**
     * Des
     * Create By better on 2016/10/24 15:05.
     */
    public static String createNewsImgTag(NewsDetailsBean bean) {
        String body = bean.getBody();
        if (!TextUtils.isEmpty(body) && null != bean.getImg() && !bean.getImg().isEmpty()) {
            for (int i = 0, l = bean.getImg().size(); i < l; i++) {
                if (body.contains(bean.getImg().get(i).getRef())) {
                    boolean isLast = i == l - 1;
                    body = body.replace(bean.getImg().get(i).getRef(), getImgTag(bean.getImg().get(i)) + getExchangeLineTag(isLast));
                }
            }
        }
//        Utils.d("Better", "\n========>" + body);
        return body;
    }

    /**
     * Des 换行，每个图片一行
     * Create By better on 2016/10/26 10:34.
     */
    private static String getExchangeLineTag(boolean isLast) {
        return isLast ? "" : "<br/>";
    }
}
