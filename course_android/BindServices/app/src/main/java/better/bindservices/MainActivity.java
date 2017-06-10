package better.bindservices;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import better.bindservices.data.MyMessage;

public class MainActivity extends AppCompatActivity {

    private DemoBinderServices mDemoServices;
    private ServiceConnectPlus mBinderConnection = new ServiceConnectPlus() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            super.onServiceConnected(name, service);
            DemoBinderServices.MyBinder myBinder = (DemoBinderServices.MyBinder) service;
            mDemoServices = myBinder.getServices();
        }
    };

    private android.os.Messenger mMessenger;
    private ServiceConnectPlus mMessengerConnect = new ServiceConnectPlus() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            super.onServiceConnected(name, service);
            mMessenger = new Messenger(service);
        }
    };

    private IInterCommunication mIInterCommunication;
    private ServiceConnectPlus mAidlConnect = new ServiceConnectPlus() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            super.onServiceConnected(name, service);
            mIInterCommunication = IInterCommunication.Stub.asInterface(service);
            try {
                mIInterCommunication.registerCallBack(mCallBack);
                mIInterCommunication.sendInt(8);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            super.onServiceDisconnected(name);
            try {
                mIInterCommunication.unRegisterCallBack(mCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mIInterCommunication = null;
        }
    };
    private ILocCallBack mCallBack = new ILocCallBack.Stub() {
        @Override
        public void locSuccess() throws RemoteException {
            toastInMain("服务端确认定位成功");
        }

        @Override
        public void locFail() throws RemoteException {
            toastInMain("服务端确认定位失败");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(MainActivity.this, DemoBinderServices.class), mBinderConnection, Service.BIND_AUTO_CREATE);
        bindService(new Intent(MainActivity.this, DemoMessengerService.class), mMessengerConnect, Service.BIND_AUTO_CREATE);
        bindService(new Intent(MainActivity.this, DemoAidlServices.class), mAidlConnect, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinderConnection.isBind()) {
            unbindService(mBinderConnection);
        }
        if (mMessengerConnect.isBind()) {
            unbindService(mMessengerConnect);
        }
        if (mAidlConnect.isBind()) {
            unbindService(mAidlConnect);
        }
    }

    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.binder:
                if (mBinderConnection.isBind()) {
                    toast("获取到" + mDemoServices.getLat());
                } else {
                    toast("还没准备好");
                }
                break;
            case R.id.messenger:
                if (mMessengerConnect.isBind()) {
                    try {
                        mMessenger.send(android.os.Message.obtain(null, DemoMessengerService.INIT));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    toast("还没准备好");
                }

                break;
            case R.id.aidl_get:
                if (mAidlConnect.isBind()) {
                    try {
                        toast("获取到：" + mIInterCommunication.getLat());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    toast("还没准备好");
                }
                break;
            case R.id.aidl_send:
                if (mAidlConnect.isBind()) {
                    try {
                        mIInterCommunication.sendYouAMessage("Fuck");
                        mIInterCommunication.sendMyMessage(new MyMessage("张三", "哈哈哈"));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    toast("还没准备好");
                }
                break;
        }
    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void toastInMain(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void log(String msg) {
        Log.d(this.getClass().getSimpleName(), msg);
    }
}
