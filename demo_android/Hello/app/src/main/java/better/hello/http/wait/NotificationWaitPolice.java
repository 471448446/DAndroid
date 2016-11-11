package better.hello.http.wait;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import better.hello.App;
import better.hello.R;
import better.hello.data.bean.DownloadInfo;
import better.hello.data.bean.DownloadingInfo;
import better.hello.reciver.NotificationClickReciver;
import better.hello.util.C;
import better.hello.util.Utils;
import better.lib.waitpolicy.WaitPolicy;

/**
 * Created by better on 2016/11/3.
 */

public class NotificationWaitPolice extends WaitPolicy {
    private Context mContext;
    private NotificationManager mManager;
    private NotificationCompat.Builder mBuilder;
    private int mId;

    public NotificationWaitPolice() {
        this.mContext = App.getApplication();
        mId = getId();
        mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(
                mContext).setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))// largeicon
                .setOngoing(false)
                .setAutoCancel(true);
    }

    private int getId() {
        String string = String.valueOf(System.currentTimeMillis());
        //Invalid int: "1478620469458"
        return Integer.valueOf(string.substring(string.length() / 3 * 2, string.length()));
    }

    @Override
    public void displayLoading(String message) {
        Utils.toastShort(mContext, message);
    }

    @Override
    public void displayLoading() {
        displayLoading(mContext.getString(R.string.down_start));
    }

    @Override
    public void displayRetry(String message) {
        if (!TextUtils.isEmpty(message)) Utils.toastShort(mContext, message);
    }

    @Override
    public void displayRetry() {
        displayRetry(mContext.getString(R.string.down_fail));
    }

    @Override
    public void disappear(String des) {
        mManager.notify(mId, mBuilder.setContentTitle(mContext.getString(R.string.down_ok)).setContentText(des).setSound(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.notify)).build());
    }

    public void disappear(DownloadInfo des) {
        if (null == des || TextUtils.isEmpty(des.getFileName())) {
            disappear();
            return;
        }
        Intent i = new Intent(mContext, NotificationClickReciver.class);
        i.putExtra(C.EXTRA_FIRST, des.getFileName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mManager.notify(mId, mBuilder.setContentTitle(mContext.getString(R.string.down_ok)).setContentText(des.getTitle()).setSound(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.notify))
                .setContentIntent(pendingIntent).build());

    }

    @Override
    public void disappear() {
        disappear("");
    }

    @Override
    public void onNext(Object bean) {
        if (bean instanceof DownloadingInfo) {
            DownloadingInfo info = (DownloadingInfo) bean;
            mManager.notify(mId, mBuilder.setProgress((int) info.getTotalFileSize(), (int) info.getReadFileSize(), false).build());

        }
    }
}
