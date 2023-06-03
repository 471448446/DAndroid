package better.learn.view.useage.screencap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import better.library.utils.Utils;

/**
 * Created by better on 2018/1/15 13:40.
 */

public class ScreenCapHelper {
    public interface ISaveBitmap {
        void onFinish();
    }



    public static Bitmap getScrollViewBitmap(ScrollView scrollView) {
        int h = 0;
        View child;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            child = scrollView.getChildAt(i);
            h += child.getHeight();
            child.setBackgroundColor(ContextCompat.getColor(scrollView.getContext(), android.R.color.white));
        }
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    public static void saveBitmap(final AppCompatActivity activity, final Bitmap bitmap, final ISaveBitmap l) {
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Utils.toast(activity, "磁盘权限");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DemoView";
                final String path = dirPath + File.separator + System.currentTimeMillis() + ".png";
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                Utils.log(path);
                File file = new File(path);
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    OutputStream outputStream = new FileOutputStream(file);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    bufferedOutputStream.write(byteArrayOutputStream.toByteArray());
                    bufferedOutputStream.flush();

                    bufferedOutputStream.close();
                    byteArrayOutputStream.close();
                    if (!activity.isFinishing()) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.toast(activity, "截图成功");
                                Utils.log("path:" + path);
                            }
                        });
                    }
                    if (null != l) {
                        l.onFinish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
