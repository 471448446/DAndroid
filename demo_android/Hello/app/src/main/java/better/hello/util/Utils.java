package better.hello.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import better.hello.R;
import better.hello.data.bean.ImagesDetailsBean;
import better.hello.data.bean.ShareBean;
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
            close(is);
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
        Uri fileUri = FileProvider.getUriForFile(activity, "better.hello.fileprovider", temp_file);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, getMimeType(temp_file.getAbsolutePath()));
        if (null != intent.resolveActivity(activity.getPackageManager())) {
            activity.startActivity(intent);
        } else {
            Utils.toastShort(activity, "没有打开文件的应用");
        }
    }
//    public static void openFile(Context activity, String path) {
//        File temp_file = new File(path);
//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(temp_file), getMimeType(temp_file.getAbsolutePath()));
//        if (null != intent.resolveActivity(activity.getPackageManager())) {
//            activity.startActivity(intent);
//        } else {
//            Utils.toastShort(activity, "没有打开文件的应用");
//        }
//    }

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

    /**
     * Des 欢迎页图片不回收应用使用内存20M回收后11.9M
     * Create By better on 2016/11/11 16:57.
     */
    public static void recycleImageViewBitmap(ImageView... imageViews) {
        if (null == imageViews || imageViews.length == 0) return;
        for (ImageView imageView : imageViews) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                if (null != bitmapDrawable.getBitmap()) {
                    bitmapDrawable.getBitmap().recycle();
                }
            }
        }
    }

    public static void close(Closeable... closeables) throws IOException {
        if (null != closeables) {
            for (Closeable closeable : closeables) {
                if (null != closeable) closeable.close();
            }
        }
    }

    public static void shareTxt(Activity activity, ShareBean bean) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.share_msg, bean.getTitle(), bean.getShareUrl()));
        intent.setType("text/plain");
        PackageManager packageManager = activity.getPackageManager();
        if (null != packageManager && intent.resolveActivity(packageManager) != null) {
            activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.please_choose)), 1);
        } else {
            Utils.toastShort(activity, activity.getString(R.string.share_no));
        }
    }

    public static ArrayList<ImagesDetailsBean> pranse(List<ImagesDetailsBean> list) {
        ArrayList<ImagesDetailsBean> arrayList = new ArrayList<>();
        if (null == list) return arrayList;
        for (int i = 0, l = list.size(); i < l; i++) {
            arrayList.add(list.get(i));
        }
        return arrayList;
    }
}
