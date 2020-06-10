package better.bindservices;

import android.app.Service;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import better.bindservices.data.MyMessage;

/**
 * in:接受端能收到值，修传递值不影响原来的值
 * out:接收端收不到值，但收到了值得副本（new），原来的值的所有字段被设置为副本值对应的字段值。修改副本值，原来的值对应字段被修改。
 * inout：接收端能收到值，修改接受值会影响原来的值（对应的字段值会被修改）
 * <p>
 * 发现只要用了out标记，传递的值所有的字段都会被初始化为默认值。
 * <p>
 * Messager-》客户端和服务端相发信息，Messenger通信本质上也就是Handler发送处理消息，一次只处理一个请求，所以不需要考虑线程同步的问题。
 * AIDL-》服务端发送数据，客户端修改得到的数据原样返回，服务端再查看数据
 * Create By better on 2017/6/30 10:03.
 */
public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    private DemoBinderServices mDemoServices;
    private ServiceConnectPlus mBinderConnection = new ServiceConnectPlus() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            super.onServiceConnected(name, service);
            DemoBinderServices.MyBinder myBinder = (DemoBinderServices.MyBinder) service;
            mDemoServices = myBinder.getServices();
        }
    };

    private android.os.Handler mHandler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DemoMessengerService.INIT:
                    toast(msg.getData().getString(DemoMessengerService.EXTRA_STR));
                    break;
                case DemoMessengerService.BIG_BITMAP:
                    Bitmap bitmap = msg.getData().getParcelable(DemoMessengerService.EXTRA_BITMAP);
                    if (null != bitmap) {
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 300, 150, true);
                        imageView.setImageBitmap(scaledBitmap);
                    } else {
                        toast("shit no back bitmap");
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private android.os.Messenger mSeverMessenger, mCustomerMessager;
    private ServiceConnectPlus mMessengerConnect = new ServiceConnectPlus() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            super.onServiceConnected(name, service);
            mSeverMessenger = new Messenger(service);
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

        @Override
        public MyMessage callMessageOut(MyMessage msg) throws RemoteException {
            log("得到服务端数据_out=》" + msg.toString());
            msg.setName("客户端修改名字");
            return msg;
        }

        @Override
        public MyMessage callMessageIn(MyMessage msg) throws RemoteException {
            log("得到服务端数据_in=》" + msg.toString());
            msg.setName("客户端修改名字");
            return msg;
        }

        @Override
        public MyMessage callMessageInOut(MyMessage msg) throws RemoteException {
            log("得到服务端数据_inOut=》" + msg.toString());
            msg.setName("客户端修改名字");
            return msg;
        }

    };
    /**
     * 准备大图 进程传递
     */
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.bitmap);
        // 这里简单加载bitmap
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.xl);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        toast("bitmap load done");
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(MainActivity.this, DemoBinderServices.class), mBinderConnection, Service.BIND_AUTO_CREATE);
        bindService(new Intent(MainActivity.this, DemoMessengerService.class), mMessengerConnect, Service.BIND_AUTO_CREATE);
        bindService(new Intent(MainActivity.this, DemoAidlServices.class), mAidlConnect, Service.BIND_AUTO_CREATE);
        mCustomerMessager = new Messenger(mHandler);
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
                    //联系服务端
                    android.os.Message message = android.os.Message.obtain(null, DemoMessengerService.INIT);
                    android.os.Bundle bundle = new Bundle();
                    bundle.putString(DemoMessengerService.EXTRA_STR, "Hello 服务端");
                    message.setData(bundle);
                    // 指定回复者
                    message.replyTo = mCustomerMessager;
                    try {
                        //send 开始进程间通信
                        mSeverMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

//                    try {
//                        mSeverMessenger.send(android.os.Message.obtain(null, DemoMessengerService.INIT));
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
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
            case R.id.aidl_in_Out_InOut:
                if (!mAidlConnect.isBind()) {
                    return;
                }
                try {
                    MyMessage myMessageIn = new MyMessage("A", "a");
                    MyMessage myMessageOut = new MyMessage("A", "a");
                    MyMessage myMessageInOut = new MyMessage("A", "a");
                    log("客户端准备的数据》s");
                    log("In", myMessageIn);
                    log("Out", myMessageOut);
                    log("InOut", myMessageInOut);
                    log("客户端准备的数据》e");
                    log("客户端传递返回数据---》s");
                    log("in", mIInterCommunication.messageIn(myMessageIn));
                    log("out", mIInterCommunication.messageOut(myMessageOut));
                    log("inOut", mIInterCommunication.messageInOut(myMessageInOut));
                    log("客户端传递返回数据---》e");
                    log("原来的数据====》s");
                    log("In", myMessageIn);
                    log("Out", myMessageOut);
                    log("InOut", myMessageInOut);
                    log("原来的数据====》e");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.messenger_bitmap:
                if (null == bitmap || !mMessengerConnect.isBind()) {
                    toast("还没准备好");
                    return;
                }
                Message message = Message.obtain(null, DemoMessengerService.BIG_BITMAP);
                Bundle bundle = new Bundle();
                bundle.putParcelable(DemoMessengerService.EXTRA_BITMAP, bitmap);
                message.setData(bundle);
                message.replyTo = mCustomerMessager;
                try {
                    mSeverMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
            default:
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

    private void log(String tag, MyMessage msg) {
        Log.d("Better", tag + "==>" + ((msg == null) ? "null" : msg.toString()));
    }
}
