package better.hello.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.MimeTypeMap;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import better.lib.utils.BaseUtils;

/**
 * Created by better on 2016/10/17.
 */

public class Utils extends BaseUtils {
    public static Document getDocumentByIS(InputStream is) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            doc = builder.parse(is);
            is.close();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Des http://stackoverflow.com/questions/6265298/action-view-intent-for-a-file-with-unknown-mimetype
     * Create By better on 2016/11/11 15:39.
     */
    public static void openFile(Context activity, String path) {
        File temp_file = new File(path);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(temp_file), getMimeType(temp_file.getAbsolutePath()));
        if (null!=intent.resolveActivity(activity.getPackageManager())){
            activity.startActivity(intent);
        }else {
            Utils.toastShort(activity,"没有打开文件的应用");
        }
    }

    private static String getMimeType(String url) {
        String parts[] = url.split("\\.");
        String extension = parts[parts.length - 1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * Des 判断系统是否小于Android的某个版本
     * Create By better on 2016/10/23 10:14.
     */
    public static boolean isBelowAndroidVersion(int version) {
        return Build.VERSION.SDK_INT < version;
    }

}
