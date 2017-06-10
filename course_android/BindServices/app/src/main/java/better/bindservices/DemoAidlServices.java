package better.bindservices;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

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
        public void registerCallBack(ILocCallBack callBack) throws RemoteException {
            mCallbackList.register(callBack);
        }

        @Override
        public void unRegisterCallBack(ILocCallBack callBack) throws RemoteException {
            mCallbackList.unregister(callBack);

        }
    }
}
