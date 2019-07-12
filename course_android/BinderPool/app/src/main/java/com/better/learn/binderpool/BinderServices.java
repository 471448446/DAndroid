package com.better.learn.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class BinderServices extends Service {
    public static final int ID_AUTHOR = 10;
    public static final int ID_BOOK = 11;

    private Binder binder = new IBinderPool.Stub() {

        @Override
        public IBinder basicTypes(int binderID) throws RemoteException {
            switch (binderID) {
                case ID_AUTHOR:
                    return new IAuthor.Stub() {
                        @Override
                        public void helloAuthor(String name, int age) throws RemoteException {
                            Log.e("Better", "helloBook() " + name + "," + age);
                        }
                    };
                case ID_BOOK:
                    return new IBook.Stub() {
                        @Override
                        public void helloBook(String name, double money) throws RemoteException {
                            Log.e("Better", "helloBook() " + name + "," + money);
                        }
                    };
            }
            return null;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
