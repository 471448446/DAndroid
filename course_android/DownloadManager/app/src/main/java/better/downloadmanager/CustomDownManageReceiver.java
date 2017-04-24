package better.downloadmanager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by better on 2017/4/24 15:29.
 */

public class CustomDownManageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long localId = context.getSharedPreferences("myApp", Context.MODE_APPEND).getLong("download", 0);
        if (localId == downId) {
            Intent install = new Intent(Intent.ACTION_VIEW);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadFileUri = downloadManager.getUriForDownloadedFile(downId);

            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }
}
