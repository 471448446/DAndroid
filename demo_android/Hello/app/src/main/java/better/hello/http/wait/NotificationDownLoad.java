package better.hello.http.wait;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import better.hello.R;
import better.hello.common.LikeRx;
import better.hello.data.bean.DownloadingInfo;
import better.hello.util.Utils;

/**
 * https://developer.android.com/guide/topics/ui/notifiers/notifications.html#Progress
 *
 * @deprecated Created by better on 2016/11/3.
 */
public class NotificationDownLoad implements LikeRx<DownloadingInfo> {
    private Context mContext;
    private NotificationManager mManager;
    private NotificationCompat.Builder mBuilder;
    private int mId;

    public NotificationDownLoad(Context ctx) {
        this.mContext = ctx;
        mId = getId();
        mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(
                mContext).setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))// largeicon
                .setOngoing(false)// true使notification变为ongoing，用户不能手动清除，类似QQ,false或者不设置则为普通的通知
                .setAutoCancel(true);
    }

    private int getId() {
        String string = String.valueOf(System.currentTimeMillis());
        //Invalid int: "1478620469458"
        return Integer.valueOf(string.substring(string.length() / 3 * 2, string.length()));
    }

    @Override
    public void onStart() {
        Utils.toastShort(mContext, mContext.getString(R.string.down_start));
    }

    @Override
    public void onNext(DownloadingInfo info) {
        mManager.notify(mId, mBuilder.setProgress((int) info.getTotalFileSize(), (int) info.getReadFileSize(), false).build());
    }

    public void onNext(String info) {
        mManager.notify(mId, mBuilder.setContentText(info).build());
    }

    @Override
    public void onComplete() {
        onComplete("");
    }

    @Override
    public void onError(String error) {

    }

    public void onComplete(String des) {
        mManager.notify(mId, mBuilder.setContentTitle(mContext.getString(R.string.down_ok)).setContentText(des).setSound(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.notify)).build());
    }
}
