package better.permissiongrand;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示如何动态获取权限
 */
public class MainActivity extends AppCompatActivity {
    TextView textViewl;
    final String mobile = "18200141846";
    List<String> mutiPermission = new ArrayList<>();
    String call = Manifest.permission.CALL_PHONE;
    String storage = Manifest.permission.READ_EXTERNAL_STORAGE;
    String location = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewl = (TextView) findViewById(R.id.txt);
        findViewById(R.id.signal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //无权限下,直接call crash
//                callPhone(mobile);
                //第一种方式授权
                signalGrant(mobile);

            }
        });
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用兼容包方法授权
                mutiGrant();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionGrantHelper.REQ_CODE_PHONE:
                if (PackageManager.PERMISSION_GRANTED != grantResults[0]) {
                    toastShort("你决绝了拨打电话权限" + String.valueOf(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, call)));
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, call)) {
                        showDialog();
                    }
//                    showDialog();
//                    showDialog(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION});
                } else {
                    callPhone(mobile);
                }
                break;
            case PermissionGrantHelper.REQ_CODE_LOCATION:
                StringBuilder builder = new StringBuilder("你授权了:\n");
                int count = 0;
                for (int i = 0, l = permissions.length; i < l; i++) {
                    if (PermissionGrantHelper.isGrantedSuccess(call, permissions[i], grantResults[i])) {
                        builder.append(call + "\n");
                        count++;
                    } else if (PermissionGrantHelper.isGrantedSuccess(location, permissions[i], grantResults[i])) {
                        builder.append(location + "\n");
                        count++;
                    } else if (PermissionGrantHelper.isGrantedSuccess(storage, permissions[i], grantResults[i])) {
                        builder.append(storage + "\n");
                        count++;
                    }
                }
                textViewl.setText(builder.toString());
                if (count < 3) {
                    showDialog();
                }
                break;
        }
    }

    private void mutiGrant() {
        mutiPermission.clear();
        addPermission(call);
        addPermission(storage);
        addPermission(location);
        if (!mutiPermission.isEmpty())
            ActivityCompat.requestPermissions(MainActivity.this, mutiPermission.toArray(new String[mutiPermission.size()]), PermissionGrantHelper.REQ_CODE_LOCATION);
    }

    private void signalGrant(String num) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = new String[]{call};
            if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(call)) {
                //第一次shouldShowRequestPermissionRationale 返回时false,以后只要点击了不在询问返回为false否者返回true。
//                if (!shouldShowRequestPermissionRationale(call)) {
//                    showDialog(permissions);
//                } else {
//                    ActivityCompat.requestPermissions(MainActivity.this, permissions, PermissionGrantHelper.REQ_CODE_PHONE);
//                }
                ActivityCompat.requestPermissions(MainActivity.this, permissions, PermissionGrantHelper.REQ_CODE_PHONE);
            } else {
                callPhone(num);
            }
        } else {
            callPhone(num);
        }
    }

    private void showDialog(final String[] permissions) {
        new AlertDialog.Builder(MainActivity.this).setMessage("你需要去应许应用权限").setPositiveButton("要的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this, permissions, PermissionGrantHelper.REQ_CODE_PHONE);
            }
        }).create().show();
    }

    /**
     * Des 引导用户去授权
     * Create By better on 16/8/11 13:50.
     */
    private void showDialog() {
        showDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String packageName = BuildConfig.APPLICATION_ID;
                Uri uri = Uri.parse("package:" + packageName);
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
                if (null != intent.resolveActivity(getPackageManager())) {
                    startActivity(intent);
                } else {
                    toastShort("无效");
                }
            }
        });
    }

    private void showDialog(DialogInterface.OnClickListener clickListener) {
        new AlertDialog.Builder(MainActivity.this).setMessage("你需要去授权应用权限").setPositiveButton("要的", clickListener).create().show();
    }

    /**
     * 权限
     *
     * @param num not null
     */
    public void callPhone(String num) {
        Activity context = MainActivity.this;
        if (TextUtils.isEmpty(num)) return;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + num));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            toastShort("没有拨打电话应用");
        }
    }

    private void addPermission(String location) {
        if (!PermissionGrantHelper.isGrantedThisPermission(MainActivity.this, location)) {
            mutiPermission.add(location);
        }
    }

    private void toastShort(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
