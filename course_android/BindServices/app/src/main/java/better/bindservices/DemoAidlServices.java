package better.bindservices;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import better.bindservices.data.MyMessage;

/**
 * https://github.com/android/platform_development/blob/master/samples/ApiDemos/src/com/example/android/apis/app/RemoteService.java
 * Create By better on 2017/6/7 15:40.
 */
public class DemoAidlServices extends BaseServices {
    DemoAidlServicesStub mBinder = new DemoAidlServicesStub();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0, l = mBinder.mCallbackList.beginBroadcast(); i < l; i++) {
                        mBinder.mCallbackList.getBroadcastItem(i).locSuccess();
                    }
                    mBinder.mCallbackList.finishBroadcast();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    MyMessage messageIn = new MyMessage("Hello", "World!"), messageOut = new MyMessage("Hello", "World!"), messageInOut = new MyMessage("Hello", "World!");
                    log2("准备的数据》s");
                    logMessage("in", messageIn);
                    logMessage("out", messageOut);
                    logMessage("inout", messageInOut);
                    log2("准备的数据》e");
                    log2("服务端传递返回数据--》start");
                    for (int i = 0, l = mBinder.mCallbackList.beginBroadcast(); i < l; i++) {
                        logMessage("in", mBinder.mCallbackList.getBroadcastItem(i).callMessageIn(messageIn));
                        logMessage("out", mBinder.mCallbackList.getBroadcastItem(i).callMessageOut(messageOut));
                        logMessage("inout", mBinder.mCallbackList.getBroadcastItem(i).callMessageInOut(messageInOut));
                    }
                    mBinder.mCallbackList.finishBroadcast();
                    log2("服务端传递返回数据--》end ");
                    log2("原来的数据=====》s");
                    logMessage("in", messageIn);
                    logMessage("out", messageOut);
                    logMessage("inout", messageInOut);
                    log2("原来的数据=====》e");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, 4000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinder.mCallbackList.kill();
    }

    private class DemoAidlServicesStub extends IInterCommunication.Stub {

        public android.os.RemoteCallbackList<ILocCallBack> mCallbackList = new android.os.RemoteCallbackList();

        @Override
        public double getLat() throws RemoteException {
            return 89.00002;
        }

        @Override
        public void sendYouAMessage(String msg) throws RemoteException {
            toastInUi("客户端发来消息：" + msg);
            log(msg);
        }

        @Override
        public void sendInt(int length) throws RemoteException {
            log("int：" + length);
        }

        @Override
        public void sendMyMessage(MyMessage msg) throws RemoteException {
            toastInUi("客户端发来消息->" + msg.toString());
            log(msg.toString());
        }

        @Override
        public MyMessage messageIn(MyMessage msg) throws RemoteException {
            log2("得到客户端传递数据=》" + msg.toString());
            msg.setContent("messageIn set Content");
            return msg;
        }

        @Override
        public MyMessage messageOut(MyMessage msg) throws RemoteException {
            log2("得到客户端传递数据=》" + msg.toString());
            msg.setName("服务端修改名字");
            return msg;
        }

        @Override
        public MyMessage messageInOut(MyMessage msg) throws RemoteException {
            log2("得到客户端传递数据=》" + msg.toString());
            msg.setName("服务端修改名字");
            return msg;
        }

        @Override
        public void registerCallBack(ILocCallBack callBack) throws RemoteException {
            mCallbackList.register(callBack);
        }

        @Override
        public void unRegisterCallBack(ILocCallBack callBack) throws RemoteException {
            mCallbackList.unregister(callBack);

        }
    }

    private void log2(String ms) {
        Log.d("Better", ms);
    }

    private void logMessage(String tag, MyMessage msg) {
        Log.d("Better", tag + "==>" + msg == null ? "null" : msg.toString());
    }
}
