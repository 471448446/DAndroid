package better.hello.util;

import android.os.Build;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
    public static Document getDocmentByIS(InputStream is) {
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
     * Des 判断系统是否小于Android的某个版本
     * Create By better on 2016/10/23 10:14.
     */
    public static boolean isBelowAndroidVersion(int version) {
        return Build.VERSION.SDK_INT < version;
    }
}
