package com.better.learn.teststart;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TestPing {

    public void alog() {
        Log.e("Better", "prepare watch native");
        Log.e("Better", "prepare watch service");
    }

    public void test(Context context, String parent) {
        File v6 = new File(parent);

//        Log.e("Better", v6.getAbsolutePath());
        copy(context, v6);

//        Log.e("Better", "main() kill decode :" + v6);
        String b = String.valueOf(v6 != null);
        Log.e("Better", "main() kill decode :" + b + "," + v6);
//        Log.e("Better", "main() kill:" + Process.myPid());
    }

    public static void copy(Context context, File file) {
        File parent = new File(context.getExternalCacheDir(), "init");
        if (!parent.exists()) {
            parent.mkdir();
        }
        File outFile = new File(parent, file.getName());
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (!outFile.exists()) {
                outFile.createNewFile();
            }

            inputStream = new FileInputStream(file);
            outputStream = new FileOutputStream(outFile);

            final byte[] buf = new byte[8192];
            final int bufferReadLimit = 8192;
            long pBytesLeftToRead = 0;
            int read;

            while ((read = inputStream.read(buf, 0, bufferReadLimit)) != -1) {
                if (pBytesLeftToRead > read) {
                    outputStream.write(buf, 0, read);
                    pBytesLeftToRead -= read;
                } else {
                    outputStream.write(buf, 0, (int) pBytesLeftToRead);
                    break;
                }
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
