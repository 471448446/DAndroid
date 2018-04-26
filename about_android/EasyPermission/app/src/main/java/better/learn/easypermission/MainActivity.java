package better.learn.easypermission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 编译sdk低于26就不行
 * Create By better on 2018/4/26 17:03.
 */
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private static final int STORGE_CODE = 10;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        l("___onRequestPermissionsResult___" + requestCode + ";" + grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,
                this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (EasyPermissions.hasPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})) {
            toast("已获取存储权限");

        } else {
            EasyPermissions.requestPermissions(this, "你已拒绝多次拉🌶", STORGE_CODE, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

//    @AfterPermissionGranted(STORGE_CODE)
//    public void getStorge() {
//        toast("获取成功");
//    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void l(String msg) {
        Log.d(this.getClass().getSimpleName(), msg);

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        toast("🙃获取成功");
        l("========onPermissionsGranted_" + requestCode + ";" + perms.toArray());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        l("————————onPermissionsDenied" + requestCode + ";" + perms.toArray());
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        l("继续？————Yes");
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        l("继续————No");
    }
}
