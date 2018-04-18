package better.learn.mobileinfo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Des http://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device
 * http://www.jianshu.com/p/be244fb85a4e
 * http://wetest.qq.com/lab/view/116.html
 * Create By better on 2016/12/28 14:34.
 */
public class MobileInfoActivity extends AppCompatActivity {
    TextView txt;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 10) {
            showInfo();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_info);
        txt = (TextView) findViewById(R.id.txt);
        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            showInfo();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 10);
            Toast.makeText(this, "授予权限", Toast.LENGTH_SHORT).show();
        }

    }

    private void showInfo() {
        String info = "mei=" + Util.getIMEI() + "\n,msi=" + Util.getIMSI() + ",wifi ip=" + Util.getWifi2Ip(this) + ",mac=" + Util.getMacFromWifi(this)
                + ",\n ip=" + Util.getLocalIpAddress();
        txt.setText(info);
    }
}
