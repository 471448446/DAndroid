package better.bindservices;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 用Messager 可以很好的实现双向交互，
 * Bindler呢？
 * Created by better on 2017/6/6 13:22.
 */

public class DemoBinderServices extends BaseServices {

    public static class MyBinder extends android.os.Binder {
        DemoBinderServices mDemoServices;

        public MyBinder(DemoBinderServices demoServices) {
            mDemoServices = demoServices;
        }

        public DemoBinderServices getServices() {
            return mDemoServices;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder(this);
    }

    /**
     * 向外暴露的方法
     */
    public double getLat() {
        Log.d("Binder", "getLat()" + Thread.currentThread().getName());
        return 78.90098;
    }
}
