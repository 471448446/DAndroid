package better.syshelper;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by better on 2016/10/18.
 */

public class LockHelper {

    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;
    Activity ctx;

    public LockHelper(Activity ctx) {
        this.ctx=ctx;
        devicePolicyManager = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(ctx, LockScreenAdmin.class);
    }

    public void lock(){
        lockOrApply(true);
    }
    public void lockOrApply(boolean apply) {
        if (isLockActive()) {
            devicePolicyManager.lockNow();
            ctx.finish();
        } else {
            if (apply) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, ctx.getString(R.string.des));
                ctx.startActivity(intent);
            }
        }
    }

    public boolean isLockActive() {
        return devicePolicyManager.isAdminActive(componentName);
    }
}
