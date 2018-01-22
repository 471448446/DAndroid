package better.learn.view.useage.screencap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;

import java.nio.ByteBuffer;

/**
 * Created by better on 2018/1/15 16:23.
 */

public class MediaHelper {
    Activity mActivity;
    int req;

    ImageReader imageReader;
    MediaProjectionManager mManager;
    DisplayMetrics metrics;
    VirtualDisplay mDisplay;

    android.os.Handler mHandler = new Handler();

    public MediaHelper(Activity activity, int i) {
        this.req = i;
        this.mActivity = activity;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        metrics = activity.getResources().getDisplayMetrics();
        imageReader = ImageReader.newInstance(metrics.widthPixels, metrics.heightPixels, PixelFormat.RGBA_8888, 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        final MediaProjection projection = mManager.getMediaProjection(resultCode, data);
        mDisplay = projection.createVirtualDisplay(
                "test", metrics.widthPixels, metrics.heightPixels, metrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.getSurface(), null, null
        );
        //延时主要是让imageReader开始截屏，然后获取数据
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScreenCapHelper.saveBitmap(mActivity, takeScreenShot(projection, mDisplay), new ScreenCapHelper.ISaveBitmap() {
                    @Override
                    public void onFinish() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            mDisplay.release();
                        }
                    }
                });
            }
        }, 100);
    }

    private Bitmap takeScreenShot(MediaProjection projection, VirtualDisplay display) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return bitmap;
        }
        try {
            Image image = imageReader.acquireLatestImage();
            int width = image.getWidth();
            int height = image.getHeight();
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride,
                    height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageReader != null) {
                imageReader.close();
            }
            if (projection != null) {
                projection.stop();
            }
            if (display != null) {
                display.release();//释放
            }
        }
        return bitmap;
    }

    public void prepareMediaScreenCap(Activity activity, int req) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        if (null == mManager) {
            mManager = (MediaProjectionManager) activity.getSystemService(Activity.MEDIA_PROJECTION_SERVICE);
        }

        activity.startActivityForResult(mManager.createScreenCaptureIntent(), req);
    }
}
