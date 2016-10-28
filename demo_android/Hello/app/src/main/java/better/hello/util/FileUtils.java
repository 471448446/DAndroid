package better.hello.util;

import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import better.hello.App;

/**
 * Created by better on 2016/10/19.
 */

public class FileUtils {
    /*一级目录*/
    public static final String FILE_ROOT = "Hello";
    /*二级目录-欢迎页图片*/
    public static final String FILE_SPLASH = "splash";

    public static InputStream readFileFromRaw(int fileID) {
        return App.getApplication().getResources().openRawResource(fileID);
    }

    public static boolean isLoadTodaySplash(Context ctx) {
        return new File(getTodaySplashImagePath(ctx)).exists();
    }

    public static String getTodaySplashImagePath(Context ctx) {
        String todayFileName = (String) DateFormat.format("yyyy-MM-dd", Calendar.getInstance());
        return getSplashFileDir(ctx) + File.separator + todayFileName + ".jpg";
    }

    public static String makeDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
            return dir.getAbsolutePath();
        } else {
            return path;
        }
    }

    public static boolean writeSplash(Context ctx, InputStream ins) throws IOException {
        Utils.d("Better", "今日文件路径：" + getTodaySplashImagePath(ctx));
        File todayFile = new File(getTodaySplashImagePath(ctx));
//        try {
//            return writeFile(ins, todayFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Utils.e("Better","写入文件出错："+e.getMessage());
//        }
//        return false;
        return writeFile(ins, todayFile);

    }

    public static boolean writeFile(InputStream ins, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file, true);
        int readSize;
        byte buffer[] = new byte[1024];
        while ((readSize = ins.read(buffer/*不要忘了写*/)) > 0) {
            outputStream.write(buffer, 0, readSize);
        }
        if (null != outputStream) {
//            outputStream.flush();
            outputStream.close();
        }
        if (null != ins) {
            ins.close();
        }
        return true;
    }

    /**
     * Des 获取app的缓存根目录
     * Create By better on 2016/10/28 09:49.
     */
    public static String getStorageRootPath(Context context) {
        return makeDir(getDiskCacheDir(context, FILE_ROOT));
    }

    public static String getSplashFileDir(Context context) {
        return makeDir(getStorageRootPath(context) + File.separator + FILE_SPLASH);
    }

    public static String getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (externalExist()) {
            cachePath = Environment.getExternalStorageDirectory().getPath();
            Utils.v("Better", "存在sdk" + cachePath);
        } else {
            cachePath = context.getCacheDir().getPath();
            Utils.v("Better", "不存在sdk" + cachePath);
        }
        return cachePath + File.separator + uniqueName;
    }

    /**
     * Des 是否挂载了外部sdk
     * Create By better on 2016/10/28 09:43.
     */
    public static boolean externalExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable();
    }

    public static String getFilePathName(String... args) {
        String[] param = args;
        String Str = args[0];
        int i = 1;

        for (int len = args.length; i < len; ++i) {
            if (Str == null) {
                return null;
            }

            if (Str.endsWith(File.separator) && param[i].startsWith(File.separator)) {
                Str = Str.substring(0, Str.length() - 1) + param[i];
            } else if (!Str.endsWith(File.separator) && !param[i].startsWith(File.separator)) {
                Str = Str + File.separator + param[i];
            } else {
                Str = Str + param[i];
            }
        }

        return Str;
    }
}
