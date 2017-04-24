package better.downloadmanager;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * file:///storage/emulated/0/test/test.apk
 * Create By better on 2017/4/24 15:47.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DownloadManager downloadManager;
    private long downId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.status).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                down();
                break;
            case R.id.status:
                checkStatus();
                break;
        }
    }

    private void checkStatus() {
        if (-1 == downId) {
            toast("还没有下载");
            return;
        }
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downId);
        Cursor cursor = downloadManager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
            switch (status) {
                case DownloadManager.STATUS_PENDING:
                    toast("准备下载");
                    break;
                case DownloadManager.STATUS_PAUSED:
                    toast("暂停");
                    break;
                case DownloadManager.STATUS_RUNNING:
                    toast("下载中");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    toast("下载成功");
                    break;
                case DownloadManager.STATUS_FAILED:
                    toast("失败");
                    break;
            }
            log("" + path);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void down() {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://wdj-uc1-apk.wdjcdn.com/2/5d/ddecc0a4904d8239cddaf64f666635d2.apk"));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        /* 任意位置  --better 2017/4/24 15:55. */
//        request.setDestinationUri()
        /* 外部存储  --better 2017/4/24 15:55. */
        request.setDestinationInExternalPublicDir("test", "test.apk");
        /* 内部存储  --better 2017/4/24 15:55. */
//        request.setDestinationInExternalFilesDir(MainActivity.this, "apk", "test.apk");
        request.setTitle("下载文件")
                .setDescription("正在为你下载文件")
                .setMimeType("application/vnd.android.package-archive");

        downId = downloadManager.enqueue(request);
        SharedPreferences preferences = getSharedPreferences("myApp", MODE_APPEND);
        preferences.edit().putLong("download", downId).commit();

        log("下载Id=" + downId);
    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void log(String msg) {
        Log.d("Better", msg);
    }
}
