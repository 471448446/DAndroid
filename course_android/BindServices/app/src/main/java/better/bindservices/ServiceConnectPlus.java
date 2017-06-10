package better.bindservices;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 主要是用于判断服务链接没有
 * Created by better on 2017/6/6 13:36.
 */

public class ServiceConnectPlus implements ServiceConnection {
    private boolean isBind;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        isBind = true;

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        isBind = false;

    }

    public boolean isBind() {
        return isBind;
    }
}
