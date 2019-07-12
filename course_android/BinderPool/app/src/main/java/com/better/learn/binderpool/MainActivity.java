package com.better.learn.binderpool;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBinderPool binder = IBinderPool.Stub.asInterface(service);
            try {
                IBook iBook = IBook.Stub.asInterface(binder.basicTypes(BinderServices.ID_BOOK));
                iBook.helloBook("东周列国志", 100.9);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                IAuthor iAuthor = IAuthor.Stub.asInterface(binder.basicTypes(BinderServices.ID_AUTHOR));
                iAuthor.helloAuthor("冯梦龙", 40);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(this, BinderServices.class), serviceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
