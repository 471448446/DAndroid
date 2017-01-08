package better.hello.util;

import android.content.Context;
import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import better.hello.App;
import better.hello.data.bean.DownloadingInfo;
import better.hello.data.db.DbHelper;
import okhttp3.ResponseBody;
import rx.Emitter;

/**
 * Created by better on 2016/10/19.
 */

public class FileUtils {
    /*一级目录*/
    public static final String FILE_ROOT = "Hello";
    /*二级目录-欢迎页图片*/
    public static final String FILE_SPLASH = "splash";
    public static final String FILE_VIDEO = "video";
    public static final String FILE_CACHE = "cache";
    public static final String FILE_DB = "db";

    public static final String MAP_4 = ".mp4";
    public static final String JPG = ".jpg";
    public static final String SPLASH_FILE_NAME = "splash" + JPG;

    public static InputStream readFileFromRaw(int fileID) {
        return App.getApplication().getResources().openRawResource(fileID);
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

//    public static boolean writeFile(InputStream ins, File file) throws IOException {
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        FileOutputStream outputStream = new FileOutputStream(file, true);
//        int readSize;
//        byte buffer[] = new byte[1024];
//        while ((readSize = ins.read(buffer/*不要忘了写*/)) > 0) {
//            outputStream.write(buffer, 0, readSize);
//        }
//        if (null != outputStream) {
////            outputStream.flush();
//            outputStream.close();
//        }
//        if (null != ins) {
//            ins.close();
//        }
//        return true;
//    }

    /**
     * Des file一定是存在的
     * Create By better on 2016/11/3 11:04.
     */
    public static boolean writeFile(ResponseBody body, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        long readSize = 0, length = body.contentLength();
        byte buffer[] = new byte[4096];
        FileOutputStream outputStream = new FileOutputStream(file, true);
        InputStream ins = body.byteStream();
        while (true) {
            int read = ins.read(buffer);
            if (read <= 0) {
                break;
            }
            outputStream.write(buffer, 0, read);
            readSize += read;
            Utils.d("Better", "下载了==" + readSize + ",total=" + length);
        }
        outputStream.flush();
        close(outputStream, ins);
        return true;
    }

    public static void close(Closeable... closeables) throws IOException {
        if (null == closeables) return;
        for (Closeable c : closeables
                ) {
            if (null != c) c.close();
        }
    }

    //    public static void writeFile(Subscriber<? super DownloadingInfo> subscriber, ResponseBody body, String fileName) {
//        try {
//            File file = getFile(fileName);
//            BufferedSink sink = Okio.buffer(Okio.sink(file));
//            Buffer buffer = sink.buffer();
//            long readSize = 0, length = body.contentLength(), read;
//            int step = 4069;
//            BufferedSource source = body.source();
//            DownloadingInfo info = new DownloadingInfo(length);
//            while ((read = source.read(buffer, step)) != -1) {
//                readSize += read;
//                info.setReadFileSize(readSize);
//                Utils.d("Better", "下载了==" + readSize + ",total=" + length);
//                subscriber.onNext(info);
//            }
//            sink.close();
//            subscriber.onCompleted();
//        } catch (Exception e) {
//            e.printStackTrace();
//            subscriber.onError(e);
//        }
//    }
    public static void writeFile(final Emitter<DownloadingInfo> subscriber, ResponseBody body, String fileName) {
        try {
            long readSize = 0, length = body.contentLength();
            byte buffer[] = new byte[4096];
            FileOutputStream outputStream = new FileOutputStream(getFile(fileName), true);
            InputStream ins = body.byteStream();
            while (true) {
                int read = ins.read(buffer);
                if (read <= 0) {
                    subscriber.onCompleted();
                    break;
                }
                outputStream.write(buffer, 0, read);
                readSize += read;
                DownloadingInfo info = new DownloadingInfo(length);
                info.setReadFileSize(readSize);
                subscriber.onNext(info);
                Utils.d("Better", "下载了==" + readSize + ",total=" + length);
            }
            outputStream.flush();
            Utils.close(outputStream, ins, body);
        } catch (Exception e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }
//    public static void writeFile(final Subscriber<? super DownloadingInfo> subscriber, ResponseBody body, String fileName) {
//        try {
//            File file = getFile(fileName);
//            long readSize = 0, length = body.contentLength();
//            byte buffer[] = new byte[4096];
//            FileOutputStream outputStream = new FileOutputStream(file, true);
//            InputStream ins = body.byteStream();
//            int count =0;
//            if (!subscriber.isUnsubscribed()) {
//                while (true) {
//                    int read = ins.read(buffer);
//                    if (read <= 0) {
//                        subscriber.onCompleted();
//                        break;
//                    }
//                    outputStream.write(buffer, 0, read);
//                    readSize += read;
//                    DownloadingInfo info = new DownloadingInfo(length);
//                    info.setReadFileSize(readSize);
//                    info.setProgress(readSize / length);
//                    subscriber.onNext(info);
//                    count++;
//                    Utils.d("Better", count+"下载了==" + readSize + ",total=" + length);
//                }
//            }
//            if (null != outputStream) {
//                outputStream.flush();
//                outputStream.close();
//            }
//            if (null != ins) {
//                ins.close();
//            }
//            if (null != body) {
//                body.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            subscriber.onError(e);
//        }
//    }

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

    public static String getVideoFileDir(Context context) {
        return makeDir(getStorageRootPath(context) + File.separator + FILE_VIDEO);
    }

    public static String getCacheFileDir(Context context) {
        return makeDir(getStorageRootPath(context) + File.separator + FILE_CACHE);
    }

    public static String getDbFileDir(Context context) {
        return makeDir(getStorageRootPath(context) + File.separator + FILE_DB);
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

    /**
     * Des 创建文件
     * Create By better on 2016/11/3 10:49.
     */
    public static File getFile(String path) throws IOException {
        File file = new File(path);
        file.delete();
        file.createNewFile();
        return file;
    }

    public static String getVideoFileName(Context context, String fileName) {
        return getVideoFileDir(context) + File.separator + fileName + MAP_4;
    }

    public static String getDBFileName() {
        return getDbFileDir(App.getApplication()) + File.separator + DbHelper.DATABASE_NAME;
    }

    public static String getTodaySplashImagePath(Context ctx) {
        return getSplashFileDir(ctx) + File.separator + SPLASH_FILE_NAME;
    }

    /**
     * Des 不存在且今天没下载
     * Create By better on 2016/11/11 14:43.
     */
    public static boolean isNeedDownLoadTodaySplash(Context ctx) {
        File splashFile = new File(getTodaySplashImagePath(ctx));
        if (!splashFile.exists() || splashFile.length() == 0) return true;
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String today = f.format(new Date());
        return !today.equalsIgnoreCase(f.format(new Date(splashFile.lastModified())));
    }

    public static boolean isCanLoadSplash(Context ctx) {
        return new File(getTodaySplashImagePath(ctx)).exists();
    }

    public static boolean writeSplash(Context ctx, ResponseBody ins) throws IOException {
//        Utils.d("Better", "今日文件路径：" + getTodaySplashImagePath(ctx));
        File todayFile = new File(getTodaySplashImagePath(ctx));
        if (todayFile.exists()) {
            String newName = new SimpleDateFormat("yyyy-MM-dd").format(new Date(todayFile.lastModified())) + JPG;
            todayFile.renameTo(new File(getSplashFileDir(ctx) + File.separator + newName));
        }
        return writeFile(ins, todayFile);
    }

//    public static String getFilePathName(String... args) {
//        String[] param = args;
//        String Str = args[0];
//        int i = 1;
//
//        for (int len = args.length; i < len; ++i) {
//            if (Str == null) {
//                return null;
//            }
//
//            if (Str.endsWith(File.separator) && param[i].startsWith(File.separator)) {
//                Str = Str.substring(0, Str.length() - 1) + param[i];
//            } else if (!Str.endsWith(File.separator) && !param[i].startsWith(File.separator)) {
//                Str = Str + File.separator + param[i];
//            } else {
//                Str = Str + param[i];
//            }
//        }
//
//        return Str;
//    }
}
