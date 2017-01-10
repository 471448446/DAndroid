package better.hello.http.download;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import better.hello.common.UIHelper;
import better.hello.data.bean.DownloadInfo;
import better.hello.data.bean.DownloadingInfo;
import better.hello.http.call.RequestCallback;
import better.hello.http.call.RequestInfo;
import better.hello.http.wait.NotificationWaitPolice;
import better.hello.util.C;
import better.hello.util.Utils;
import rx.Subscription;

/**
 * Des 下载文件
 * Create By better on 2016/11/9 16:24.
 */
public class DownLoadService extends Service implements RequestCallback<DownloadingInfo> {
    private final String TAG = "DownLoadService";
    private Subscription mSubscription;
    private DownloadInfo mDownloadInfo;

    public static void start(Context c, DownloadInfo downloadInfo) {
        Intent intent = new Intent(c, DownLoadService.class);
        intent.putExtra(C.EXTRA_BEAN, downloadInfo);
        c.startService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.d(TAG, "onDestroy");
        if (null != mSubscription) mSubscription.unsubscribe();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    protected void onHandleIntent(Intent intent) {
        Utils.d(TAG, "onHandleIntent");
        if (null == intent) return;
        mDownloadInfo = intent.getParcelableExtra(C.EXTRA_BEAN);
        if (null == mDownloadInfo || TextUtils.isEmpty(mDownloadInfo.getFileName()) || TextUtils.isEmpty(mDownloadInfo.getUrl()))
            return;
        Utils.d(TAG, mDownloadInfo.toString());
//        NotificationWaitPolice police = new NotificationWaitPolice();
//        try {
//            for (int i = 0, l = 20; i <= l; i++) {
//                DownloadingInfo downloadInfo = new DownloadingInfo(l);
//                downloadInfo.setReadFileSize(i);
//                police.onNext(downloadInfo);
//                i++;
//                Thread.sleep(100);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        mSubscription = UIHelper.downLoad(new RequestInfo<>(this, new NotificationWaitPolice()), mDownloadInfo.getFileName(), mDownloadInfo.getUrl());
    }

    @Override
    public void onError(RequestInfo<DownloadingInfo> requestInfo, String msg) {

    }

    @Override
    public void onSuccess(RequestInfo<DownloadingInfo> requestInfo, DownloadingInfo data, Object o) {

    }

    @Override
    public void onStart(RequestInfo<DownloadingInfo> requestInfo) {

    }

    @Override
    public void onComplete(RequestInfo<DownloadingInfo> requestInfo) {
        if (null != requestInfo.getWaitPolicy()) {
            ((NotificationWaitPolice) requestInfo.getWaitPolicy()).disappear(mDownloadInfo);
        }
        stopSelf();
    }
}
