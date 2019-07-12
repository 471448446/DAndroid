// IBinderPool.aidl
package com.better.learn.binderpool;
import android.os.IBinder;

// Declare any non-default types here with import statements

interface IBinderPool {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    IBinder basicTypes(int binderID);
}
