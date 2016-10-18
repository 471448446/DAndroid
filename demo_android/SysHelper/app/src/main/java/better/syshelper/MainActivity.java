package better.syshelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Des http://stackoverflow.com/questions/32984849/restart-android-device-programmatically
 * http://www.android100.net/html/201308/26/4061.html
 * Create By better on 2016/10/18 10:57.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    LockHelper lockHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.lock).setOnClickListener(this);
        findViewById(R.id.lock_short_cut).setOnClickListener(this);
        findViewById(R.id.lock_reBoot).setOnClickListener(this);
        lockHelper = new LockHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lock:
                if (lockHelper.isLockActive()) {
                    lockHelper.lock();
                } else {
                    startActivity(new Intent(MainActivity.this, LockActivity.class));
                }
                break;
            case R.id.lock_short_cut:
                ShortCutUtil.addShortCut(MainActivity.this, R.string.lock, R.mipmap.ic_launcher, LockActivity.class);
                break;
            case R.id.lock_reBoot:
//                Intent intent=new Intent(Intent.ACTION_REBOOT);
//                intent.putExtra("nowait", 1);
//                intent.putExtra("interval", 1);
//                intent.putExtra("window", 0);
//                sendBroadcast(intent);

//                PowerManager pManager=(PowerManager) getSystemService(Context.POWER_SERVICE);
//                pManager.reboot("");
                new AlertDialog.Builder(MainActivity.this).setMessage("重启").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot"});
                            proc.waitFor();
                        } catch (Exception ex) {
                            Toast.makeText(MainActivity.this, "手机为root", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create().show();

                break;
        }

    }
}
