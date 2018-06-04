package better.learn.mobileinfo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import better.library.CustomBroadReceiver;
import better.library.utils.GPSUtls;
import better.library.utils.Utils;

/**
 * Des http://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device
 * http://www.jianshu.com/p/be244fb85a4e
 * http://wetest.qq.com/lab/view/116.html
 * Create By better on 2016/12/28 14:34.
 */
public class MobileInfoActivity extends AppCompatActivity {
    TextView txtHandWare, txtStatusBattery, txtStatusBlue, txtStatusGps, txtStatusWifi, txtCpu;

    CustomBroadReceiver receiverHDStatus = new CustomBroadReceiver() {
        @Override
        public IntentFilter getIntentFilter() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
            intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            return intentFilter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_BATTERY_CHANGED:
                    showBatteryInfo(intent);
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    // 需要 BLUETOOTH,BLUETOOTH_ADMIN 权限
                    showBlueToothInfo(intent);
                    break;
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    showGpsInfo(intent);
                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    showWifiInfo(intent);
                    break;
            }

        }
    };

    private void showWifiInfo(Intent intent) {
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
        String msg;
        switch (wifiState) {
            case WifiManager.WIFI_STATE_ENABLED:
                msg = "打开";
                break;
            case WifiManager.WIFI_STATE_DISABLED:
            case WifiManager.WIFI_STATE_DISABLING:
                msg = "关闭";
                break;
            default:
                msg = "未知";
                break;
        }
        txtStatusWifi.setText("WiFi:" + msg);
    }

    private void showGpsInfo(Intent intent) {
        String msg;
        if (GPSUtls.isEnabled(MobileInfoActivity.this)) {
            msg = "开开";
        } else {
            msg = "关闭";
        }
        txtStatusGps.setText("GPS：" + msg);
    }

    private void showBlueToothInfo(Intent intent) {
        int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
        String msg;
        switch (blueState) {
            case BluetoothAdapter.STATE_ON:
                msg = "已打开";
                break;
            case BluetoothAdapter.STATE_OFF:
                msg = "已关闭";
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                msg = "打开动作";
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                msg = "关闭动作";
                break;
            default:
                msg = "未知";
                break;
        }

        txtStatusBlue.setText("蓝牙：" + msg);
    }

    private void showBatteryInfo(Intent intent) {
        int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        float temperature = temp / 10f;
        txtStatusBattery.setText(String.format("电池温度：%1$.1f C", temperature));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 10) {
            showHandWareInfo();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_info);
        txtHandWare = findViewById(R.id.txtHandWare);
        txtStatusBattery = findViewById(R.id.txtStatusBattery);
        txtStatusBlue = findViewById(R.id.txtStatusBlue);
        txtStatusGps = findViewById(R.id.txtStatusGps);
        txtStatusWifi = findViewById(R.id.txtStatusWifi);
        txtCpu = findViewById(R.id.txtCPU);
        showHandWareInfo();
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 10);
            Toast.makeText(this, "授予权限", Toast.LENGTH_SHORT).show();
        }
        registerReceiver(receiverHDStatus, receiverHDStatus.getIntentFilter());

        showCpu();
        txtCpu.post(new Runnable() {
            @Override
            public void run() {
                StringBuilder builder = new StringBuilder(Utils.getTextViewString(txtCpu));
                builder.append(String.format("\nCpu 使用:%1$.2f%%", CpuUtil.getCpuLoad()));
                txtCpu.setText(builder);
            }
        });
    }

    private void showCpu() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nCpu 1温度°C:" + CpuUtil.getCpuTemperature1());
        builder.append("\nCpu 温度°C:" + CpuUtil.getCpuTemperature());

        txtCpu.setText(builder);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverHDStatus);

    }

    private void showHandWareInfo() {
        String infoIP = "serial=" + Util.getSerial(this) +
                "\nmei=" + Util.getIMEI() +
                "\nmsi=" + Util.getIMSI() +
                "\nwifi ip=" + Util.getWifi2Ip(this) +
                "\nmac=" + Util.getMacFromWifi(this) +
                "\nip=" + Util.getLocalIpAddress();

        txtHandWare.setText(infoIP);
    }
}
