package better.oinstallapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

/**
 * http://developers.googleblog.cn/2017/08/android-o_29.html
 * 安装文件还需授予读的权限，不然还是会报错
 * Create By better on 2017/11/12 15:02.
 */
public class MainActivity extends AppCompatActivity {

    String updateFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        findViewById(R.id.main_check).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                check();
//            }
//        });
//        findViewById(R.id.main_open).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                open();
//            }
//        });

        findViewById(R.id.main_install).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installFile();
            }
        });
//        toast("New");
        updateFilePath = android.os.Environment.getExternalStorageDirectory().

                getAbsolutePath() +
                File.separator + "HelloO" + File.separator + "app-release.apk";
    }

//    /**
//     * 必须申请权限  java.lang.SecurityException: Need to declare android.permission.REQUEST_INSTALL_PACKAGES to call this api
//     * Create By better on 2017/11/12 14:59.
//     */
//    private void check() {
//        PackageManager packageManager = getPackageManager();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            toast("具有安装权限？" + packageManager.canRequestPackageInstalls());
//        }
//    }
//
//    private void open() {
//        startActivity(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES));
//    }
//
//    private void toast(String msg) {
//        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//    }

    private void installFile() {
        File f = new File(updateFilePath);
        Uri uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", f);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri,
                "application/vnd.android.package-archive");
        Log.d("Main", updateFilePath + "\n" + uri.getPath() + "\nexist:" + f.exists());
        startActivity(intent);
    }

}
