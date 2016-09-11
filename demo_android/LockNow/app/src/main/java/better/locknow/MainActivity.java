package better.locknow;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Des 设备管理器
 * Create By better on 16/9/5 17:07.
 */
public class MainActivity extends Activity {
    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    View lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.lock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockOrApply(true);
            }
        });
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, LockScreenAdmin.class);
        lockOrApply(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lockOrApply(false);
    }

    private void lockOrApply(boolean apply) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow();
            finish();
        } else {
            if (apply) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.des));
                startActivity(intent);
            }
        }
    }
}
